import asyncio
import json
import os
import re
import shutil
import subprocess
import time

import grpc
import lz4.frame
import pytest
from aiounittest import AsyncTestCase
from google.protobuf import json_format

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.learning.proto.etl_learning_pb2 import (
    LearningAlert,
    LearningMatch,
    RunEtlLearningRequest,
)
from etl_pipeline.learning.proto.etl_learning_pb2_grpc import EtlLearningServiceStub
from etl_pipeline.learning.proto.historical_decision_learning_pb2 import (
    HistoricalDecisionLearningStoreExchangeRequest,
)
from etl_pipeline.learning.service.connection import HistoricalDecisionExchange
from tests.test_json.constant import (
    HISTORICAL_DECISION_REQUEST,
    HISTORICAL_DECISION_REQUEST_WITHOUT_MARK,
)
from tests.test_json.test_json_pipeline import assert_nested_dict


def load_alert(filepath: str = "notebooks/sample/wm_address_in_payload_format.json"):
    with open(filepath, "r") as f:
        text = json.load(f)
        ids = [
            text[i]
            for key in text
            for i in re.compile(r".*matchRecords\[\d+\].matchId.*").findall(key)
            if i
        ]
        matches_ids = [LearningMatch(match_id=i, match_name="alerts/2/matches/2") for i in ids]
        alert = LearningAlert(batch_id="1", alert_name="alerts/2", learning_matches=matches_ids)
        for key, value in text.items():
            alert.flat_payload[str(key)] = str(value)
    with open("notebooks/sample/event_history.json", "r") as f:
        alert.alert_event_history = f.read()
    return alert


class Promise:
    def __init__(self):
        self.result = None


class HistoricalDecisionStub:
    def __init__(self) -> None:
        self.exchange = HistoricalDecisionExchange()
        self.result = Promise()

    async def start(self):
        await self.exchange.start()

        self.conn = self.exchange.connections[0]
        self.conn.channel.queue_purge(self.conn.messaging_configuration.get("queue-name", ""))

    async def get_message(self):
        self.result.result = next(
            self.conn.channel.consume(self.conn.messaging_configuration.get("queue-name", ""))
        )[-1]


@pytest.mark.asyncio
class TestGrpcServer(AsyncTestCase):
    TIMEOUT = 1
    stub = None

    @classmethod
    def setUpClass(cls):
        service_path = os.path.join(os.environ["CONFIG_APP_DIR"], "service")
        shutil.copyfile(os.path.join(service_path, "service.yaml"), "backup_service_service.yaml")
        shutil.copyfile(
            os.path.join(service_path, "service.ssl.yaml"),
            os.path.join(service_path, "service.yaml"),
        )

        environment = os.environ.copy()
        service_config = ConsulServiceConfig()
        _ = subprocess.Popen("tests/scripts/start_services.sh", env=environment)
        channel = grpc.insecure_channel(
            f"{service_config.etl_service_ip}:{service_config.etl_service_port}"
        )
        cls.stub = EtlLearningServiceStub(channel)
        time.sleep(cls.TIMEOUT)

    @classmethod
    def tearDownClass(cls):

        service_path = os.path.join(os.environ["CONFIG_APP_DIR"], "service")
        process = subprocess.Popen("tests/scripts/kill_services.sh")
        shutil.copyfile("backup_service_service.yaml", os.path.join(service_path, "service.yaml"))
        os.remove("backup_service_service.yaml")
        process.wait()

    @pytest.mark.asyncio
    @pytest.mark.rabbitmq
    async def test_ok_flow(self):
        await self.assert_request(
            "notebooks/sample/wm_address_in_payload_format.json", HISTORICAL_DECISION_REQUEST
        )

    @pytest.mark.asyncio
    @pytest.mark.rabbitmq
    async def test_empty_discriminator(self):
        await self.assert_request(
            "notebooks/sample/wm_address_in_payload_format_two_marks.json",
            HISTORICAL_DECISION_REQUEST_WITHOUT_MARK,
        )

    async def assert_request(self, filename, request):
        historical_decision = HistoricalDecisionStub()
        await historical_decision.start()
        request_alert = load_alert(filename)
        self.stub.RunEtlLearning(RunEtlLearningRequest(learning_alerts=[request_alert]))
        await historical_decision.get_message()
        await asyncio.sleep(1)
        data = lz4.frame.decompress(historical_decision.result.result)
        response = HistoricalDecisionLearningStoreExchangeRequest()
        response.ParseFromString(data)
        assert_nested_dict(request, json_format.MessageToDict(response))
        historical_decision.conn.connection.close()

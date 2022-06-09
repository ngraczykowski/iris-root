import asyncio
import json
import os
import random
import re
import subprocess
import time

import grpc
import pytest
from aio_pika.abc import AbstractIncomingMessage
from aiounittest import AsyncTestCase

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.learning.proto.etl_learning_pb2 import (
    LearningAlert,
    LearningMatch,
    RunEtlLearningRequest,
)
from etl_pipeline.learning.proto.etl_learning_pb2_grpc import EtlLearningServiceStub
from etl_pipeline.learning.service.connection import HistoricalDecisionExchange


def load_alert(filepath: str = "notebooks/sample/wm_address_in_payload_format.json"):
    with open(filepath, "r") as f:
        text = json.load(f)
        ids = [
            text[i]
            for key in text
            for i in re.compile(r".*matchRecords\[\d+\].matchId.*").findall(key)
            if i
        ]
        matches_ids = [
            LearningMatch(match_id=i, match_name=f"alerts/2/matches/{random.randint(1, 100000)}")
            for num, i in enumerate(ids)
        ]
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
        self.queue = await self.conn.channel.get_queue(
            self.conn.messaging_configuration.get("queue-name")
        )

    async def on_message(self, message: AbstractIncomingMessage) -> None:
        async with message.process():
            print(message.body)
            self.result.result = message.body

    async def get_message(self):
        await self.queue.consume(self.on_message)


@pytest.mark.asyncio
class TestGrpcServer(AsyncTestCase):
    TIMEOUT = 1
    stub = None

    @classmethod
    def setUpClass(cls):
        cls.tearDownClass()
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
        try:
            os.remove("/tmp/categories.txt")
        except FileNotFoundError:
            pass
        process = subprocess.Popen("tests/scripts/kill_services.sh")
        process.wait()

    @pytest.mark.asyncio
    @pytest.mark.rabbitmq
    async def test_ok_flow(self):
        historical_decision = HistoricalDecisionStub()
        await historical_decision.start()
        request_alert = load_alert("notebooks/sample/wm_address_in_payload_format.json")
        self.stub.RunEtlLearning(RunEtlLearningRequest(learning_alerts=[request_alert]))
        await historical_decision.get_message()
        assert not historical_decision.result.result
        await asyncio.sleep(1)
        assert historical_decision.result.result
        await historical_decision.conn.stop()

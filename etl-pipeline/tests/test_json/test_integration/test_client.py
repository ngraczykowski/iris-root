import json
import os
import re
import subprocess
import time
from copy import deepcopy

import grpc
import pytest
from aiounittest import AsyncTestCase

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import (
    FAILURE,
    SUCCESS,
    Alert,
    Match,
    RunEtlRequest,
)
from etl_pipeline.service.proto.api.etl_pipeline_pb2_grpc import EtlPipelineServiceStub


def load_alert(filepath: str = "notebooks/sample/wm_address_in_payload_format.json"):
    with open(filepath, "r") as f:
        text = json.load(f)
        matches = [
            Match(match_id="0", match_name=f"alerts/2/matches/{num}")
            for num, _ in enumerate(
                set(
                    [
                        re.findall(r"matchRecords\[\d+\]", key)[0]
                        for key in text
                        if re.search(r"matchRecords\[\d+\]", key)
                    ]
                )
            )
        ]
        alert = Alert(batch_id="1", alert_name="alerts/2", matches=matches)
        for key, value in text.items():
            alert.flat_payload[str(key)] = str(value)
    return alert


class BaseGrpcTestCase:
    @pytest.mark.asyncio
    class TestGrpcServer(AsyncTestCase):
        TIMEOUT = 3

        @classmethod
        def tearDownClass(cls):
            try:
                os.remove("tests/categories.txt")
            except FileNotFoundError:
                pass
            process = subprocess.Popen("tests/scripts/kill_services.sh")
            process.wait()

        @pytest.mark.asyncio
        async def test_ok_flow(self):
            alert = load_alert()
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))
            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        async def test_failures_flow(self):
            alert = load_alert()
            alerts = [deepcopy(alert) for _ in range(10)]

            alerts[0].matches[0].match_name = "test"
            alerts[9].matches[0].match_name = "test"
            alerts[8].flat_payload["alertedParty.test"] = "test"
            counter = 0
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=alerts))
            assert len(alerts) == len(response.etl_alerts)
            for alert in response.etl_alerts:
                try:
                    assert alert.etl_status == SUCCESS
                except AssertionError:
                    assert alert.etl_status == FAILURE
                    counter += 1
            assert counter == 2

        @pytest.mark.asyncio
        def test_cross_input_match_records(self):
            alert = load_alert(
                "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))

            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_empty_supplemental_info(self):
            alert = load_alert("notebooks/sample/wm_party_payload_without_supplemental_info.json")
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))

            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_partial_supplemental_info(self):
            alert = load_alert(
                "notebooks/sample/wm_party_payload_with_partial_supplemental_info.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))

            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_wm_party(self):
            alert = load_alert("notebooks/sample/wm_party_in_payload_format.json")
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))
            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_wrong_alert_name(self):
            alert = load_alert()
            alert.matches[0].match_name = "2"
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))
            assert response.etl_alerts[0].etl_status == FAILURE

        @pytest.mark.asyncio
        def test_empty_flow(self):
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[]))
            assert [i for i in response.etl_alerts] == []


class TestGrpcServerWithoutSSL(BaseGrpcTestCase.TestGrpcServer):

    stub = None

    @classmethod
    def setUpClass(cls):
        cls.tearDownClass()
        environment = os.environ.copy()
        service_config = ConsulServiceConfig()
        subprocess.Popen("tests/scripts/start_services.sh", env=environment)
        channel = grpc.insecure_channel(
            f"{service_config.ETL_SERVICE_IP}:{service_config.ETL_SERVICE_PORT}"
        )
        TestGrpcServerWithoutSSL.stub = EtlPipelineServiceStub(channel)
        time.sleep(BaseGrpcTestCase.TestGrpcServer.TIMEOUT)

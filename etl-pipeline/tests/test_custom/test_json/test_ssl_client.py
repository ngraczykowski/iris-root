import json
import os
import re
import subprocess
import time
import unittest

import grpc

from etl_pipeline.config import service_config
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


class TestGrpcServer(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        environment = os.environ.copy()
        subprocess.Popen("scripts/start_services_ssl.sh", env=environment)
        with open(service_config.GRPC_CLIENT_TLS_CA, "rb") as f:
            ca = f.read()
        with open(service_config.GRPC_CLIENT_TLS_PRIVATE_KEY, "rb") as f:
            private_key = f.read()
        with open(service_config.GRPC_CLIENT_TLS_PUBLIC_KEY_CHAIN, "rb") as f:
            certificate_chain = f.read()
        server_credentials = grpc.ssl_channel_credentials(ca, private_key, certificate_chain)
        channel = grpc.secure_channel("localhost:9090", server_credentials)
        TestGrpcServer.stub = EtlPipelineServiceStub(channel)
        time.sleep(1)

    @classmethod
    def tearDownClass(cls):
        process = subprocess.Popen("scripts/kill_services.sh")
        process.wait()
        pass

    def test_ok_flow(self):
        alert = load_alert()

        response = TestGrpcServer.stub.RunEtl(RunEtlRequest(alerts=[alert]))
        for alert in response.etl_alerts:
            assert alert.etl_status == SUCCESS

    def test_cross_input_match_records(self):
        alert = load_alert(
            "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json"
        )
        response = TestGrpcServer.stub.RunEtl(RunEtlRequest(alerts=[alert]))

        for alert in response.etl_alerts:
            assert alert.etl_status == SUCCESS

    def test_wm_party(self):
        alert = load_alert("notebooks/sample/wm_party_in_payload_format.json")
        response = TestGrpcServer.stub.RunEtl(RunEtlRequest(alerts=[alert]))
        for alert in response.etl_alerts:
            assert alert.etl_status == SUCCESS

    def test_wrong_alert_name(self):
        alert = load_alert()
        alert.matches[0].match_name = "2"
        response = TestGrpcServer.stub.RunEtl(RunEtlRequest(alerts=[alert]))
        assert response.etl_alerts[0].etl_status == FAILURE

    def test_empty_flow(self):

        response = TestGrpcServer.stub.RunEtl(RunEtlRequest(alerts=[]))
        assert [i for i in response.etl_alerts] == []

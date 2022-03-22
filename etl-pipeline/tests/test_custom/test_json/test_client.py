import json
import os
import subprocess
import time
import unittest

import grpc

from etl_pipeline.service.proto.etl_pipeline_pb2 import (
    FAILURE,
    SUCCESS,
    Alert,
    Match,
    RunEtlRequest,
)
from etl_pipeline.service.proto.etl_pipeline_pb2_grpc import EtlPipelineServiceStub


def load_alert():
    with open("notebooks/sample/wm_address_in_payload_format.json", "r") as f:
        text = json.load(f)
        match1 = Match(match_id="0", match_name="mathes/0")
        match2 = Match(match_id="1", match_name="matches/1")
        alert = Alert(batch_id="1", alert_name="alerts/2", matches=[match1, match2])
        for key, value in text.items():
            alert.flat_payload[str(key)] = str(value)
    return alert


class TestGrpcServer(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        environment = os.environ.copy()
        subprocess.Popen("scripts/start_services.sh", env=environment)
        time.sleep(1)

    @classmethod
    def tearDownClass(cls):
        process = subprocess.Popen("scripts/kill_services.sh")
        process.wait()

    def test_ok_flow(self):
        alert = load_alert()
        channel = grpc.insecure_channel("localhost:9090")
        stub = EtlPipelineServiceStub(channel)
        response = stub.RunEtl(RunEtlRequest(alerts=[alert]))
        for alert in response.etl_alerts:
            assert alert.etl_status == SUCCESS

    def test_wrong_alert_name(self):
        alert = load_alert()
        channel = grpc.insecure_channel("localhost:9090")
        alert.alert_name = "2"
        stub = EtlPipelineServiceStub(channel)
        response = stub.RunEtl(RunEtlRequest(alerts=[alert]))
        assert response.etl_alerts[0].etl_status == FAILURE

    def test_empty_flow(self):

        channel = grpc.insecure_channel("localhost:9090")
        stub = EtlPipelineServiceStub(channel)
        response = stub.RunEtl(RunEtlRequest(alerts=[]))
        assert [i for i in response.etl_alerts] == []

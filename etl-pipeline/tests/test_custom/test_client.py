import json

import grpc
import pytest

from etl_pipeline.service.proto.etl_pipeline_pb2 import Alert, Match, RunEtlRequest
from etl_pipeline.service.proto.etl_pipeline_pb2_grpc import EtlPipelineServiceStub


def load_alert():
    with open("API/xml_alignment_payload.json", "r") as f:
        text = json.load(f)
        match1 = Match(match_id="0", match_name="1")
        match2 = Match(match_id="1", match_name="2")
        alert = Alert(batch_id="1", alert_name="2", matches=[match1, match2])
        for key, value in text.items():
            alert.flat_payload[str(key)] = str(value)
    return alert


@pytest.mark.skip
def test_flow():
    alert = load_alert()
    channel = grpc.insecure_channel("localhost:50051")
    stub = EtlPipelineServiceStub(channel)
    response = stub.RunEtl(RunEtlRequest(alerts=[alert]))
    assert response.etl_alerts[0].etl_status == 0

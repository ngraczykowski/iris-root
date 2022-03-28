import json

import grpc

from etl_pipeline.service.proto.api.etl_pipeline_pb2 import SUCCESS, Alert, Match, RunEtlRequest
from etl_pipeline.service.proto.api.etl_pipeline_pb2_grpc import EtlPipelineServiceStub


def load_alert():
    with open("notebooks/sample/wm_address_in_payload_format.json", "r") as f:
        text = json.load(f)
        match1 = Match(match_id="0", match_name="alerts/1/matches/0")
        match2 = Match(match_id="1", match_name="alerts/1/matches/1")
        alert = Alert(batch_id="1", alert_name="alerts/2", matches=[match1, match2])
        for key, value in text.items():
            alert.flat_payload[str(key)] = str(value)
    return alert


alert = load_alert()
channel = grpc.insecure_channel("localhost:9090")
stub = EtlPipelineServiceStub(channel)
response = stub.RunEtl(RunEtlRequest(alerts=[alert]))
assert response.etl_alerts[0].etl_status == SUCCESS

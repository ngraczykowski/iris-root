import json
import pickle
import unittest

from spark_manager.spark_client import SparkClient
from spark_manager.spark_config import SPARK_CONF

from config import columns_namespace, pipeline_config
from custom.ms.datatypes.field import InputRecordField  # noqa F401
from etl_component.proto.etl_pipeline_pb2 import Alert, Match
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from pipelines.ms.json_pipeline import MSPipeline

ALERT_INTERNAL_ID = columns_namespace.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"
spark_instance = SparkClient(SPARK_CONF)


def load_alert():

    with open("API/flat_response.json", "r") as f:
        text = json.load(f)
        match1 = Match(match_id="1", match_name="1")
        match2 = Match(match_id="2", match_name="2")
        alert = Alert(batch_id="1", alert_name="2", matches=[match1, match2])
        for key, value in text.items():
            alert.flat_payload[str(key)] = str(value)
    return alert


class TestMSPipeline(unittest.TestCase):
    @classmethod
    def setUpClass(self):
        self.spark_engine = JsonProcessingEngine(pipeline_config)
        self.uut = MSPipeline(self.spark_engine, config=pipeline_config)

    def test_pipeline(self):
        payload = load_alert()
        payload = payload.flat_payload
        payload = self.uut.transform_standardized_to_cleansed(payload)
        payload = self.uut.transform_cleansed_to_application(payload)

        with open("tests/shared/parsed_payload.pkl", "rb") as f:
            reference_payload = pickle.load(f)
        for num in range(len(payload["matchesPayloads"])):
            for key in payload["matchesPayloads"][num]:
                try:
                    assert (
                        payload["matchesPayloads"][num][key]
                        == reference_payload["matchesPayloads"][num][key]
                    )
                except AssertionError:
                    assert sorted(payload["matchesPayloads"][num][key]) == sorted(
                        reference_payload["matchesPayloads"][num][key]
                    )

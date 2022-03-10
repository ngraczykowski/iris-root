import json
import pickle
import unittest

from spark_manager.spark_client import SparkClient
from spark_manager.spark_config import SPARK_CONF

from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.datatypes.field import InputRecordField  # noqa F401
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from etl_pipeline.service.proto.etl_pipeline_pb2 import Alert, Match
from pipelines.ms.new_json_pipeline import MSPipeline

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"
spark_instance = SparkClient(SPARK_CONF)


def load_alert():

    with open("notebooks/sample/alert.json", "r") as f:
        text = json.load(f)
        match1 = Match(match_id="0", match_name="1")
        match2 = Match(match_id="1", match_name="2")
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
        with open("notebooks/sample/alert.json", "r") as file:
            payload = json.loads(file.read())

        payload_json = {key: payload[key] for key in sorted(payload)}
        # payload_json = PayloadLoader().load_payload_from_json(payload_json)
        payload_json["match_ids"] = [
            i for i in range(len(payload_json[cn.ALERT_FIELD][cn.MATCH_RECORDS]))
        ]
        payload = payload_json
        payload = self.uut.transform_standardized_to_cleansed(payload)
        payload = self.uut.transform_cleansed_to_application(payload)
        with open("tests/shared/parsed_payload.pkl", "rb") as f:
            reference_payload = pickle.load(f)
        for num in range(len(payload[cn.ALERT_FIELD][cn.MATCH_RECORDS])):
            for key in payload[cn.ALERT_FIELD][cn.MATCH_RECORDS][num]:
                try:
                    assert (
                        payload[cn.ALERT_FIELD][cn.MATCH_RECORDS][num][key]
                        == reference_payload[cn.ALERT_FIELD][cn.MATCH_RECORDS][num][key]
                    )
                except AssertionError:
                    assert sorted(payload[cn.ALERT_FIELD][cn.MATCH_RECORDS][num][key]) == sorted(
                        reference_payload[cn.ALERT_FIELD][cn.MATCH_RECORDS][num][key]
                    )

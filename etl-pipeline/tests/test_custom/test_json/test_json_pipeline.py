import json
import pickle
import unittest

from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.datatypes.field import InputRecordField  # noqa F401
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from pipelines.ms.wm_address_pipeline import MSPipeline

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"


class TestMSPipeline(unittest.TestCase):
    @classmethod
    def setUpClass(self):
        self.spark_engine = JsonProcessingEngine(pipeline_config)
        self.uut = MSPipeline(self.spark_engine, config=pipeline_config)

    def test_pipeline(self):
        with open("notebooks/sample/wm_address_in_payload_format.json", "r") as file:
            payload = json.loads(file.read())
        payload_json = {key: payload[key] for key in sorted(payload)}
        payload_json = PayloadLoader().load_payload_from_json(payload_json)
        payload_json["match_ids"] = [
            i for i in range(len(payload_json["watchlistParty"][cn.MATCH_RECORDS]))
        ]
        payload = payload_json
        parsed_payloads = self.uut.transform_standardized_to_cleansed(payload)
        parsed_payloads = self.uut.transform_cleansed_to_application(parsed_payloads)
        with open("tests/shared/parsed_payload.pkl", "rb") as f:
            reference_payloads = pickle.load(f)

        assert len(parsed_payloads) == 2
        for i in parsed_payloads:
            assert len(i["alertedParty"]["inputRecordHist"]) == 1
            assert len(i["watchlistParty"]["matchRecords"]) == 1

        for payload, reference_payload in zip(parsed_payloads, reference_payloads):
            for num in range(len(payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS])):
                for key in payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num]:
                    try:
                        assert (
                            payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                            == reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        )
                    except AssertionError:
                        assert sorted(
                            payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        ) == sorted(
                            reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        )

    def test_pipeline_with_cross_payloads(self):
        with open(
            "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json", "r"
        ) as file:
            payload = json.loads(file.read())
        payload_json = {key: payload[key] for key in sorted(payload)}
        payload_json = PayloadLoader().load_payload_from_json(payload_json)
        payload_json["match_ids"] = [
            i for i in range(len(payload_json["watchlistParty"][cn.MATCH_RECORDS]))
        ]
        payload = payload_json
        parsed_payloads = self.uut.transform_standardized_to_cleansed(payload)
        parsed_payloads = self.uut.transform_cleansed_to_application(parsed_payloads)
        with open("tests/shared/parsed_payload_2_payload.pkl", "rb") as f:
            reference_payloads = pickle.load(f)

        assert len(parsed_payloads) == 3
        for i in parsed_payloads:
            assert len(i["alertedParty"]["inputRecordHist"]) == 1
            assert len(i["watchlistParty"]["matchRecords"]) == 1

        for payload, reference_payload in zip(parsed_payloads, reference_payloads):
            for num in range(len(payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS])):
                for key in payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num]:
                    try:
                        assert (
                            payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                            == reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        )
                    except AssertionError:
                        assert sorted(
                            payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        ) == sorted(
                            reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        )

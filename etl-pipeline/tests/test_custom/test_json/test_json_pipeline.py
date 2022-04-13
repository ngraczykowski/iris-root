import json
import pickle

import pytest

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.datatypes.field import InputRecordField  # noqa F401
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from pipelines.ms.ms_pipeline import MSPipeline

cn = pipeline_config.cn

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"


def run_pipeline(uut, file_path, reference_file_path):
    with open(file_path, "r") as file:
        payload = json.loads(file.read())
    payload_json = {key: payload[key] for key in sorted(payload)}
    payload_json = PayloadLoader().load_payload_from_json(payload_json)
    payload_json["match_ids"] = [
        i for i in range(len(payload_json[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]))
    ]
    payload = payload_json
    parsed_payloads = uut.transform_standardized_to_cleansed(payload)
    parsed_payloads = uut.transform_cleansed_to_application(parsed_payloads)
    with open(reference_file_path, "rb") as f:
        reference_payloads = pickle.load(f)
    return parsed_payloads, reference_payloads


def check_payload(parsed_payloads, reference_payloads):
    for payload, reference_payload in zip(parsed_payloads, reference_payloads):
        for num in range(len(payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS])):
            for key in payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num]:
                try:
                    assert (
                        payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        == reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                    )
                except AssertionError:
                    try:
                        assert sorted(
                            payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        ) == sorted(
                            reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                        )
                    except:
                        if isinstance(
                            payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key], dict
                        ) and isinstance(
                            reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key],
                            dict,
                        ):
                            reference = reference_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][
                                num
                            ][key]
                            tested = payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS][num][key]
                            for new_key in tested:
                                assert tested[new_key] == reference[new_key]


def assert_length_and_content_match(requested_length, parsed_payloads, reference_payloads):
    assert len(parsed_payloads) == requested_length
    for i in parsed_payloads:
        assert len(i[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST]) == 1
        assert len(i[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]) == 1
    check_payload(parsed_payloads, reference_payloads)


@pytest.mark.parametrize(
    ["payload_file", "reference_file", "reference_length"],
    [
        (
            "notebooks/sample/wm_address_in_payload_format.json",
            "tests/shared/parsed_payload.pkl",
            2,
        ),
        (
            "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json",
            "tests/shared/parsed_payload_2_payload.pkl",
            3,
        ),
        (
            "notebooks/sample/wm_party_payload_without_supplemental_info.json",
            "tests/shared/empty_payload.pkl",
            2,
        ),
        (
            "notebooks/sample/wm_party_payload_with_partial_supplemental_info.json",
            "tests/shared/empty_payload.pkl",
            2,
        ),
    ],
)
def test_pipeline(payload_file, reference_file, reference_length):
    json_engine = JsonProcessingEngine(pipeline_config)
    uut = MSPipeline(json_engine, config=pipeline_config)
    parsed_payloads, reference_payloads = run_pipeline(uut, payload_file, reference_file)
    assert_length_and_content_match(reference_length, parsed_payloads, reference_payloads)

import json
import pickle

import pandas as pd
import pytest

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.datatypes.field import (  # noqa F401; required for unpickling
    InputRecordField,
)
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from pipelines.ms.ms_pipeline import MSPipeline

cn = pipeline_config.cn

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"


def run_pipeline(uut, file_path):
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
    return parsed_payloads


def check_no_dict_type(ref, tested):
    if type(ref) != type(tested):
        raise AssertionError("Wrong Type")
    if isinstance(ref, str):
        ref = ref.strip()
        tested = tested.strip()
    assert tested == ref


def flatten(value):
    if value == []:
        return value
    if isinstance(value, list):
        if isinstance(value[0], list):
            return flatten(value[0]) + flatten(value[1:])
        return value[:1] + flatten(value[1:])
    return value


def remove_nulls_from_aggegated(match):
    return [i for i in match if i]


def assert_list(tested, reference):
    try:
        assert sorted(tested) == sorted(reference)
    except TypeError:
        assert_compare_list_of_dict_of_list(tested, reference)


def assert_compare_list_of_dict_of_list(tested, reference):
    assert len(tested) == len(reference)
    for tested_element, reference_element in zip(tested, reference):
        for key in tested_element:
            assert sorted(tested_element[key]) == sorted(reference_element[key])


def check_payload(out_payload, reference_file):
    out_payload = pd.DataFrame(
        [match for payload in out_payload for match in payload["watchlistParty"]["matchRecords"]]
    )
    with open(reference_file, "rb") as f:
        reference_payloads = pickle.load(f)

    reference_payload = pd.DataFrame(
        [
            match
            for payload in reference_payloads
            for match in payload["watchlistParty"]["matchRecords"]
        ]
    )
    for cols in out_payload.columns:
        try:
            pd.testing.assert_series_equal(out_payload[cols], reference_payload[cols])
        except (AssertionError, TypeError):
            if isinstance(out_payload[cols].values[0], list):
                reference = remove_nulls_from_aggegated(
                    flatten([i for i in reference_payload[cols].values])
                )
                output = remove_nulls_from_aggegated(
                    flatten([i for i in out_payload[cols].values])
                )
                assert_list(output, reference)

            else:
                assert output == reference


def assert_length_and_content_match(requested_length, parsed_payloads, reference_file):
    assert len(parsed_payloads) == requested_length
    for i in parsed_payloads:
        assert len(i[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST]) == 1
        assert len(i[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]) == 1
    check_payload(parsed_payloads, reference_file)


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
    parsed_payloads = run_pipeline(uut, payload_file)
    assert_length_and_content_match(reference_length, parsed_payloads, reference_file)

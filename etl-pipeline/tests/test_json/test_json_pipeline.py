import json
import pickle
from copy import deepcopy

import pandas as pd
import pytest

from etl_pipeline.config import DatasetConfigError, pipeline_config
from etl_pipeline.custom.ms.datatypes.field import (  # noqa F401; required for unpickling
    InputRecordField,
)
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from pipelines.ms.ms_pipeline import MSPipeline
from tests.test_json.constant import EXAMPLE_PARTIES

cn = pipeline_config.cn

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"


def get_dummy_payload_for_dataset(dataset_name):
    return {cn.ALERTED_PARTY_FIELD: {cn.HEADER_INFO: {cn.DATASET_NAME: dataset_name}}}


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


def assert_list(tested, reference, key):
    try:
        assert set(sorted(tested)) == set(sorted(reference)), key
    except TypeError:
        assert_compare_list_of_dict_of_list(tested, reference, key)


def assert_compare_list_of_dict_of_list(tested, reference, col):
    assert len(tested) == len(reference)

    for tested_element, reference_element in zip(tested, reference):
        for key in tested_element:
            assert sorted(tested_element[key]) == sorted(reference_element[key]), col


def load_payload(out_payload, reference_file):
    with open(reference_file, "rb") as f:
        reference_payloads = pickle.load(f)
    alerted_payload = pd.DataFrame([payload["alertedParty"] for payload in out_payload])
    wp_payload = pd.DataFrame([payload["alertedParty"] for payload in reference_payloads])
    check_payload(alerted_payload, wp_payload)
    alerted_payload = pd.DataFrame(
        [payload["watchlistParty"]["matchRecords"] for payload in out_payload]
    )
    wp_payload = pd.DataFrame(
        [payload["watchlistParty"]["matchRecords"] for payload in reference_payloads]
    )
    check_payload(alerted_payload, wp_payload)
    alerted_payload = pd.DataFrame(
        [
            {match: payload[match] for match in payload if not isinstance(payload[match], dict)}
            for payload in out_payload
        ]
    )
    wp_payload = pd.DataFrame(
        [
            {match: payload[match] for match in payload if not isinstance(payload[match], dict)}
            for payload in reference_payloads
        ]
    )
    check_payload(alerted_payload, wp_payload)


def check_payload(out_payload, reference_payload):
    for cols in out_payload.columns:
        try:
            pd.testing.assert_series_equal(out_payload[cols], reference_payload[cols])
        except (AssertionError, TypeError):
            reference = remove_nulls_from_aggegated(
                flatten([i for i in reference_payload[cols].values])
            )
            output = remove_nulls_from_aggegated(flatten([i for i in out_payload[cols].values]))
            assert_list(output, reference, cols)


def assert_length_and_content_match(
    requested_length, parsed_payloads, reference_file, ap_id_tp_marked_agent_input
):
    assert len(parsed_payloads) == requested_length
    for i in parsed_payloads:
        assert len(i[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST]) == 1
        assert (
            i[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]["ap_id_tp_marked_agent_input"]
            == ap_id_tp_marked_agent_input
        )
    load_payload(parsed_payloads, reference_file)


@pytest.mark.parametrize(
    ["payload_file", "reference_file", "reference_length", "ap_id_tp_marked_agent_input"],
    [
        (
            "notebooks/sample/wm_address_in_payload_format.json",
            "tests/shared/parsed_payload.pkl",
            2,
            "A05003324172",
        ),
        (
            "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json",
            "tests/shared/parsed_payload_2_payload.pkl",
            3,
            "A05003324172",
        ),
        (
            "notebooks/sample/wm_party_payload_without_supplemental_info.json",
            "tests/shared/empty_payload.pkl",
            2,
            "A05003324172",
        ),
        (
            "notebooks/sample/wm_party_payload_with_partial_supplemental_info.json",
            "tests/shared/wm_party_payload_with_partial_supplemental_info.pkl",
            2,
            "A05003324172",
        ),
        (
            "notebooks/sample/big_fat_flat_payload.json",
            "tests/shared/big_fat_payload_parsed.pkl",
            9,
            "A00183210139",
        ),
    ],
)
def test_pipeline(payload_file, reference_file, reference_length, ap_id_tp_marked_agent_input):
    uut = MSPipeline.build_pipeline(MSPipeline)
    parsed_payloads = run_pipeline(uut, payload_file)
    assert_length_and_content_match(
        reference_length, parsed_payloads, reference_file, ap_id_tp_marked_agent_input
    )


@pytest.fixture(scope="module")
def pipeline_resource(request):
    uut = MSPipeline.build_pipeline(MSPipeline)
    yield uut


@pytest.mark.parametrize(
    ["match", "expected_result"],
    [
        (
            {
                cn.DATASET_TYPE: "ISG_PARTY",
                "ap_id_tp_marked_agent_input": [
                    "DUMMY_ISG_PARTY_ID",
                    "DUMMY_ADDRESS_PARTY_ID",
                    "DUMMY_ISG_ACCOUNT_ID",
                ],
            },
            "DUMMY_ISG_PARTY_ID",
        ),
        (
            {
                cn.DATASET_TYPE: "WM_ADDRESS",
                "ap_id_tp_marked_agent_input": [
                    "DUMMY_ISG_PARTY_ID",
                    "DUMMY_ADDRESS_PARTY_ID",
                    "DUMMY_ISG_ACCOUNT_ID",
                ],
            },
            "DUMMY_ADDRESS_PARTY_ID",
        ),
        (
            {
                cn.DATASET_TYPE: "WM_PARTY",
                "ap_id_tp_marked_agent_input": [
                    "DUMMY_ISG_PARTY_ID",
                    "DUMMY_ADDRESS_PARTY_ID",
                    "DUMMY_ISG_ACCOUNT_ID",
                ],
            },
            "DUMMY_ADDRESS_PARTY_ID",
        ),
        (
            {
                cn.DATASET_TYPE: "ISG_ACCOUNT",
                "ap_id_tp_marked_agent_input": [
                    "DUMMY_ISG_PARTY_ID",
                    "DUMMY_ADDRESS_PARTY_ID",
                    "DUMMY_ISG_ACCOUNT_ID",
                ],
            },
            "DUMMY_ISG_ACCOUNT_ID",
        ),
        (
            {
                cn.DATASET_TYPE: "UNKNOWN",
                "ap_id_tp_marked_agent_input": [
                    "DUMMY_ISG_PARTY_ID",
                    "DUMMY_ADDRESS_PARTY_ID",
                    "DUMMY_ISG_ACCOUNT_ID",
                ],
            },
            "",
        ),
    ],
)
def test_select_ap_for_ap_id_tp_marked_agent(match, expected_result, pipeline_resource):
    pipeline_resource.select_ap_for_ap_id_tp_marked_agent(match)
    assert match["ap_id_tp_marked_agent_input"] == expected_result


@pytest.mark.parametrize(
    ["payload", "expected_result", "is_data_config_error_expected"],
    [
        (get_dummy_payload_for_dataset("DUMMY COLUMN"), None, True),
        (get_dummy_payload_for_dataset("R_Global_MultiParty_Daily"), "ISG_PARTY", False),
        (get_dummy_payload_for_dataset("R_GLOBAL_Us_Active_Address"), "WM_ADDRESS", False),
        (get_dummy_payload_for_dataset("R_US_Active_Party"), "WM_PARTY", False),
        (get_dummy_payload_for_dataset("R_Global_MultiParty_Wkly"), "ISG_PARTY", False),
        (get_dummy_payload_for_dataset("R_Global_MultiAccounts_Daily_HK"), "ISG_ACCOUNT", False),
        (get_dummy_payload_for_dataset("R_Global_MultiAccounts_Daily"), "ISG_ACCOUNT", False),
        (
            get_dummy_payload_for_dataset("R_Global_MultiAccounts_Daily_Chinese"),
            "ISG_ACCOUNT",
            False,
        ),
        (get_dummy_payload_for_dataset("R_Global_MultiAccounts_Wkly"), "ISG_ACCOUNT", False),
        (
            get_dummy_payload_for_dataset("R_Global_MultiAccounts_Wkly_Chinese"),
            "ISG_ACCOUNT",
            False,
        ),
        (
            get_dummy_payload_for_dataset("R_Global_MultiAccounts_Wkly_EDDCBI"),
            "ISG_ACCOUNT",
            False,
        ),
    ],
)
def test_set_up_dataset_type_match(
    payload, expected_result, is_data_config_error_expected, pipeline_resource
):
    match = {}
    if is_data_config_error_expected:
        with pytest.raises(DatasetConfigError):
            pipeline_resource.set_up_dataset_type_match(payload, match)
    else:
        pipeline_resource.set_up_dataset_type_match(payload, match)
        assert match[cn.DATASET_TYPE] == expected_result


def test_collect_party_values_from_parties(pipeline_resource):
    parties = deepcopy(EXAMPLE_PARTIES)
    payload = {
        cn.ALERTED_PARTY_FIELD: {cn.SUPPLEMENTAL_INFO: {cn.RELATED_PARTIES: {cn.PARTIES: parties}}}
    }
    pipeline_resource.collect_party_values(payload)
    assert {key: value for key, value in payload.items() if key.startswith("ALL")} == {
        "ALL_CONNECTED_PARTIES_NAMES": [],
        "ALL_CONNECTED_GOVT_IDS": [],
        "ALL_CONNECTED_PARTY_NAMES": ["Shaolin kung fu master", "John, Doe Doe"],
        "ALL_CONNECTED_TAX_IDS": ["1231413412312", "12097381208937"],
        "ALL_CONNECTED_PARTY_BIRTH_COUNTRIES": ["1341412312312", "13413401280"],
        "ALL_CONNECTED_PARTY_CITIZENSHIP_COUNTRIES": ["Arabian Emirates"],
        "ALL_CONNECTED_PARTY_RESIDENCY_COUNTRIES": [],
        "ALL_CONNECTED_COUNTRY_OF_INCORPORATION": ["Arabian Emirates"],
    }

    assert payload["alertedParty"]["ALL_PARTY_TYPES"] == []
    assert payload["alertedParty"]["ALL_PARTY_DOBS"] == ["10/10/1969"]

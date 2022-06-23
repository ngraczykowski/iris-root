import json
from copy import deepcopy
from dataclasses import asdict

import pytest

from etl_pipeline.config import DatasetConfigError, pipeline_config
from etl_pipeline.custom.ms.datatypes.field import (  # noqa F401; required for unpickling
    InputRecordField,
)
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.service.servicer import Match
from pipelines.ms.ms_pipeline import MSPipeline
from tests.test_json.constant import (
    EXAMPLE_FOR_TEST_SET_REF_KEY,
    EXAMPLE_PARTIES,
    EXAMPLE_PARTIES_WITH_NAMES,
    RESULT_FOR_EXAMPLE_FOR_TEST_SET_REF_KEY,
)

cn = pipeline_config.cn

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"


def get_dummy_payload_for_dataset(dataset_name):
    return {cn.ALERTED_PARTY_FIELD: {cn.HEADER_INFO: {cn.DATASET_NAME: dataset_name}}}


def sort_list(dict_):
    if isinstance(dict_, dict):
        for key in dict_:
            dict_[key] = sort_list(dict_[key])
    if isinstance(dict_, list):
        sorted_ = [sort_list(i) for i in dict_]
        try:
            return sorted(sorted_)
        except TypeError:
            return sorted_
    return dict_


def run_pipeline(uut, file_path):
    with open(file_path, "r") as file:
        payload = json.loads(file.read())
    payload_json = {key: payload[key] for key in sorted(payload)}
    payload_json = PayloadLoader().load_payload_from_json(payload_json)

    payload_json["match_ids"] = [
        Match(match_id=match["matchId"], match_name="dummy")
        for match in payload_json[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
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


def remove_nulls_from_aggregated(match):
    return [i for i in match if i]


def assert_list(tested, reference, key):
    try:
        assert set(sorted(tested)) == set(sorted(reference)), key
    except TypeError:
        assert_compare_list_of_dict_of_list(tested, reference, key)


def assert_compare_list_of_dict_of_list(tested, reference, col=None):
    assert len(tested) == len(reference)

    for tested_element, reference_element in zip(tested, reference):
        assert_nested_dict(tested_element, reference_element, col)


def assert_nested_dict(tested, reference, key=None):

    if isinstance(reference, dict):
        assert len(tested) == len(reference)
        if isinstance(tested, Match):
            assert {"match_id": tested.match_id, "match_name": tested.match_name} == reference
            return
        try:
            assert tested == reference
        except:
            for key in reference:
                assert_nested_dict(tested[key], reference[key], key)
        return
    elif isinstance(reference, list):
        try:
            assert sorted(tested) == sorted(reference)
        except (TypeError, AssertionError):
            assert_compare_list_of_dict_of_list(tested, reference, key)
        return

    assert tested == reference, key


def load_payload(out_payload, reference_file):
    if reference_file.endswith(".json"):
        with open(reference_file, "r") as f:
            payload = json.load(f)

        for i in out_payload:
            try:
                i["alertedParty"]["inputRecordHist"]["inputRecords"]["INPUT_FIELD"] = {
                    j: asdict(
                        i["alertedParty"]["inputRecordHist"]["inputRecords"]["INPUT_FIELD"][j]
                    )
                    for j in sorted(
                        i["alertedParty"]["inputRecordHist"]["inputRecords"]["INPUT_FIELD"]
                    )
                }
            except TypeError:
                pass
        for match in out_payload:
            match["match_ids"] = {
                "match_id": match["match_ids"].match_id,
                "match_name": match["match_ids"].match_name,
            }
        assert_nested_dict(sort_list(out_payload), payload)
        return


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
            "tests/shared/parsed_payload.json",
            2,
            "A05003324172",
        ),
        (
            "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json",
            "tests/shared/parsed_payload_2_payload.json",
            3,
            "A05003324172",
        ),
        (
            "notebooks/sample/wm_party_payload_without_supplemental_info.json",
            "tests/shared/empty_payload.json",
            2,
            "A05003324172",
        ),
        (
            "notebooks/sample/wm_party_payload_with_partial_supplemental_info.json",
            "tests/shared/wm_party_payload_with_partial_supplemental_info.json",
            2,
            "A05003324172",
        ),
        (
            "notebooks/sample/big_fat_flat_payload.json",
            "tests/shared/big_fat_payload_parsed.json",
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
    assert {key: value for key, value in payload.items() if key.startswith("AP")} == {
        "AP_PARTIES_NAMES": [],
        "AP_PARTY_NAMES": ["Shaolin kung fu master", "John, Doe Doe"],
        "AP_TAX_IDS": ["1231413412312", "12097381208937"],
        "AP_PARTY_BIRTH_COUNTRIES": ["1341412312312", "13413401280"],
        "AP_PARTY_CITIZENSHIP_COUNTRIES": ["Arabian Emirates"],
        "AP_PARTY_RESIDENCY_COUNTRIES": [],
        "AP_COUNTRY_OF_INCORPORATION": ["Arabian Emirates"],
        "AP_GOVT_IDS": [],
        "AP_ACCOUNT_NAMES": [],
        "AP_ACCOUNT_BRANCH_ACCOUNT_NUMBERS": [],
        "AP_ACCOUNT_BENEFICIARY_NAMES": [],
        "AP_PARTY1_COUNTRY_OF_INCORPORATION": None,
        "AP_PARTY1_ADDRESS1_COUNTRY": None,
        "AP_PARTY1_COUNTRY1_CITIZENSHIP": None,
        "AP_PARTY1_COUNTRY2_CITIZENSHIP": None,
        "AP_PARTY1_COUNTRY_FORMATION1": None,
        "AP_PARTY1_COUNTRY_DOMICILE1": None,
        "AP_PRTY_PRIM_CTZNSH_CNTRY": None,
        "AP_PRTY_RSDNC_CNTRY_CD": None,
        "AP_PARTY1_COUNTRY_PEP": None,
        "AP_CONCAT_NAMES": None,
    }

    assert payload["alertedParty"]["AP_DOB"] == ["10/10/1969"]


def test_set_trigger_reasons(pipeline_resource):
    match = EXAMPLE_FOR_TEST_SET_REF_KEY
    pipeline_resource.set_trigger_reasons(match, pipeline_config.config.FUZZINESS_LEVEL)
    assert match[cn.TRIGGERED_BY] == sorted(RESULT_FOR_EXAMPLE_FOR_TEST_SET_REF_KEY)


def test_set_beneficiary_hits(pipeline_resource):
    match = EXAMPLE_FOR_TEST_SET_REF_KEY
    assert match.get(cn.IS_BENEFICIARY_HIT, None) is None
    pipeline_resource.set_beneficiary_hits(match)
    assert not match.get(cn.IS_BENEFICIARY_HIT, None)


def test_connect_full_names(pipeline_resource):
    parties = deepcopy(EXAMPLE_PARTIES)
    pipeline_resource._connect_full_names(parties)
    for party in parties:
        assert party[cn.CONNECTED_FULL_NAME] == ""

    parties = deepcopy(EXAMPLE_PARTIES_WITH_NAMES)
    pipeline_resource._connect_full_names(parties)
    assert parties[0][cn.CONNECTED_FULL_NAME] == "Ultra Giga Pole"
    assert parties[1][cn.CONNECTED_FULL_NAME] == ""


def test_get_clean_names_from_concat_name(pipeline_resource):
    assert pipeline_resource.get_clean_names_from_concat_name(
        "KA LAI JOSEPH CHAN & KAR LUN KAREN LEE LUNKAREN",
        {
            "PRIN_OWN_NM": "KA LAI JOSEPH CHAN",
            "ORD_PLACR_NM": "KA LAI JOSEPH CHAN",
        },
    ) == {
        "PRIN_OWN_NM": "KA LAI JOSEPH CHAN",
        "concat_residue": " & KAR LUN KAREN LEE LUNKAREN",
    }

    assert pipeline_resource.get_clean_names_from_concat_name(
        "MANULIFE INVEST MANAGEMENT (US) LLC - MD SHORT TERM BOND FUND - SPOT ONLY REPATRIATION MANAGEMENT(US) BONDFUND",
        {
            "LAST_NM": "MANULIFE INVEST MANAGEMENT",
            "FRST_NM": "MANULIFE INVEST MANAGEMENT",
            "PRIN_OWN_NM": "MD SHORT TERM BOND FUND",
        },
    ) == {
        "LAST_NM": "MANULIFE INVEST MANAGEMENT",
        "PRIN_OWN_NM": "MD SHORT TERM BOND FUND",
        "concat_residue": " (US) LLC -  - SPOT ONLY REPATRIATION MANAGEMENT(US) BONDFUND",
    }

    assert pipeline_resource.get_clean_names_from_concat_name(
        "VTB CAPITAL PLC A/C JP MORGAN CHASE BANK NA PLCA/C",
        {" ": ["VTB CAPITAL PLC", "Zoria"]},
    ) == {" ": "VTB CAPITAL PLC", "concat_residue": " A/C JP MORGAN CHASE BANK NA PLCA/C"}

    assert pipeline_resource.get_clean_names_from_concat_name(
        "MSSB C/F SALVATORE P TADDEO JR IRA STANDARD DATED 09/11/97 28 WARREN ST RUMSON NJ 07760",
        {"all_party_names": ["Taddeo, Lisa", "Taddeo, Sal"]},
    ) == {
        "concat_residue": "MSSB C/F SALVATORE P TADDEO JR IRA STANDARD DATED 09/11/97 28 WARREN ST RUMSON NJ 07760"
    }

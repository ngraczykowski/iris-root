import pytest

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.service.servicer import Match
from pipelines.ms.functions import Functions
from pipelines.ms.ms_pipeline import MSPipeline
from tests.test_json.constant import TEST_AGENT_INPUT_CASES

cn = pipeline_config.cn


@pytest.fixture(scope="module")
def pipeline_resource(request):
    uut = MSPipeline.build_pipeline(MSPipeline)
    yield uut


@pytest.mark.parametrize(("test_case"), TEST_AGENT_INPUT_CASES)
def test_agent_input(test_case, pipeline_resource):
    input_payload, expected_result = test_case.payload, test_case.expected_result
    payload_json = PayloadLoader().load_payload_from_json(input_payload)
    payload_json["match_ids"] = [
        Match(match_id=match["matchId"], match_name="dummy")
        for match in payload_json[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
    ]
    parsed_payloads = pipeline_resource.transform_standardized_to_cleansed(payload_json)
    parsed_payloads = pipeline_resource.transform_cleansed_to_application(parsed_payloads)
    payload = parsed_payloads[0]
    match = payload.get(cn.WATCHLIST_PARTY, {}).get(cn.MATCH_RECORDS, [])
    for cat, key in expected_result.keys():
        new_config = {}
        Functions.parse_key({cat: [key]}, match, payload, new_config)
        key_short = key.split(".")[-1] if "." in key else key
        assert (
            new_config[key_short] == expected_result[(cat, key)]
        ), f"{new_config[key_short]} != {expected_result[(cat, key)]} for {(cat, key)} in {test_case.agent_name}"

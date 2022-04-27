import pytest

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from pipelines.ms.ms_pipeline import MSPipeline
from tests.test_json.constant import TEST_AGENT_INPUT_CASES

cn = pipeline_config.cn


def parse_key(value, match, payload, new_config):
    temp_dict = dict(value)
    for new_key in temp_dict:
        for element in temp_dict[new_key]:
            elements = element.split(".")
            if cn.MATCH_RECORDS in element:
                value = match
                elements = elements[2:]
            else:

                value = payload
            for field_name in elements:
                if field_name == cn.INPUT_FIELD:
                    try:
                        value = value[0][field_name][elements[-1]].value
                    except (AttributeError, KeyError):
                        value = None
                    break
                try:
                    value = value.get(field_name, None)
                except TypeError:
                    key = PayloadLoader.LIST_ELEMENT_REGEX.sub("", field_name)
                    ix = int(PayloadLoader.LIST_ELEMENT_REGEX.match(field_name).groups(0))
                    value = value[key][ix]
            new_config[elements[-1]] = value


@pytest.fixture(scope="module")
def pipeline_resource(request):
    json_engine = JsonProcessingEngine(pipeline_config)
    uut = MSPipeline(json_engine, config=pipeline_config)
    yield uut


@pytest.mark.parametrize(("test_case"), TEST_AGENT_INPUT_CASES)
def test_agent_input(test_case, pipeline_resource):
    input_payload, expected_result = test_case.payload, test_case.expected_result
    payload_json = PayloadLoader().load_payload_from_json(input_payload)
    payload_json["match_ids"] = [
        i for i in range(len(payload_json[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]))
    ]
    parsed_payloads = pipeline_resource.transform_standardized_to_cleansed(payload_json)
    parsed_payloads = pipeline_resource.transform_cleansed_to_application(parsed_payloads)
    payload = parsed_payloads[0]
    match = payload.get(cn.WATCHLIST_PARTY, {}).get(cn.MATCH_RECORDS, [])[0]
    for cat, key in expected_result.keys():
        new_config = {}
        parse_key({cat: [key]}, match, payload, new_config)
        key_short = key.split(".")[-1] if "." in key else key

        assert (
            new_config[key_short] == expected_result[(cat, key)]
        ), f"{new_config[key_short]} != {expected_result[(cat, key)]} for {(cat, key)} in {test_case.agent_name}"

import pytest
from silenteight.agent.geo.v1.api.common_pb2 import LocationType

from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory
from tests.service_based.wrappers.utils import read_json_resource


@pytest.fixture(scope="module")
def geo_knowledge_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("geo_knowledge")
    return wrapper


@pytest.fixture(scope="module")
def geo_measure_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("geo_measure")
    return wrapper


@pytest.mark.parametrize(
    "text, results_file_name",
    [
        (
            "Greetings from Boston, Massachusetts",
            "boston.json",
        ),
        (
            "Hello World",
            "hello_world.json",
        ),
        (
            "[ORIGINATOR     ] IT36701908273410 AC 121140399 BANK "
            "OF TIANJIN CO LTD NO.15 YOU YI ROAD,HE XI DISTRICT",
            "bank_of_tianjin.json",
        ),
    ],
)
def test_geo_knowledge(text, results_file_name, geo_knowledge_wrapper):
    call_results = geo_knowledge_wrapper.knowledge_call(text)
    expected_results = read_json_resource(results_file_name)
    assert len(call_results) == len(expected_results)
    call_results_sorted = sorted(call_results, key=lambda item: item.results.name)

    for result, expected in zip(call_results_sorted, expected_results):
        assert result.original_input == expected.pop("original_input")

        for key, value in expected.items():
            result_attribute_value = getattr(result.results, key)
            if key == "type":  # caused by proto enum parsing to int
                assert LocationType.Name(result_attribute_value) == value
            else:
                assert result_attribute_value == value


@pytest.mark.parametrize(
    "ap_name, wl_name, expected_recommend, expected_eval",
    [
        (
            "New York, USA",
            "NY, United States of America",
            "STATE_MATCH",
            ["COUNTRY_MATCH", "COUNTRY_NO_MATCH", "STATE_MATCH"],
        ),
        (
            "Kraków, Poland",
            "Moscow, Russia",
            "COUNTRY_NO_MATCH",
            ["COUNTRY_NO_MATCH", "CITY_NO_MATCH"],
        ),
    ],
)
def test_geo_measure(ap_name, wl_name, expected_recommend, expected_eval, geo_measure_wrapper):
    results = geo_measure_wrapper.measure_call(ap_payload=ap_name, wl_payload=wl_name, context="")
    assert results.recommendation == expected_recommend
    assert len(results.results) == len(expected_eval)
    for result, expected in zip(results.results, expected_eval):
        result.evaluation = expected

import pytest

from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory
from tests.service_based.wrappers.utils import read_json_resource


@pytest.fixture(scope="module")
def org_name_knowledge_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("org_name_knowledge")
    return wrapper


@pytest.fixture(scope="module")
def org_name_measure_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("org_name_measure")
    return wrapper


@pytest.mark.parametrize(
    "text, results_file_name",
    [
        ("ABC Company", "org_name_knowledge_abc_company.json"),
        ("Hello World", "hello_world.json"),
    ],
)
def test_org_name_knowledge(text, results_file_name, org_name_knowledge_wrapper):
    results = org_name_knowledge_wrapper.knowledge_call(text)
    expected_results = read_json_resource(results_file_name)
    assert len(results) == len(expected_results)
    for result, expected in zip(results, expected_results):
        assert result.results == expected


@pytest.mark.parametrize(
    "ap_names, wl_names, expected_solution, expected_results",
    [
        (
            ["H&P"],
            ["Hewlett and Packard Company"],
            "MATCH",
            ["MATCH"],
        ),
        (
            ["Silent Eight"],
            ["KGHM SA"],
            "NO_MATCH",
            ["NO_MATCH"],
        ),
        (
            ["ABC Inc.", "DEF Ltd"],
            ["KL Company", "MN LTD", "OP sp. z. o. o."],
            "NO_MATCH",
            ["NO_MATCH", "NO_MATCH", "NO_MATCH", "NO_MATCH", "NO_MATCH", "NO_MATCH"],
        ),
        (
            "A few words about the XYZ company based in Nowhere",  # freetext example
            ["XYZ Inc."],
            "MATCH",
            ["MATCH"],
        ),
    ],
)
def test_org_name_measure(
    ap_names, wl_names, expected_solution, expected_results, org_name_measure_wrapper
):
    results = org_name_measure_wrapper.measure_call(
        ap_payload=ap_names, wl_payload=wl_names, context=""
    )
    assert results.recommendation == expected_solution
    assert len(results.results) == len(expected_results)
    for result, expected_result in zip(results.results, expected_results):
        assert result.evaluation == expected_result

import pytest

from business_layer.api import ValueKnowledge
from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory


@pytest.fixture(scope="module")
def date_knowledge_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("date_knowledge")
    return wrapper


@pytest.fixture(scope="module")
def date_measure_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("date_measure")
    return wrapper


@pytest.mark.parametrize(
    "text, expected_results",
    [
        (
            "01-02-2003",
            [
                ValueKnowledge(
                    original_input="01-02-2003",
                    results=["2003-01-02"],
                ),
                ValueKnowledge(
                    original_input="01-02-2003",
                    results=["2003-02-01"],
                ),
            ],
        ),
    ],
)
def test_date_knowledge(text, expected_results, date_knowledge_wrapper):
    results = date_knowledge_wrapper.knowledge_call(text)
    assert len(results) == len(expected_results)
    for result, expected in zip(sorted(results, key=lambda x: x.results[0]), expected_results):
        assert result.original_input == expected.original_input
        assert result.results == expected.results


@pytest.mark.parametrize(
    "freetexts, expected_results_sequence",
    [
        (
            ["01-02-2003", "09-09-2005"],
            [
                [
                    ValueKnowledge(
                        original_input="01-02-2003",
                        results=["2003-01-02"],
                    ),
                    ValueKnowledge(
                        original_input="01-02-2003",
                        results=["2003-02-01"],
                    ),
                ],
                [
                    ValueKnowledge(
                        original_input="09-09-2005",
                        results=["2005-09-09"],
                    ),
                ],
            ],
        ),
    ],
)
def test_date_knowledge_multiple(freetexts, expected_results_sequence, date_knowledge_wrapper):
    all_results = date_knowledge_wrapper.knowledge_streaming_call(freetexts)
    assert len(all_results) == len(expected_results_sequence)
    for results, expected_results in zip(all_results, expected_results_sequence):
        assert len(results) == len(expected_results)
        for result, expected in zip(sorted(results, key=lambda x: x.results[0]), expected_results):
            assert result.original_input == expected.original_input
            assert result.results == expected.results


@pytest.mark.parametrize(
    "first_freetext, second_freetext, expected_status, expected_results",
    [
        (
            "01-02-2003",
            "15-03-2005",
            "OK",
            [
                {
                    "min": 803,
                    "max": 803,
                    "ap_value": "2003-01-02",
                    "wl_value": "2005-03-15",
                },
                {
                    "min": 773,
                    "max": 773,
                    "ap_value": "2003-02-01",
                    "wl_value": "2005-03-15",
                },
            ],
        ),
    ],
)
def test_date_measure(
    first_freetext,
    second_freetext,
    expected_status,
    expected_results,
    date_measure_wrapper,
):
    call_results = date_measure_wrapper.measure_call(first_freetext, second_freetext, context="")
    assert call_results.recommendation == expected_status
    assert len(call_results.results) == len(expected_results)
    for call_result, expected_result in zip(
        sorted(call_results.results, key=lambda x: x.ap_value), expected_results
    ):
        assert call_result.ap_value == expected_result["ap_value"]
        assert call_result.wl_value == expected_result["wl_value"]
        assert call_result.metrics.distance_in_days.min == expected_result["min"]
        assert call_result.metrics.distance_in_days.max == expected_result["max"]

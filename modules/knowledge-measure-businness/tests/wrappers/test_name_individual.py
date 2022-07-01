import pytest

from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory


@pytest.fixture(scope="module")
def ind_name_measure_wrapper():
    wrapper = ServiceWrapperFactory().create_wrapper("individual_name_measure")
    return wrapper


@pytest.mark.parametrize(
    "ap_names, wl_names, expected_solution, results_ap",
    [
        (
            ["J. F. Kennedy"],
            ["John Fitzgerald Kennedy"],
            "MATCH",
            "J. F. Kennedy",
        ),
        (
            ["Adam Kowalski", "Jan Nowak"],
            ["Zbigniew Malinowski"],
            "HQ_NO_MATCH",
            "Adam Kowalski | Jan Nowak",
        ),
        (
            ["Admiral General Al Aladeen Wadiyah"],
            ["George Washington", "Benjamin Franklin", "Abraham Lincoln"],
            "NO_MATCH",
            "Admiral General Al Aladeen Wadiyah",
        ),
    ],
)
def test_individual_name_measure(
    ap_names, wl_names, expected_solution, results_ap, ind_name_measure_wrapper
):
    result = ind_name_measure_wrapper.measure_call(
        ap_payload=ap_names, wl_payload=wl_names, context=""
    )
    assert result.recommendation == expected_solution
    assert result.results[0].ap_value == results_ap

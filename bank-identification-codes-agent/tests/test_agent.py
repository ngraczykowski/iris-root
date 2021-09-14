import pytest

from bank_identification_codes_agent.agent import BankIdentificationCodesAgent
from tests.conftest import load_test_cases


@pytest.mark.parametrize("test_case", load_test_cases())
def test_bank_identification_codes_agent(test_case):
    # when
    result = BankIdentificationCodesAgent().resolve(test_case.input)
    expected_solution, expected_reason = test_case.expected_values

    # then
    print(result.reason)
    assert result.solution == expected_solution
    assert result.reason == expected_reason

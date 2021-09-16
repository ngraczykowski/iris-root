import pytest

from bank_identification_codes_agent.agent import BankIdentificationCodesAgent
from data_models.result import Solution
from tests.conftest import load_test_cases


@pytest.mark.parametrize("test_case", load_test_cases())
def test_bank_identification_codes_agent(test_case):
    # when
    result = BankIdentificationCodesAgent().resolve(test_case.input)
    expected_solution, expected_reason = test_case.expected_values

    # then
    assert result.solution == expected_solution
    assert result.reason == expected_reason


@pytest.mark.parametrize("agent_input", [(), "Some random text"])
def test_agent_error(agent_input):
    result = BankIdentificationCodesAgent().resolve(agent_input)
    assert result.solution == Solution.AGENT_ERROR

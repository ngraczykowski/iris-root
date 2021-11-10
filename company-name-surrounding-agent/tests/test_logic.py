import pytest

from company_name_surrounding.agent import CompanyNameSurroundingAgent
from company_name_surrounding.surrounding_check import get_company_token_number


@pytest.mark.parametrize(
    "names, expected",
    [
        (["xu wei wei industry co ltd"], 3),
        ([""], 0),
        ([None], 0),
        (["abc ltd", "bcd person"], 0),
        (["Bank of China"], 0),
        (["The Funny Company"], 2),
    ],
)
def test_surrounding_count(names, expected):
    assert get_company_token_number(names) == expected


@pytest.mark.parametrize(
    "names, expected_result, expected_solution",
    [
        (["xu wei wei industry co ltd"], 3, "3"),
        ([""], 0, "0"),
        ([None], 0, "0"),
        (["abc ltd", "bcd person"], 0, "0"),
        (["Bank of China"], 0, "0"),
        (["The Funny Company"], 2, "2"),
    ],
)
def test_agent(names, expected_result, expected_solution):
    agent = CompanyNameSurroundingAgent()
    result = agent.resolve(names)
    assert result.result == expected_result
    assert result.solution == expected_solution

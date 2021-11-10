import pytest

from company_name_surrounding.surrounding_check import get_company_token_number


@pytest.mark.parametrize(
    "name, expected",
    [
        (["xu wei wei industry co ltd"], 3),
        ([""], 0),
        ([None], 0),
        (["abc ltd", "bcd person"], 0),
    ],
)
def test_surrounding_count(name, expected):
    assert get_company_token_number(name) == expected

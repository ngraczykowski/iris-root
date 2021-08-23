import pytest

from company_name.names.parse.parse import _separate_parentheses


@pytest.mark.parametrize(
    "name, separate_name, parentheses",
    [
        ("Silent Eight", "Silent Eight", []),
        ("KGHM SA (Poland)", "KGHM SA", ['Poland'])
    ]
)
def test_separate_parentheses(name: str, separate_name: str, parentheses: str):
    separated_name, separated_parentheses = _separate_parentheses(name=name)
    assert separated_name == separate_name
    assert separated_parentheses == parentheses

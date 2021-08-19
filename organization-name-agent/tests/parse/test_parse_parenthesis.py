import pytest

from company_name.compare import parse_name


@pytest.mark.parametrize(
    ("name", "expected_parenthesis"),
    (
        ("Google", ()),
        ("(UK) Google (China)", ()),
        ("(Facebook) Google", ("facebook",)),
        ("(France) (Facebook) Google", ("facebook",)),
    ),
)
def test_parenthesis(name, expected_parenthesis):
    information = parse_name(name)
    assert {n.name().cleaned_name for n in information.parenthesis} == set(expected_parenthesis)

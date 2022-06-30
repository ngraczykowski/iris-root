import pytest

from organization_name_knowledge.names.parse.parse import parse_name


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
    assert set(information.parenthesis) == set(expected_parenthesis)


@pytest.mark.parametrize(
    "name, expected",
    [
        ("BAIANAT (ANTIGUO SMAS IP)", ("antiguo", "smas", "ip")),
        ("Alfa Limited (Beta China) Company", ("beta",)),
        ("Parser (Brackets Poland (Second)) sp. z. o. o", ("brackets", "second")),
        ("Name_one (Spain Name2 Portugal Netherlands) LTD", ("name2",)),
    ],
)
def test_get_name_from_brackets(name, expected):
    name_information = parse_name(name)
    assert name_information.parenthesis == expected

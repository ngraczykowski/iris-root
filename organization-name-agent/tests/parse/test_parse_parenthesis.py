import pytest

from company_name import CompanyNameAgent, Solution
from company_name.compare import compare, parse_name


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
        ("Name_one (Spain Name_2 Portugal Netherlands) LTD", ("name_2",)),
    ],
)
def test_get_name_from_brackets(name, expected):
    name_information = parse_name(name)
    assert name_information.parenthesis == expected


@pytest.mark.parametrize(
    "first, second",
    [
        ("Alfa (Beta) Poland", "Alfa Beta"),
        ("Super Fun Company (SFC)", "SFC Limited"),
        ("Software Producers (Poland Branch Microsoft Company)", "Microsoft Corp"),
        ("Arsenal (United Kingdom Football Club)", "Football Club Arsenal"),
        ("Polskone (Poland Drzwi na Wymiar)", "Drzwi na Wymiar Polskone"),
        ("ESP-FRA (Spain France Winery Limited)", "Winery ESP-FRA"),
        ("Bear (Paddington Limited Corporation)", "Paddington"),
    ],
)
def test_match_when_name_in_brackets(first, second):
    print(compare(first, second))
    result = CompanyNameAgent().resolve([first], [second])
    assert result.solution == Solution.MATCH

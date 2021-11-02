import pytest

from organization_name_knowledge.names.parse.parse import parse_name


@pytest.mark.parametrize(
    ("name", "expected_country"),
    (
        ("Google (UK)", ("uk",)),
        ("(UK) Google", ("uk",)),
        ("Google (UK) Facebook", ("uk",)),
        ("Google (United Kingdom)", ("united kingdom",)),
        ("(UK) Google (China)", ("uk", "china")),
        ("(France) (Facebook) Google", ("france",)),
        ("(U.K.) Google", ("uk",)),
    ),
)
def test_parse_country(name, expected_country):
    information = parse_name(name)
    print(information)
    assert set(information.countries.cleaned_tuple) == set(expected_country)


@pytest.mark.parametrize(
    "name",
    (
        "Nissan",
        "Nissan (awesome) Limited",
        "(Facebook) Google",
        "STRATEGIC SOLUTION ARCHITECTS (PTY) LTD",
        "DRIGATE PRODUCTS (MANUFACTURING) LIMITED",
    ),
)
def test_parse_not_countries(name):
    parsed = parse_name(name)
    print(parsed)
    assert not parsed.countries

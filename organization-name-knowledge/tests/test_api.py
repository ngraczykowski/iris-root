import pytest

from organization_name_knowledge.api import get_all_country_names, get_all_legal_terms, parse


@pytest.mark.parametrize(
    "name, expected_countries",
    [
        ("Silent Eight PTE LTD", set()),
        ("Company of Fun of Poland", {"Poland"}),
        ("Corporation of United Kingdom, London", {"United Kingdom"}),
        ("The Netherlands Football Association", {"The Netherlands", "Netherlands"}),
        ("Poland - Lithuania Union", {"Poland", "Lithuania"}),
    ],
)
def test_country(name, expected_countries):
    countries = get_all_country_names(name)
    assert countries == expected_countries


@pytest.mark.parametrize(
    "name, expected_legal",
    [
        ("Silent Eight PTE LTD", {"PTE", "LTD"}),
        ("Company of Fun Limited", {"Company", "Limited"}),
        ("Company of Fun", {"Company"}),
        ("Cracow City Foundation for Foreigners", {"Foundation"}),
    ],
)
def test_legal(name, expected_legal):
    legal_terms = get_all_legal_terms(name)
    assert legal_terms == expected_legal


@pytest.mark.parametrize(
    "name, expected_base",
    [
        ("Silent Eight PTE LTD", ("Silent", "Eight")),
        ("Company of Fun Limited", ("Fun",)),
        ("Corporation of London", ("Corporation", "of", "London")),
        ("The Hewlett and Packard Company", ("Hewlett", "and", "Packard")),
    ],
)
def test_base(name, expected_base):
    name_information = parse(name)
    assert name_information.base.original_tuple == expected_base

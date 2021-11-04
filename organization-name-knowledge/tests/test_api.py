import pytest

from organization_name_knowledge.api import get_legal_terms, parse


@pytest.mark.parametrize(
    "name, expected_legal",
    [
        ("Silent Eight PTE LTD", ("PTE", "LTD")),
        # if legal at the end, trailing considered as name part
        ("Company of Fun Limited", ("Limited",)),
        ("Company of Fun", ("Company", "of")),
        ("Cracow City Foundation for Foreigners", ("Foundation",)),
    ],
)
def test_legal(name, expected_legal):
    countries = get_legal_terms(name)
    assert countries == expected_legal


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

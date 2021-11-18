import pytest

from organization_name_knowledge.api import get_all_legal_terms, parse
from organization_name_knowledge.freetext.name_matching import generate_matching_legal_terms
from organization_name_knowledge.names.parse import create_tokens


@pytest.mark.parametrize(
    "name, expected_terms",
    [
        ("Silent Limited", ["limited liability company"]),
        ("Silent Eight Pte Ltd", ["Private limited company", "limited liability company"]),
        ("Corporation of Cracow", ["Corporation"]),
        ("The Corp. of XYZ", ["Corporation"]),
    ],
)
def test_generate_matching_legal_terms(name, expected_terms):
    tokens = create_tokens(name)
    for actual, expected in zip(generate_matching_legal_terms(tokens), expected_terms):
        assert actual[0].normalized == expected


@pytest.mark.parametrize(
    "name, expected_legal",
    [
        ("Silent Eight PTE LTD", {"PTE", "LTD"}),
        ("Company of Fun Limited", {"Company", "Limited"}),
        ("Company of Fun", {"Company"}),
        ("Cracow City Foundation for Foreigners", {"Foundation"}),
        ("Firma sp. z. o. o.", {"sp. z. o. o.", "sp."}),
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
        ("Aladeen Wadiya Inc.", ("Aladeen", "Wadiya")),
    ],
)
def test_base(name, expected_base):
    name_information = parse(name)
    assert name_information.base.original_tuple == expected_base

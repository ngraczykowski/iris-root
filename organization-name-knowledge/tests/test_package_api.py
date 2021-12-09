import logging

import pytest

from organization_name_knowledge.api import get_all_legal_terms, parse, parse_freetext

LOGGER = logging.getLogger(__name__)


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
def test_parse_name_base(name, expected_base):
    name_information = parse(name)
    assert name_information.base.original_tuple == expected_base


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "Silent Eight PTE LTD",
            [
                {"base": "Eight", "legal": "pte ltd", "source": "eight pte ltd"},
                {"base": "Silent Eight", "legal": "pte ltd", "source": "silent eight pte ltd"},
            ],
        ),
        (
            "Some Text about The Silent Eight PTE LTD founded years ago in Singapore",
            [
                {"base": "Eight", "legal": "pte ltd", "source": "eight pte ltd"},
                {"base": "Silent Eight", "legal": "pte ltd", "source": "the silent eight pte ltd"},
            ],
        ),
    ],
)
def test_parse_freetext(freetext, expected_names):
    parsed_freetext = parse_freetext(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    assert len(parsed_freetext) == len(expected_names)
    for name_information, expected in zip(parsed_freetext, expected_names):
        assert name_information
        assert name_information.base.cleaned_name == expected["base"].lower()
        assert name_information.legal.cleaned_name == expected["legal"].lower()
        assert name_information.source.cleaned == expected["source"].lower()

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
            "Some Text about The Silent Eight PTE LTD founded years ago in Singapore",
            [
                {"base": "Eight", "legal": "pte ltd"},
                {"base": "ltd founded", "legal": "ltd"},
                {"base": "ltd founded years", "legal": "ltd"},
                {"base": "pte ltd founded", "legal": "pte ltd"},
                {"base": "Silent Eight", "legal": "pte ltd"},
            ],
        ),
        (
            "First Company Limited, Second Corp",
            [
                {"base": "First", "legal": "company limited"},
                {"base": "limited second", "legal": "company limited"},
                {"base": "Second", "legal": "corp"},
            ],
        ),
        (
            "Magic LTD and The Hogwarts Inc.",
            [
                {"base": "Hogwarts", "legal": "inc"},
                {"base": "Magic", "legal": "ltd"},
            ],
        ),
        (
            "The Hewlett and Packard Company",
            [
                {"base": "Hewlett and Packard", "legal": "company"},
                {"base": "Packard", "legal": "company"},
            ],
        ),
        (
            "ACME CO and Google Inc",
            [
                {"base": "ACME", "legal": "CO"},
                {"base": "co and Google", "legal": "inc"},
                {"base": "Google", "legal": "Inc"},
            ],
        ),
        (
            "Paramount Pictures LLC or Walt Disney Company",
            [
                {"base": "Disney", "legal": "Company"},
                {"base": "LLC or", "legal": "LLC"},
                {"base": "LLC or Walt", "legal": "LLC"},
                {"base": "Paramount Pictures", "legal": "LLC"},
                {"base": "Pictures", "legal": "LLC"},
                {"base": "Walt Disney", "legal": "Company"},
            ],
        ),
        (
            "The NASA Hubble Space Telescope is a project of international cooperation "
            "between NASA, ESA also conducted by the ABC DEF Company. AURA’s Space Telescope Science "
            "Institute in Baltimore, Maryland, conducts Hubble science operations.",
            [
                {"base": "ABC DEF", "legal": "company"},
                {"base": "auras", "legal": "company"},
                {"base": "auras space", "legal": "company"},
                {"base": "auras space telescope", "legal": "company"},
                {"base": "DEF", "legal": "company"},
            ],
        ),
        (
            "At World's End The Dutchman arrives to the XYZ Limited",
            [
                {"base": "to the XYZ", "legal": "Limited"},
                {"base": "XYZ", "legal": "Limited"},
            ],
        ),
        (
            "KGHM Polska Miedź S A",
            [
                {"base": "KGHM Polska Miedz", "legal": "s a"},
                {"base": "Miedz", "legal": "s a"},
                {"base": "Polska Miedz", "legal": "s a"},
            ],
        ),
        (
            "12345 ABC Company",
            [{"base": "ABC", "legal": "company"}],
        ),
        (
            "Some long name for number 1234567 ABC Corporation",
            [
                {"base": "ABC", "legal": "Corporation"},
                {"base": "number ABC", "legal": "Corporation"},
            ],
        ),
        (
            "First Company Limited oraz Second Company",
            # polish conjunction 'oraz' is treated as possible base part
            [
                {"base": "First", "legal": "Company Limited"},
                {"base": "Limited oraz", "legal": "Company Limited"},
                {"base": "Limited oraz Second", "legal": "Company Limited"},
                {"base": "Oraz Second", "legal": "Company"},
                {"base": "Second", "legal": "Company"},
            ],
        ),
        (
            "IT36701908273410\r\n1/OTHER COMPANY",
            [
                {"base": "1 OTHER", "legal": "COMPANY"},
                {"base": "it 1 OTHER", "legal": "COMPANY"},
            ],
        ),
        (
            "123456 PR RETAIL LLC C/O BLACKPOINT PARTNERS, LLC 123 ABC ST SUITE 88 US 12345",
            [
                {"base": base, "legal": legal}
                for base, legal in zip(
                    [
                        "blackpoint",
                        "c o blackpoint",
                        "llc 123",
                        "llc 123 abc",
                        "partners",
                        "pr retail",
                        "retail",
                    ],
                    ["llc c o", "c o", "llc", "llc", "llc", "llc c o", "llc c o"],
                )
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

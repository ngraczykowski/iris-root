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
        (
            "First Company Limited, Second Corp",
            [
                {"base": "First", "legal": "company limited", "source": "first company limited"},
                {"base": "Second", "legal": "corp", "source": "second corp"},
            ],
        ),
        (
            "Magic LTD and The Hogwarts Inc.",
            [
                {"base": "Hogwarts", "legal": "inc", "source": "and the hogwarts inc"},
                {"base": "Magic", "legal": "ltd", "source": "magic ltd"},
            ],
        ),
        (
            "The Hewlett and Packard Company",
            [
                {
                    "base": "Hewlett and Packard",
                    "legal": "company",
                    "source": "the hewlett and packard company",
                },
                {"base": "Packard", "legal": "company", "source": "packard company"},
            ],
        ),
        (
            "ACME CO and Google Inc",
            [
                {"base": "ACME", "legal": "CO", "source": "acme co"},
                {"base": "Google", "legal": "Inc", "source": "google inc"},
            ],
        ),
        (
            "South Africa Organization",
            [
                {"base": "Africa", "legal": "Organization", "source": "Africa Organization"},
                {
                    "base": "South Africa",
                    "legal": "Organization",
                    "source": "South Africa Organization",
                },
            ],
        ),
        (
            "Corporation of London",
            [
                {
                    "base": "corporation of london",
                    "legal": "corporation of",
                    "source": "corporation of london",
                },
            ],
        ),
        (
            "Paramount Pictures LLC or Walt Disney Company",
            [
                {"base": "Disney", "legal": "Company", "source": "disney company"},
                {"base": "Paramount Pictures", "legal": "LLC", "source": "paramount pictures llc"},
                {"base": "Pictures", "legal": "LLC", "source": "pictures llc"},
                {"base": "Walt Disney", "legal": "Company", "source": "walt disney company"},
            ],
        ),
        (
            "The NASA Hubble Space Telescope is a project of international cooperation "
            "between NASA, ESA also conducted by the ABC DEF Company. AURA’s Space Telescope Science "
            "Institute in Baltimore, Maryland, conducts Hubble science operations.",
            [
                {"base": "ABC DEF", "legal": "company", "source": "the abc def company"},
                {"base": "DEF", "legal": "company", "source": "def company"},
            ],
        ),
        (
            "At World's End The Dutchman arrives to the XYZ Limited",
            [
                {"base": "XYZ", "legal": "Limited", "source": "the xyz limited"},
            ],
        ),
        (
            "KGHM Polska Miedź S A",
            [
                {"base": "KGHM Polska Miedz", "legal": "s a", "source": "kghm polska miedz s a"},
                {"base": "Miedz", "legal": "s a", "source": "miedz s a"},
                {"base": "Polska Miedz", "legal": "s a", "source": "polska miedz s a"},
            ],
        ),
        (
            "12345 ABC Company",
            [{"base": "ABC", "legal": "company", "source": "abc company"}],
        ),
        (
            "Some long name for number 1234567 ABC Corporation",
            [
                {"base": "ABC", "legal": "Corporation", "source": "abc corporation"},
                {
                    "base": "number ABC",
                    "legal": "Corporation",
                    "source": "for number abc corporation",
                },
            ],
        ),
        (
            "First Company Limited oraz Second Company",
            # polish conjunction 'oraz' is treated as possible base part
            [
                {"base": "First", "legal": "Company Limited", "source": "first company limited"},
                {"base": "Oraz Second", "legal": "Company", "source": "oraz second company"},
                {"base": "Second", "legal": "Company", "source": "second company"},
            ],
        ),
        (
            "IT36701908273410\r\n1/OTHER COMPANY",
            [
                {"base": "1 OTHER", "legal": "COMPANY", "source": "1/other company"},
                {"base": "it 1 OTHER", "legal": "COMPANY", "source": "it 1/other company"},
                {"base": "other", "legal": "company", "source": "other company"},
            ],
        ),
        (
            "[ORIGINATOR     ] IT36701908273410 AC 121140399 BANK OF TIANJIN CO LTD NO.15 YOU YI ROAD,HE XI DISTRICT",
            [
                {"base": "ac bank", "legal": "", "source": "ac bank"},
                {"base": "bank of tianjin", "legal": "co ltd", "source": "bank of tianjin co ltd"},
                {"base": "it ac bank", "legal": "", "source": "it ac bank"},
                {"base": "tianjin", "legal": "co ltd", "source": "of tianjin co ltd"},
            ],
        ),
        (
            "123456 PR RETAIL LLC C/O BLACKPOINT PARTNERS, LLC 123 ABC ST SUITE 88 US 12345",
            [
                {"base": base, "legal": legal, "source": source}
                for base, legal, source in zip(
                    [
                        "blackpoint",
                        "c o blackpoint",
                        "o blackpoint",
                        "partners",
                        "pr retail",
                        "retail",
                    ],
                    ["llc", "c o", "llc", "llc", "llc c o", "llc c o"],
                    [
                        "blackpoint partners, llc",
                        "c/o blackpoint",
                        "o blackpoint partners, llc",
                        "partners, llc",
                        "pr retail llc c/o",
                        "retail llc c/o",
                    ],
                )
            ],
        ),
        (
            "ABC\nDEF Company",
            [
                {"base": "ABC DEF", "legal": "Company", "source": "ABC DEF Company"},
                {"base": "DEF", "legal": "Company", "source": "DEF Company"},
            ],
        ),
        (
            "ABC\nCompany",
            [
                {"base": "ABC", "legal": "Company", "source": "ABC Company"},
            ],
        ),
        (
            "Allianz Global Corporation\n",
            [
                {
                    "base": "Allianz",
                    "legal": "Corporation",
                    "source": "Allianz Global Corporation",
                },
                {"base": "Global", "legal": "Corporation", "source": "Global Corporation"},
            ],
        ),
        (
            "SIMECONDUCTOR MANUFACTURING\nINTERNATIONAL(SHANGHAI)CORPORATION\n19 ZHANGIJANG"
            " ROAD,PUDONG NEW \nAREA,SHANGHAI,CHINA 123456",
            [
                {
                    "base": "INTERNATIONAL SHANGHAI",
                    "legal": "CORPORATION",
                    "source": "INTERNATIONAL SHANGHAI CORPORATION",
                },
                {
                    "base": "MANUFACTURING INTERNATIONAL SHANGHAI",
                    "legal": "CORPORATION",
                    "source": "MANUFACTURING INTERNATIONAL SHANGHAI CORPORATION",
                },
                {"base": "SHANGHAI", "legal": "CORPORATION", "source": "SHANGHAI CORPORATION"},
            ],
        ),
        (
            "/1234\r\n1/LLC VTB DC\r\n2/ABC BLDG, 12, ABC RD\r\n3/RU/MOSCOW\n",
            [
                {"base": "LLC VTB", "legal": "LLC", "source": "LLC VTB"},
                {"base": "LLC VTB DC", "legal": "LLC", "source": "LLC VTB DC"},
            ],
        ),
        (
            "A long history of SCB Bank starts about 100 b.c.",
            [{"base": "scb bank", "legal": "", "source": "of scb bank"}],
        ),
    ],
)
def test_parse_freetext(freetext, expected_names):
    parsed_freetext = parse_freetext(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    print([x.source.original for x in parsed_freetext])
    print([x.base.cleaned_name for x in parsed_freetext])
    assert len(parsed_freetext) == len(expected_names)
    for name_information, expected in zip(parsed_freetext, expected_names):
        assert name_information
        assert name_information.base.cleaned_name == expected["base"].lower()
        assert name_information.legal.cleaned_name == expected["legal"].lower()
        assert name_information.source.cleaned == expected["source"].lower()

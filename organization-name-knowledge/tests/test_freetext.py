import pytest

from organization_name_knowledge import parse_freetext


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "Some Text about The Silent Eight PTE LTD founded years ago in Singapore",
            [{"base": "Silent Eight", "legal": "pte ltd"}],
        ),
        (
            "This is the best test case for Silent Eight Pte Ltd ever created",
            [
                {"base": "Silent Eight", "legal": "pte ltd"},
            ],
        ),
        (
            "First Company Limited, Second sp. z. o. o.",
            [
                {"base": "First", "legal": "company limited"},
                {"base": "Second", "legal": "sp z o o"},
            ],
        ),
        (
            "Magic LTD and The Hogwarts Inc.",
            [{"base": "Hogwarts", "legal": "inc"}, {"base": "Magic", "legal": "ltd"}],
        ),
        (
            "The Hewlett and Packard Company",
            [
                {"base": "Packard", "legal": "company"},
                {"base": "Hewlett and Packard", "legal": "company"},
            ],
        ),
        (
            "ACME CO and Google Inc",
            [{"base": "ACME", "legal": "CO"}, {"base": "Google", "legal": "Inc"}],
        ),
        (
            "Paramount Pictures LLC or Walt Disney Company",
            [
                {"base": "Paramount Pictures", "legal": "LLC"},
                {"base": "Walt Disney", "legal": "Company"},
            ],
        ),
        (
            "The NASA Hubble Space Telescope is a project of international cooperation "
            "between NASA, ESA and the ABC DEF Company. AURA’s Space Telescope Science "
            "Institute in Baltimore, Maryland, conducts Hubble science operations.",
            [{"base": "ABC DEF", "legal": "company"}],
        ),
        (
            "At World's End The Dutchman arrives to the XYZ Limited",
            [{"base": "XYZ", "legal": "Limited"}],
        ),
        (
            "KGHM Polska Miedź S A",
            [{"base": "KGHM Polska Miedz", "legal": "s a"}],
        ),
        (
            "12345 ABC Company",
            [{"base": "ABC", "legal": "company"}],
        ),
        (
            "Some long name with number 1234567 ABC Corporation",
            [{"base": "ABC", "legal": "Corporation"}]
        )
    ],
)
def test_freetext(freetext, expected_names):
    parsed_freetext = parse_freetext(freetext, tokens_limit=5)
    # print([x.base.cleaned_name for x in parsed_freetext])
    assert parsed_freetext
    for name_information, expected in zip(parsed_freetext, expected_names):
        assert name_information
        assert name_information.base.cleaned_name == expected["base"].lower()
        assert name_information.legal.cleaned_name == expected["legal"].lower()

import pytest

from organization_name_knowledge.freetext.parse import parse_freetext_names


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "Information about the XYZ Company Limited",
            [{"base": "XYZ", "legal": "Company Limited", "source": "the XYZ Company Limited"}],
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
    ],
)
def test_parse_freetext_1_name(freetext, expected_names):
    parsed_freetext = parse_freetext_names(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    _check_results(parsed_freetext, expected_names)


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
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
            "Paramount Pictures LLC or Walt Disney Company",
            [
                {"base": "Disney", "legal": "Company", "source": "disney company"},
                {"base": "Paramount Pictures", "legal": "LLC", "source": "paramount pictures llc"},
                {"base": "Pictures", "legal": "LLC", "source": "pictures llc"},
                {"base": "Walt Disney", "legal": "Company", "source": "walt disney company"},
            ],
        ),
        (
            "Whatsapp Company was bought by The Facebook Inc",
            [
                {"base": "Facebook", "legal": "Inc", "source": "The Facebook Inc"},
                {"base": "Whatsapp", "legal": "Company", "source": "Whatsapp Company"},
            ],
        ),
    ],
)
def test_parse_freetext_2_names(freetext, expected_names):
    parsed_freetext = parse_freetext_names(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    _check_results(parsed_freetext, expected_names)


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "A long history of SCB Bank starts about 100 b.c.",
            [{"base": "SCB Bank", "legal": "", "source": "of SCB Bank"}],
        ),
        (
            "There is a gift from Bank of Russia for James Bond",
            [{"base": "Bank of Russia", "legal": "", "source": "Bank of Russia"}],
        ),
        (
            "Boss Protection Group",
            [
                {"base": "Boss Protection", "legal": "group", "source": "Boss Protection Group"},
                {"base": "Protection", "legal": "group", "source": "Protection Group"},
            ],
        ),
    ],
)
def test_parse_freetext_name_from_markers(freetext, expected_names):
    parsed_freetext = parse_freetext_names(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    _check_results(parsed_freetext, expected_names)


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "There are gifts from Google",
            [{"base": "Google", "legal": "", "source": "Google"}],
        ),
        (
            "Berkshire Hathaway created by Warren Buffet",
            [{"base": "Berkshire Hathaway", "legal": "", "source": "Berkshire Hathaway"}],
        ),
        (
            "Procter & Gamble",
            [{"base": "Procter & Gamble", "legal": "", "source": "Procter & Gamble"}],
        ),
        (
            "American Electric Power",
            [
                {
                    "base": "American Electric Power",
                    "legal": "",
                    "source": "American Electric Power",
                }
            ],
        ),
        (
            "Sberbank of Russia",
            [
                {
                    "base": "Sberbank",
                    "legal": "",
                    "source": "Sberbank",
                }
            ],
        ),
        (
            "XYZvtb some text",
            [
                {
                    "base": "vtb",
                    "legal": "",
                    "source": "vtb",
                }
            ],
        ),
    ],
)
def test_parse_freetext_from_known_org_names(freetext, expected_names):
    parsed_freetext = parse_freetext_names(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    _check_results(parsed_freetext, expected_names)


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
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
            "XYZ 9999999 88888 Company Limited is here",
            [{"base": "XYZ", "legal": "Company Limited", "source": "XYZ Company Limited"}],
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
            "\rWXYZ Limited",
            [
                {"base": "WXYZ", "legal": "Limited", "source": "WXYZ Limited"},
            ],
        ),
    ],
)
def test_parse_freetext_invalid_tokens(freetext, expected_names):
    parsed_freetext = parse_freetext_names(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    _check_results(parsed_freetext, expected_names)


def _check_results(parsed_freetext, expected_names):
    assert len(parsed_freetext) == len(expected_names)
    for name_information, expected in zip(parsed_freetext, expected_names):
        assert name_information
        assert name_information.base.cleaned_name == expected["base"].lower()
        assert name_information.legal.cleaned_name == expected["legal"].lower()
        assert name_information.source.cleaned == expected["source"].lower()

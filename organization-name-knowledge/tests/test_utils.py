import pytest

from organization_name_knowledge.utils.term_variants import get_term_variants
from organization_name_knowledge.utils.text import (
    clear_name,
    contains_conjunction,
    divide,
    remove_split_chars,
    remove_too_long_numbers,
    split_text_by_too_long_numbers,
    starts_with_conjunction,
)


@pytest.mark.parametrize(
    "name, result",
    [
        ("Silent Eight", False),
        ("Hewlett and Packard", True),
        ("Left or right side", True),
        ("Cooperation for AML", True),
        ("ABC and DEF or GHIJ for KLMN", True),
        # and some conjunctions inside words - we DON'T want them to be treated as conjunctions
        ("On demand", False),
        ("Malo more", False),
        ("Foreigners", False),
    ],
)
def test_contains_conjunction(name, result):
    assert contains_conjunction(name.split()) == result


@pytest.mark.parametrize(
    "name, result",
    [
        ("ABC Company", False),
        ("And the Company", True),
        ("Hewlett and Packard", False),
        ("For you and your family", True),
        ("Foreign country", False),
        ("Organization Name Knowledge", False),
        ("Or the other one", True),
    ],
)
def test_starts_with_conjunction(name, result):
    assert starts_with_conjunction(name.split()) == result


@pytest.mark.parametrize(
    "name, expected_clean_name",
    [
        ("name", "name"),
        ("    name     ", "name"),
        ("Silent Eight Pte Ltd", "silent eight pte ltd"),
        ("#Company%* Limited ", "company limited"),
        ("Hewlett & Packard", "hewlett & packard"),
    ],
)
def test_clear_name(name, expected_clean_name):
    assert clear_name(name) == expected_clean_name


@pytest.mark.parametrize(
    "name, expected",
    [
        ("name", ("name",)),
        ("Silent Eight Pte Ltd", ("Silent", "Eight", "Pte", "Ltd")),
        ("Tutti-Frutti", ("Tutti-Frutti",)),
        ("Tutti - Frutti", ("Tutti", "Frutti")),
        ("One:Two", ("One", "Two")),
        ("hey, you", ("hey", "you")),
        ("dot.com", ("dot.", "com")),
    ],
)
def test_divide(name, expected):
    assert divide(name) == expected


@pytest.mark.parametrize(
    "term, expected_variants",
    [
        ("name", {"name", "n a m e"}),
        (
            "Silent Eight",
            {"S i l e n t E i g h t", "S i l e n t Eight", "Silent E i g h t", "Silent Eight"},
        ),
        ("silenteight.com", {"silenteight.com", "silenteightcom", "s i l e n t e i g h t c o m"}),
    ],
)
def test_get_term_variants(term, expected_variants):
    assert get_term_variants(term) == expected_variants


@pytest.mark.parametrize(
    "name, expected",
    [
        ("name", "name"),
        ("Silent Eight", "Silent Eight"),
        ("dot.com", "dotcom"),
        ("Tutti, Frutti", "Tutti Frutti"),
        ("One!=Two", "One!Two"),
    ],
)
def test_remove_split_chars(name, expected):
    assert remove_split_chars(name) == expected


@pytest.mark.parametrize(
    "name, expected",
    [
        ("123", ["123"]),
        ("123 456", ["123 456"]),
        ("M1 Centrum Handlowe", ["M1 Centrum Handlowe"]),
        ("1111 Aloha Hawai", ["", "Aloha Hawai"]),
        ("Number between 12345 strings", ["Number between", "strings"]),
        ("123456 XD 5656778887665", ["", "XD", ""]),
    ],
)
def test_split_text_by_too_long_numbers(name, expected):
    assert split_text_by_too_long_numbers(name) == expected


@pytest.mark.parametrize(
    "name, expected",
    [
        ("123", "123"),
        ("123 456", "123 456"),
        ("M1 Centrum Handlowe", "M1 Centrum Handlowe"),
        ("1111 Aloha Hawai", " Aloha Hawai"),
        ("Number between 12345 strings", "Number between  strings"),
        ("123456 XD 5656778887665", " XD "),
    ],
)
def test_remove_too_long_numbers(name, expected):
    assert remove_too_long_numbers(name) == expected

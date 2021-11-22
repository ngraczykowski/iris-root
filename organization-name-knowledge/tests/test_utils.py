import pytest

from organization_name_knowledge.api import get_all_legal_terms
from organization_name_knowledge.freetext.name_matching import cut_name_to_leftmost_match
from organization_name_knowledge.utils.term_variants import get_term_variants
from organization_name_knowledge.utils.text import (
    clear_name,
    divide,
    remove_split_chars,
    remove_too_long_numbers,
    split_text_by_too_long_numbers,
)


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
        ("The ABCD company", "The ABCD company"),
        ("KGHM SA - the biggest based in Poland company", "KGHM SA"),
        ("Silent Eight Pte Ltd means our team", "Silent Eight Pte Ltd"),
    ],
)
def test_cut_name_to_leftmost_legal(name, expected):
    assert cut_name_to_leftmost_match(name, get_all_legal_terms(name)) == expected


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

import pytest

from bank_identification_codes_agent.text_utils import get_first_match, remove_no_word_characters


@pytest.mark.parametrize(
    "first, second, expected",
    [
        ("MATCH", "Text with MATCH and after that another MATCH occurrence", "MATCH"),
        ("123456", "number: 123.456/789))", "123456789"),
        ("555", "prefix---555----suffix", "PREFIX555SUFFIX"),
        ("123456", "123- -456", "123 456"),
        ("abc", "abc[123]def", None),
        ("phrase", "phr-break-ase", None),
        ("Name", "Name", None),
        ("NAME", "Name", "NAME"),
    ],
)
def test_first_match(first, second, expected):
    assert get_first_match(first, second) == expected


@pytest.mark.parametrize(
    "text, expected",
    [
        ("Text", "Text"),
        ("Text2", "Text2"),
        ("Text_3", "Text_3"),
        ("Text 4", "Text4"),
        ("Text-with/punctuation", "Textwithpunctuation"),
    ],
)
def test_remove_no_words_characters(text, expected):
    assert remove_no_word_characters(text) == expected

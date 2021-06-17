import pytest

from company_name.compare import compare


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("AMAZON", "amazon"),
        ("Atlassian Corporation Plc", "Atlassian Corporation"),
        ("Activision-Blizzard", "Activision Blizzard, Inc. "),
        ("Alexion Pharmaceuticals Inc.", "Alexion"),
        ("EISOO Information Technology Corp.", "eisoo"),
        ("Atlassian Corporation Plc", "Atlassian Corporation public limited company"),
    ),
)
def test_basic(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value > 0.6


@pytest.mark.parametrize(
    ("first", "second"), (("AMAZON", "GOOGLE"), ("intuit", "intuitive surgical"))
)
def test_basic_negative(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value < 0.6


@pytest.mark.parametrize(
    ("first", "second"),
    (("AIOC", "A I O C"), ("AIOC", "A\tI  O \tC")),
)
def test_whitespaces(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Group Grant", "Grant"),
        ("the al", "al"),
        ("The Kraft Heinz Company", "Kraft Heinz"),
    ),
)
def test_common_prefixes(first, second):
    print(repr(first), repr(second))
    score = compare(first, second)
    print(score)
    assert score["fuzzy_on_base"].value == 1

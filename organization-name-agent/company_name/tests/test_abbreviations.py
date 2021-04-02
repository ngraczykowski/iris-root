import pytest

from company_name.compare import compare


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("ACLU", "American Civil Liberties Union"),
        ("HUD", "Housing and Urban Development"),
        ("M&M", "Mars & Murrie’s"),
        ("Agricultural Bank of China", "ABC"),
        ("Dalian Hi-Think Computer Technology Corporation", "DHC"),
        ("The Kraft Heinz Company", "KHC"),
    ),
)
def test_simple_abbreviation(first, second):
    print(repr(first), repr(second), compare(first, second))
    score, _ = compare(first, second)
    assert score["abbreviation"] == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (("REI", "Recreational Equipment, Inc."),),
)
def test_abbreviation_with_legal(first, second):
    print(repr(first), repr(second), compare(first, second))
    score, _ = compare(first, second)
    assert score["abbreviation"] == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("China Medical Technologies, Inc.", "CMED"),
        ("Commercial Aircraft Corporation of China, Ltd.", "COMAC"),
        ("China Ocean Shipping Company, Limited", "COSCO"),
        ("China Ocean Shipping Company, Limited", "COSCO Limited"),
    ),
)
def test_abbreviation_more_than_one_letter(first, second):
    print(repr(first), repr(second), compare(first, second))
    score, _ = compare(first, second)
    assert 0.7 < score["abbreviation"] < 1


@pytest.mark.parametrize(
    ("first", "second"),
    (("4H", "Head, Heart, Hands, Health"),),
)
def test_abbreviation_with_duplicates(first, second):
    print(repr(first), repr(second), compare(first, second))
    score, _ = compare(first, second)
    assert score["abbreviation"] == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("L. L. Bean", "Leon Leonwood Bean"),
        ("L.L.Bean", "Leon Leonwood Bean"),
        ("ALS Association", "Amyotrophic Lateral Sclerosis Association"),
        ("Agricultural Bank of China", "AgBank"),
    ),
)
def test_partial_abbreviation(first, second):
    print(repr(first), repr(second), compare(first, second))
    score, _ = compare(first, second)
    assert score["abbreviation"] > 0.7


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Aa Ii Oooo Ccccc", "AIOC"),
        ("Aa Ii Oooo Ccccc", "A I O C"),
        ("Aa Ii Oooo Ccccc", "A\tI  O \tC"),
    ),
)
def test_whitespaces(first, second):
    print(repr(first), repr(second), compare(first, second))
    score, _ = compare(first, second)
    assert score["abbreviation"] == 1

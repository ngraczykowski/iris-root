import pytest

from company_name.compare import compare, Score


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("ACLU", "American Civil Liberties Union"),
        ("HUD", "Housing and Urban Development"),
        ("M&M", "Mars & Murrie's"),
        ("Agricultural Bank of China", "ABC"),
        ("The Kraft Heinz Company", "KHC"),
    ),
)
def test_simple_abbreviation(first, second):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)
    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value == 1
    assert score["abbreviation"].compared == ((first,), (second,))


@pytest.mark.parametrize(
    ("first", "first_source", "second"),
    (
        (
            "Dalian Hi-Think Computer Technology Corporation",
            "Dalian Hi-Think Computer",
            "DHC",
        ),
        ("Jiangling Motors Corporation Limited", "Jiangling Motors Corporation", "JMC"),
    ),
)
def test_abbreviation_without_suffix_or_legal(first, first_source, second):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)
    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value == 1
    assert score["abbreviation"].compared == ((first_source,), (second,))


@pytest.mark.parametrize(
    ("first", "second"),
    (("REI", "Recreational Equipment Inc."),),
)
def test_abbreviation_with_legal(first, second):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)
    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value == 1
    assert score["abbreviation"].compared == ((first,), (second,))


@pytest.mark.parametrize(
    ("first", "first_source", "second"),
    (
        ("China Medical Technologies, Inc.", "China Medical", "CMED"),
        (
            "Commercial Aircraft Corporation of China, Ltd.",
            "Commercial Aircraft Corporation",
            "COMAC",
        ),
        (
            "China Ocean Shipping Company, Limited",
            "China Ocean Shipping Company",
            "COSCO",
        ),
        (
            "China Ocean Shipping Company, Limited",
            "China Ocean Shipping Company",
            "COSCO Limited",
        ),
    ),
)
def test_abbreviation_more_than_one_letter(first, first_source, second):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)
    assert score["abbreviation"].status.name == "OK"
    assert 0.7 < score["abbreviation"].value < 1
    assert score["abbreviation"].compared[0] == (first_source,)


@pytest.mark.parametrize(
    ("first", "second"),
    (("4H", "Head, Heart, Hands, Health"),),
)
def test_abbreviation_with_duplicates(first, second):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)
    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value == 1
    assert score["abbreviation"].compared == ((first,), (second.replace(",", ""),))


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
    score = compare(first, second)
    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value > 0.7


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
    score = compare(first, second)

    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value == 1
    assert score["abbreviation"].compared == ((first,), (" ".join(second.split()),))


@pytest.mark.parametrize(("first", "second"), (("HEWLETT-PACKARD COMPANY", "HP"),))
def test_separators(first, second):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)

    assert score["abbreviation"].status.name == "OK"
    assert score["abbreviation"].value == 1


@pytest.mark.parametrize(
    ("first", "second", "status"),
    (
        ("", "", Score.ScoreStatus.NO_DATA),
        ("COMPANY", "", Score.ScoreStatus.NO_MATCHED_PARTY_DATA),
        ("", "COMPANY", Score.ScoreStatus.NO_ALERTED_PARTY_DATA),
        ("COMPANY", "COMPANY", Score.ScoreStatus.OK),
    ),
)
def test_when_no_acceptable_data(first, second, status):
    print(repr(first), repr(second), compare(first, second))
    score = compare(first, second)
    assert score["abbreviation"].status == status
    assert score["abbreviation"].value == 0

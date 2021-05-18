import pytest
from company_name.compare import compare, Score


def _get_single_token(score):
    single_tokens = [n for n in score.compared if len(n) == 1]
    assert len(single_tokens) == 1
    return single_tokens[0][0].lower()


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("The Walt Disney Company", "Disney"),
        ("TYSON", "	Tyson Foods"),
        ("Mondelez International", "Mondelēz"),
        ("walgreens", "	Walgreens Boots Alliance"),
        ("Nissan North America", "Nissan"),
    ),
)
def test_token_inclusion(first, second):
    result = compare(first, second)
    print(result)

    assert result["token_inclusion"].status == Score.ScoreStatus.OK
    assert result["token_inclusion"].value == 1
    single_token = _get_single_token(result["token_inclusion"])
    assert single_token == first.lower() or single_token == second.lower()


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Ferric Machinery Inc", "Ferric Inc"),
        ("SUMITOMO CORPORATION", "SUMITOMO SHOJI KAISHA, LTD"),
    ),
)
def test_token_inclusion_with_legal(first, second):
    result = compare(first, second)
    print(result)
    assert result["token_inclusion"].status == Score.ScoreStatus.OK
    assert result["token_inclusion"].value == 1

    single_token = _get_single_token(result["token_inclusion"])
    assert single_token in first.lower() and single_token in second.lower()


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("MERCEDES-BENZ ESPAÑA SA", "mercedes"),
        ("MERCEDES-BENZ ESPAÑA SA", "mercedes-benz"),
    ),
)
def test_token_inclusion_with_separators(first, second):
    result = compare(first, second)
    print(result)
    assert result["token_inclusion"].status == Score.ScoreStatus.OK
    assert result["token_inclusion"].value == 1

    single_token = _get_single_token(result["token_inclusion"])
    assert single_token in first.lower() and single_token in second.lower()


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Thermo Fisher Scientific", "her"),
        ("Mandarin ooo", "Mandarins Centre LTD"),
    ),
)
def test_not_token_inclusion(first, second):
    result = compare(first, second)
    print(result)
    assert result["token_inclusion"].status == Score.ScoreStatus.OK
    assert result["token_inclusion"].value == 0

    single_token = _get_single_token(result["token_inclusion"])
    assert single_token in first.lower() or single_token in second.lower()


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Bank of New York Mellon", "Bank of America"),
        ("Fuding Precision Industry (Zhengzhou) Co., Ltd.", "Fuding Precision"),
        (
            "THE CHANCELLOR, MASTERS AND SCHOLARS OF THE UNIVERSITY OF OXFORD",
            "THE UNIVERSITY OF OXFORD",
        ),
    ),
)
def test_no_one_token_word(first, second):
    result = compare(first, second)
    print(result)
    assert result["token_inclusion"].status == Score.ScoreStatus.NOT_APPLICABLE
    assert result["token_inclusion"].value == 0
    assert result["token_inclusion"].compared == ((), ())

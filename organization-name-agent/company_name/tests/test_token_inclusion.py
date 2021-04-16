import pytest
from company_name.compare import compare


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
    score = compare(first, second)
    print(score)
    assert score['token_inclusion'].value == 1
    assert score['token_inclusion'].compared == (tuple(first.split()), tuple(second.split()))


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Thermo Fisher Scientific", "her"),
        ("Bristol-Myers Squibb", "Myers"),
    ),
)
def test_not_token_inclusion(first, second):
    score = compare(first, second)
    print(score)
    assert score['token_inclusion'].value == 0
    assert score['token_inclusion'].compared == (tuple(first.split()), tuple(second.split()))


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Bank of New York Mellon", "Bank of America"),
        ("Mandarin ooo", "Mandarins Centre LTD")
    ),
)
def test_no_one_token_word(first, second):
    score = compare(first, second)
    print(score)
    assert score['token_inclusion'].value == None
    assert score['token_inclusion'].compared == ((), ())

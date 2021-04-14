import pytest

from company_name.compare import compare


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("google", "amazon"),
        ("Berkshire Hathaway", "Bank of America"),
        ("Chevron Corporation", "Microsoft Corporation"),
        ("Exor N.V.", "Allianz SE"),
    ),
)
def test_tokenization_for_completely_different_companies(first, second):
    print(first, second)
    score = compare(first, second)
    print(score)
    assert score['tokenization'].value == 0
    assert score['tokenization'].compared
    assert score['absolute_tokenization'].value == 0
    assert score['absolute_tokenization'].compared
    assert score['tokenization'].compared == score['absolute_tokenization'].compared


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("China Life Insurance", "Bank of China"),
        ("Toyota Motor", "Nissan Motor"),
    ),
)
def test_tokenization_for_one_common_word(first, second):
    print(first, second)
    score = compare(first, second)
    print(score)
    assert 0 < score['tokenization'].value < 1
    assert score['absolute_tokenization'].value == 1
    assert score['tokenization'].compared == score['absolute_tokenization'].compared


@pytest.mark.parametrize(
    ("first", "second", "tokenization", "absolute_tokenization"),
    (
        (
            "State Grid Corporation of China",
            "China National Petroleum Corporation",
            1 / 6,
            1,
        ),
        (
            "New York Life Insurance Company",
            "Nationwide Mutual Insurance Company",
            1 / 6,
            1,
        ),
        ("	U.S. Bancorp", "U.S. Department of Defense", 1 / 4, 1),
        ("AGRICULTURAL BANK OF CHINA", "china construction bank", 2 / 4, 2),
        ("Johnson & Johnson", "SC Johnson", 1 / 3, 1),
        ("Johnson & Johnson", "Johnson & Johnson", 1, 2)
    ),
)
def test_tokenization(first, second, tokenization, absolute_tokenization):
    print(first, second)
    score = compare(first, second)
    print(score)
    assert score['tokenization'].value == tokenization
    assert score['absolute_tokenization'].value == absolute_tokenization
    assert score['tokenization'].compared == score['absolute_tokenization'].compared

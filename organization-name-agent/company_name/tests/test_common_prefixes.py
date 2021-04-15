import pytest

from company_name.compare import compare, parse_name


@pytest.mark.parametrize(("value", "prefixes"), (("the al", ("the",)),))
def test_parse_common_prefixes(value, prefixes):
    name = parse_name(value)
    print(name)
    assert name.common_prefixes == list(prefixes)


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

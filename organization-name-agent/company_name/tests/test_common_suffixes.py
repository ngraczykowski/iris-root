import pytest

from company_name.compare import compare, parse_name


@pytest.mark.parametrize(
    ("name", "expected_common_suffixes"),
    (
        ("Google", ()),
        ("EISOO Information Technology Corp.", ("information", "technology")),
    ),
)
def test_common_suffixes(name, expected_common_suffixes):
    information = parse_name(name)
    assert information.common_suffixes == expected_common_suffixes


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Hafei Equity Co., Ltd.", "Hafei"),
        ("Hasee Computer Company, Ltd", "hasee"),
        ("Huawei Technologies Co., Ltd. ", "huawei"),
        ("Inspur Group", "inspur"),
    ),
)
def test_common_on_one_side(first, second):
    print(repr(first), repr(second))
    score = compare(first, second)
    print(score)

    assert score["fuzzy_on_base"].value == 1
    base_length = min(len(first), len(second))
    assert score["fuzzy_on_base"].compared == (
        (first[:base_length],),
        (second[:base_length],),
    )

    assert score["fuzzy_on_suffix"].value == 0

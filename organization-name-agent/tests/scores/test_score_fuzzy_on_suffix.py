import pytest

from company_name.compare import compare


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

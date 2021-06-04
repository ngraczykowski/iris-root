import pytest

from company_name.compare import compare, Score


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Deutsche Asset Mgt", "Deutsche"),
        ("Deutsche", "Deutsche Asset Mgt"),
        ("The Korea Development bank", "Korea Development bank"),
    ),
)
def test_first_token(first, second):
    result = compare(first, second)
    print(result)
    assert result["first_token"].status == Score.ScoreStatus.OK
    assert result["first_token"].value == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("4COMMONGOOD (4CG) PRODUCTIONS SDN. BHD.", "4 COMMON GOOD"),
        (
            "Yingchuang (Qingdao) Interconnection Intelligence Co., Ltd.",
            "Interconnection Intelligence Yingchuang Co., Ltd.",
        ),
        (
            "The Associated Press",
            "The Athlete Agency"
        )
    ),
)
def test_different_first_token(first, second):
    result = compare(first, second)
    print(result)
    assert result["first_token"].status == Score.ScoreStatus.OK
    assert result["first_token"].value == 0


@pytest.mark.parametrize(
    ("first", "second"),
    (("Deutsche Asset Mgt", ""), ("", "Deutsche Asset Mgt"), ("", "")),
)
def test_no_first_token(first, second):
    result = compare(first, second)
    print(result)
    assert result["first_token"].status != Score.ScoreStatus.OK
    assert result["first_token"].value == 0

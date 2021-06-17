import pytest

from company_name import compare, Score


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("google", "gooogle"),
        ("alphabet", "alfabet"),
        ("Berlyn International", "Berlin Internacional"),
    ),
)
def test_typos(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy"].value > 0.6


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Advanced Micro Devices, Inc. (AMD)", "Advanced Micro Devices"),
        ("American Electric Power (AEP)", "American Electric Power"),
        ("Analog Devices, Inc. (ADI)", "Analog"),
    ),
)
def test_additional_information_doesnt_diffuse(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy"].value > 0.6
    assert result["fuzzy_on_base"].value == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("American Electric Power (AEP)", "AEP"),
        ("TRANSED", "Transport for Elderly and Disabled Persons (TRANSED)"),
    ),
)
def test_parenthesis_match(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["parenthesis_match"].value == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Industrial and Commercial Bank of China", "China"),
        ("TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED", "China"),
    ),
)
def test_country_as_company(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert not result["country"].value
    assert result["fuzzy_on_base"] < 0.3


@pytest.mark.parametrize(
    ("first", "second"),
    (("AMAZON", "Amazon.com, Inc."),),
)
def test_name_with_domains(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Gree Electric Appliances Inc. of Zhuhai", "Gree Electric Appliances"),
        ("Gree Electric Appliances Inc. of Zhuhai", "Gree Electric Appliances Inc."),
    ),
)
def test_places(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value > 0.6


@pytest.mark.parametrize(
    ("first", "second"),
    (("Mondelez International", "Mondelēz"),),
)
def test_diacritic(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value == 1
    assert result["fuzzy"].compared == ((first,), (second,))


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("f and  import export", "famie"),
        ("a & a  industries inc", "a&aiii"),
        ("256 equities inc", "joint stock company yamalsairlines"),
        ("r7 group", "r7 gronp"),
    ),
)
def test_strange_input(first, second):
    print(repr(first), repr(second), compare(first, second))


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("International", "International"),
        ("pplc", "pplc"),
    ),
)
def test_only_legal_in_names(first, second):
    print(repr(first), repr(second))
    result = compare(first, second)
    print(result)
    assert result["fuzzy_on_base"].value == 1
    assert result["fuzzy_on_base"].compared == ((first,), (second,))


def test_empty_name():
    result = compare("", "")
    print(result)
    for score in result.values():
        assert score == Score()


@pytest.mark.parametrize(
    ("first", "second", "status"),
    (
        ("", "", Score.ScoreStatus.NO_DATA),
        ("COMPANY", "", Score.ScoreStatus.NO_MATCHED_PARTY_DATA),
        ("", "COMPANY", Score.ScoreStatus.NO_ALERTED_PARTY_DATA),
    ),
)
def test_empty_names(first, second, status):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    for score in result.values():
        assert score.status == status or Score.ScoreStatus.NO_DATA

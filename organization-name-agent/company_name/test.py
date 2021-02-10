import pytest

from .compare import compare, score


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("AMAZON", "amazon"),
        ("Atlassian Corporation Plc", "Atlassian Corporation"),
        ("Activision-Blizzard", "Activision Blizzard, Inc. "),
        ("Alexion Pharmaceuticals Inc.", "Alexion"),
        ("EISOO Information Technology Corp.", "eisoo"),
    ),
)
def test_basic(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(("first", "second"), (("AMAZON", "GOOGLE"),))
def test_basic_negative(first, second):
    assert not compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("google", "gooogle"),
        ("alphabet", "alfabet"),
        ("Berlyn International", "Berlin Internacional"),
    ),
)
def test_typos(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("ACLU", "American Civil Liberties Union"),
        ("HUD", "Housing and Urban Development"),
        ("M&M", "Mars & Murrie’s"),
        ("L. L. Bean", "Leon Leonwood Bean"),
        ("L.L.Bean", "Leon Leonwood Bean"),
        ("REI", "Recreational Equipment, Inc."),
        ("4H", "Head, Heart, Hands, Health"),
        ("ALS Association", "Amyotrophic Lateral Sclerosis Association"),
        ("Agricultural Bank of China", "AgBank"),
        ("Agricultural Bank of China", "ABC"),
        ("China Medical Technologies, Inc.", "CMED"),
        ("Commercial Aircraft Corporation of China, Ltd.", "COMAC"),
        ("China Ocean Shipping Company, Limited", "COSCO"),
        ("Dalian Hi-Think Computer Technology Corporation", "DHC"),
        ("The Kraft Heinz Company", "KHC"),
    ),
)
def test_abbreviation(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Advanced Micro Devices, Inc. (AMD)", "Advanced Micro Devices"),
        ("American Electric Power (AEP)", "American Electric Power"),
        ("American Electric Power (AEP)", "AEP"),
        ("Analog Devices, Inc. (ADI)", "Analog"),
        ("TRANSED", "Transport for Elderly and Disabled Persons (TRANSED)"),
    ),
)
def test_additional_info(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Industrial and Commercial Bank of China", "China"),
        ("TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED", "China"),
        ("Air China Limited", "China"),
        ("Bank of China", "China"),
    ),
)
def test_country_as_company(first, second):
    assert not compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("ASML Holding", "ASML Holding N.V."),
        ("CDW Corporation", "CDW"),
        ("Charter Communications, Inc.", "Charter"),
        ("toyota motor finance", "TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED"),
        ("Jiangling Motors Corporation Limited", "JMC"),
    ),
)
def test_legal(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Hafei Motor Co., Ltd.", "Hafei"),
        ("Hasee Computer Company, Ltd", "hasee"),
        ("Huawei Technologies Co., Ltd. ", "huawei"),
        ("Inspur Group", "inspur"),
    ),
)
def test_common(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("The Walt Disney Company", "Disney"),
        ("AMAZON", "Amazon.com, Inc."),
        ("The Kraft Heinz Company", "Kraft Heinz"),
    ),
)
def test_hard(first, second):
    assert compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Nissan North America, Inc", "Nissan Computer Corporation"),
        ("Nissan Motor Co", "Nissan Computer Corporation"),
    ),
)
def test_nissan(first, second):
    assert not compare(first, second), score(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Gree Electric Appliances Inc. of Zhuhai", "Gree Electric Appliances"),
        ("Gree Electric Appliances Inc. of Zhuhai", "Gree Electric Appliances Inc."),
    ),
)
def test_places(first, second):
    assert compare(first, second), score(first, second)


def test_score_legal_terms():
    assert score("A corp", "A corporation")["legal_terms"] == 1
    assert score("A corp", "A")["legal_terms"] == 0.5
    assert score("A plc", "A  limited")["legal_terms"] == 0


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Mondelez International, Inc.", "Mondelēz"),
    ),
)
def test_diacritic(first, second):
    assert compare(first, second), score(first, second)
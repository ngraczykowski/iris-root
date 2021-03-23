import pytest

from company_name.compare import compare


def check_compare(first, second):
    result, _ = compare(first, second)
    for k in ("abbreviation", "fuzzy", "fuzzy_on_base"):
        if result[k] > 0.6:
            return True
    return False


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("AMAZON", "amazon"),
        ("Atlassian Corporation Plc", "Atlassian Corporation"),
        ("Activision-Blizzard", "Activision Blizzard, Inc. "),
        ("Alexion Pharmaceuticals Inc.", "Alexion"),
        ("EISOO Information Technology Corp.", "eisoo"),
        ("Atlassian Corporation Plc", "Atlassian Corporation public limited company"),
    ),
)
def test_basic(first, second):
    print(repr(first), repr(second), compare(first, second))
    result, _ = compare(first, second)
    assert result["fuzzy_on_base"] > 0.6


@pytest.mark.parametrize(
    ("first", "second"), (("AMAZON", "GOOGLE"), ("intuit", "intuitive surgical"))
)
def test_basic_negative(first, second):
    print(repr(first), repr(second), compare(first, second))
    result, _ = compare(first, second)
    assert result["fuzzy_on_base"] < 0.6


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
    assert check_compare(first, second)


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
        ("China Ocean Shipping Company, Limited", "COSCO Limited"),
        ("Dalian Hi-Think Computer Technology Corporation", "DHC"),
        ("The Kraft Heinz Company", "KHC"),
    ),
)
def test_abbreviation(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


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
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Industrial and Commercial Bank of China", "China"),
        ("TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED", "China"),
        #("Air China Limited", "China"),
        #("Bank of China", "China"),
    ),
)
def test_country_as_company(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert not check_compare(first, second)


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
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


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
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("The Walt Disney Company", "Disney"),
        ("AMAZON", "Amazon.com, Inc."),
        ("The Kraft Heinz Company", "Kraft Heinz"),
    ),
)
def test_hard(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Gree Electric Appliances Inc. of Zhuhai", "Gree Electric Appliances"),
        ("Gree Electric Appliances Inc. of Zhuhai", "Gree Electric Appliances Inc."),
    ),
)
def test_places(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


def test_score_legal_terms():
    assert compare("A corp", "A corporation")[0]["legal_terms"] == 1
    assert compare("A corp", "A")[0]["legal_terms"] == 0.5
    assert compare("A plc", "A  limited")[0]["legal_terms"] == 0


@pytest.mark.parametrize(
    ("first", "second"),
    (("Mondelez International, Inc.", "Mondelēz"),),
)
def test_diacritic(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


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
    (("AIOC", "A I O C"), ("AIOC", "A\tI  O \tC")),
)
def test_whitespaces(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)



@pytest.mark.parametrize(
    ("first", "second"),
    (("Group Grant", "Grant"), ("the al", "al")),
)
def test_common_prefixes(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("International", "International"),
        ("Internacional", "International"),
        ("pplc", "pplc"),
        ("", ""),
    ),
)
def test_empty_names(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert check_compare(first, second)


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Gazprom International School of Currency", "gazprom (uk) middle east fz company"),
        ("VTB Capital PLC","VTB KAPITAL ZHILAYA NEDVIZHIMOST OOO"),
        ("VTB-company", "company-something_very-vtb-serious")
    )
)
def test_blacklist(first, second):
    print(repr(first), repr(second), compare(first, second))
    assert compare(first, second)[0]['blacklisted'] == 1

@pytest.mark.parametrize(
    ("first", "second", "expected_score"),
    (
        ("TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED", "TOYOTA MOTOR FINANCE (THE PEOPLE'S REPUBLIC OF CHINA) COMPANY LIMITED", 1),
        ("Nissan (UK) ", "Somophore (United Kingdom)", 1),
        ("Nissan (UK) Limited", "Nissan (CHINA) Limited", 0),
        ("Nissan (UK) Limited", "Nissan", 0.5),
        ("Nissan (awesome) Limited", "Nissam (awesome) corporation", 0.5)
    ),
)
def test_countries(first, second, expected_score):
    print(repr(first), repr(second), compare(first, second))
    scored, _ = compare(first, second)
    assert scored['country'] == expected_score


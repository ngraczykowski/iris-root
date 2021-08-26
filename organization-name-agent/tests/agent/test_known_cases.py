import pytest

from company_name import CompanyNameAgent

POSITIVE_CASES = [
    [
        ["AL JAZEIRA SERVICES SOA (HFSM)"],
        ["AL JAZERRA"],
    ],
    [
        ["THE LARSEN & TOUBRO LIMITED PROVIDENT FUND OF 1952"],
        ["LARSEN AND TOUBRO LIMITED"],
    ],
    [
        ["HP, INC"],
        ["HEWLETT-PACKARD COMPANY (HP CO.)"],
    ],
    [
        ["IBREKOM CARGO LC"],
        ["IBREKOM FZCO"],
    ],
    [
        ["MHJJIAXINGLTD"],
        ["MHT JIAXING LTD"],
    ],
    [
        ["NINGBA TWO BIRDS IND.COL.LTDMH BLDG."],
        ["NINGBA TWO BIRDS INDUSTRY COMPNAY LIMITED"],
    ],
    # [
    #     ["RT LOJISTIK VE DIS TIC.LET.STI"],
    #     ["RT-LOJISTIKA, OJSC"],
    # ],
    [
        ["SAMA NAJD EST"],
        ["SAMA NAJD OFFICE"],
    ],
    [
        ["MINER TRAVELS (INDIA) PVT LTD"],
        ["MINAR TRAVELS (INDIA) PRIVATE LIMITED"],
    ],
    [
        ["Power construction coporation of china"],
        ["POWER S.A.L"],
    ],
    [
        ["TETR-PAK INDIAN PRIVATE LIMITED"],
        ["TETRA PAK EXPORT FZE"],
    ],
    [
        ["SAVOLA GIDA SAN VE TIC A.S."],
        ["SAVOLA GROUP"],
    ],
    [
        ["SABA FOUNDATION FOR WHOLESALE"],
        ["SABA AND CO"],
    ],
    [
        ["HOR0Z L0JISTIC"],
        ["HOROZ LOGISTIK"],
    ],
    [
        ["TIANSHUI HALIN IMPORT EXPORT CORPORATION"],
        ["GANSU TIANSHUI HAILIN IMPORT AND EXPORT CO LTD"],
    ],
    [
        ["Sonangol Sinopec International Limited"],
        ["SONAGOL E.P"],
    ],
    [
        ["JP Morgan"],
        ["Morgan JP"],
    ],
]

INCONCLUSIVE_CASES = []

NEGATIVE_CASES = [
    [
        ["GOOGLE"],
        ["FACEBOOK"],
    ],
]


@pytest.mark.parametrize(("ap_names", "mp_names"), POSITIVE_CASES)
def test_same_companies(ap_names, mp_names):
    print(ap_names, mp_names)
    result = CompanyNameAgent().resolve(ap_names, mp_names)
    print(result)
    assert result.solution == "MATCH"


@pytest.mark.parametrize(("ap_names", "mp_names"), INCONCLUSIVE_CASES)
def test_difficult_cases(ap_names, mp_names):
    print(ap_names, mp_names)
    result = CompanyNameAgent().resolve(ap_names, mp_names)
    print(result)
    assert result.solution == "INCONCLUSIVE"


@pytest.mark.parametrize(("ap_names", "mp_names"), NEGATIVE_CASES)
def test_different_companies(ap_names, mp_names):
    print(ap_names, mp_names)
    result = CompanyNameAgent().resolve(ap_names, mp_names)
    print(result)
    assert result.solution == "NO_MATCH"

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
    [
        ["RT LOJISTIK VE DIS TIC.LET.STI"],
        ["RT-LOJISTIKA, OJSC"],
    ],
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
    [
        ["world i.p.c. oh se kye"],
        ["ipc"],
    ],
    # [
    #     ["world i. p. c. oh se kye"],
    #     ["ipc"]
    # ],
    [
        ["D.B.INTERNATIONAL LOGISTICS CAMBODIA CO, LTD."],
        ["D.B. INT.L LOGISTICS (CAMBODIA) CO. LTD"],
    ],
    [
        ["THE SSZ LTD", "The SSZ Limited"],
        ["SSZ OOO"],
    ],
    [
        ["GRAND PARTNER LTD", "GRAND PARTNER LIMITED"],
        ["GROUP GRAND LIMITED", "GROUP GRAND LTD"],
    ],
    [
        ["Gazprom (UK) Limited", "GAZPROM UK LTD"],
        ["GAZPROM MKS OOO"],
    ],
    [
        ["Gazprom (UK) Limited", "GAZPROM UK LTD"],
        ["GAZPROM EP INTERNATIONAL SERVICES BV"],
    ],
    [
        ["Gazprom (UK) Limited", "GAZPROM UK LTD"],
        ["GAZPROM GEROSGAZ MANAGEMENT BV"],
    ],
    [
        ["Gazprom (UK) Limited", "GAZPROM UK LTD"],
        ["GAZPROM EP INTERNATIONAL INVESTMENTS BV"],
    ],
    [
        ["SINO OCEAN TRADING LTD", "(海信貿易有限公司) SINO OCEAN TRADING LIMITED"],
        ["SINO OCEAN SHIPPING CO LTD"],
    ],
    [
        ["SHANKS BV", "Shanks BV"],
        ["SHANKS OOO", "SHANS LLC"],
    ],
    [
        [
            "AL JAZEIRA SRVCS SOA HSFM",
            "AL JAZEIRA SRVCS SOA (HSFM)",
            "AL JAZEIRA SERVICES SOA (HFSM)",
            "AL JAZEIRA SERVICES SOA HFSM",
        ],
        ["AL JAZERRA"],
    ],
    [
        ["ALI"],
        ["ALI SHER SHINWARY LTD"],
    ],
    [
        ["VTB Capital PLC", "VTB CAPITAL PLC"],
        ["VTB KAPITAL ZHILAYA NEDVIZHIMOST OOO"],
    ],
    [
        ["ZHENG DAO GROUP LTD", "(正道集團有限公司) ZHENG DAO GROUP LIMITED"],
        ["ZHENG DTO"],
    ],
    [
        ["Gazprom (UK) Limited", "GAZPROM UK LTD"],
        ["GAZPROM INTERNATIONAL TRAINING BV"],
    ],
    [
        ["BRC ARABIA LLC"],
        ["BRC"],
    ],
    [
        ["Gazprom (UK) Limited", "GAZPROM UK LTD"],
        ["GAZPROM TRANSSERVIS OOO"],
    ],
    [
        ["BANK VTB 24 CJSC RE FX SPOT", "Bank VTB 24 CJSC Re FX/Spot"],
        ["BANK VTB OAO"],
    ],
    [
        ["BANK VTB 24 CJSC RE FX SPOT", "Bank VTB 24 CJSC Re FX/Spot"],
        ["VTB REGISTRAR CJSC"],
    ],
    [
        ["IAC"],
        ["I A C"],
    ],
    [
        ["OCEAN TRADING CO", "(海洋貿易公司) OCEAN TRADING COMPANY"],
        ["OCEAN GROUP S A"],
    ],
    [
        ["GRAND INTERNATIONAL GROUP HOLDINGS LTD"],
        ["GROUP GRAND LIMITED", "GROUP GRAND LTD"],
    ],
    [
        ["ABDUL RAHMAN"],
        ["ABDULRAHMAN"],
    ],
    [
        ["AL SHABAB SHOW ROOM"],
        ["AL CHABAB", "AL SHABAB", "AL SHABBAB"],
    ],
    [
        ["GRAND ENTERPRISES GROUP LIMITED", "GRAND ENTERPRISES GROUP LTD"],
        ["GROUP GRAND LIMITED", "GROUP GRAND LTD"],
    ],
    [
        ["BENVOLENT FUND"],
        ["BENEVOLENT INTERNATIONAL FUND", "BENEVOLENCE INTERNATIONAL FUND"],
    ],
    [
        ["ABDUL KARIM LTD", "Abdul Karim Ltd"],
        ["WAEL ABDUL KARIM GROUP", "ABDULKARIM GROUP"],
    ],
    [
        [
            "CHINA OVERSEAS PROPERTY SERVICES LTD",
            "(中國海外物業服務有限公司) CHINA OVERSEAS PROPERTY SERVICES LIMITED",
        ],
        [
            "CHINA OVERSEAS PROPERTY HOLDINGS LIMITED",
            "CHINA OVERSEAS PROPERTY HOLDINGS LTD",
        ],
    ],
    [
        [
            "MEGATRADE AND INVEST CORP",
            "MEGATRADE AND INVESTMENT CORP",
            "MEGATRADE AND INVESTMENT CORPORATION",
            "MEGATRADE AND INVESTMENT CORP",
        ],
        ["MEGATRADE"],
    ],
    [
        ["HARA GLOBAL HOLDINGS LTD", "HARA GLOBAL HOLDINGS LIMITED"],
        ["HARA COMPANY", "HARA CO"],
    ],
    [
        ["SKS CONSULTANT UK LTD", "Sks Consultant Uk Ltd"],
        ["SKS"],
    ],
    [
        ["GRAND TRADING CO", "GRAND TRADING CO (格蘭貿易公司)"],
        ["GROUP GRAND LIMITED", "GROUP GRAND LTD"],
    ],
]

INCONCLUSIVE_CASES = []

NEGATIVE_CASES = [
    [
        ["4Life UK Ltd", "4LIFE UK LTD"],
        ["TRUST LIFE"],
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

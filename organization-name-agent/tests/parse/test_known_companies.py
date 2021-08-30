import pytest

from company_name.names.parse.parse import parse_name


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "SOCIETE COOPERATIVE AGRICOLE ET VITICOLE BOURGOGNE DU SUD",
            {"base": "SOCIETE COOPERATIVE AGRICOLE ET VITICOLE BOURGOGNE DU SUD"},
        ),
        (
            "EGYPTIAN COMPANY FOR COSMETICS (ECC)",
            {"base": "EGYPTIAN COMPANY FOR COSMETICS"},
        ),
        (
            "LUBELSKIE BIOGAZOWNIE SP Z O O W RESTRUKTURYZACJI",
            {
                "base": "LUBELSKIE BIOGAZOWNIE",
                "legal": "SP Z O O",
                "other": "W RESTRUKTURYZACJI",
            },
        ),
        (
            "PREDUZECE KANEGO DOO KRAGUJEVAC",
            {"base": "PREDUZECE KANEGO", "legal": "DOO", "other": "KRAGUJEVAC"},
        ),
        ('"S & T COMPANY" d.o.o.', {"base": '"S & T', "legal": 'COMPANY" d.o.o.'}),
        (
            "Corporation of The Town of Whitby, The ",
            {"base": "Corporation of The Town of Whitby"},
        ),
        (
            "FAXINFORME, GESTÃO DE INFORMAÇÃO E SERVIÇOS, LDA - EM LIQUIDAÇÃO ",
            {
                "base": "FAXINFORME GESTÃO DE INFORMAÇÃO E SERVIÇOS",
                "legal": "LDA",
                "other": "EM LIQUIDAÇÃO",
            },
        ),
        ("BRUSKO, Joze Saje s.p.", {"base": "BRUSKO Joze Saje", "legal": "s.p."}),
        (
            "TECHNIRAIL S. A. - MANILA BRANCH",
            {"base": "TECHNIRAIL", "legal": "S. A.", "other": "MANILA BRANCH"},
        ),
        (
            "MEWOD PRODUKCJA USŁUGI I HANDEL S C FRANCISZEK GOLONKA WOJCIECH HERMAN LESŁAW ZDON",
            {
                "base": "MEWOD PRODUKCJA USŁUGI I HANDEL",
                "legal": "S C",
                "other": "FRANCISZEK GOLONKA WOJCIECH HERMAN LESŁAW ZDON",
            },
        ),
        (
            "Cia de Ferro Ligas da Bahia Ferbasa",
            {"base": "Cia de Ferro Ligas", "legal": "da", "other": "Bahia Ferbasa"},
        ),
        (
            "TRIL BENGALURU REAL ESTATE FOUR PRIVATE LIMITED",
            {"base": "TRIL BENGALURU REAL ESTATE FOUR", "legal": "PRIVATE LIMITED"},
        ),
        (
            "THE ASSET MANAGEMENT GROUP",
            {
                "common_prefixes": "THE",
                "base": "ASSET",
                "common_suffixes": "MANAGEMENT",
                "legal": "GROUP",
            },
        ),
        (
            "MODERN DETERGENT IND.L.L.C",
            {
                "base": "MODERN DETERGENT IND.",
                "legal": "L.L.C",
            },
        ),
    ),
)
def test_company_name(name, expected):
    parsed = parse_name(name)
    print(parsed)
    for key, value in expected.items():
        assert value == getattr(parsed, key).original_name

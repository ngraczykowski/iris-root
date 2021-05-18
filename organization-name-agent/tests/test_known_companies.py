import pytest

from company_name.names.parse_name import parse_name


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
        ("\"S & T COMPANY\" d.o.o.", {"base": "\"S & T", "legal": "COMPANY\" d.o.o."}),
        (
            "M2A SOLUTIONS SOCIEDAD COMERCIAL DE RESPONSABILIDAD LIMITADA  M2A SOLUTIONS S.R.L.",
            {
                # TODO
            },
        ),
        (
            "Corporation of The Town of Whitby, The ",
            {"base": "Corporation of The Town of Whitby"},
        ),
        (
            "TADIRAN TELECOM (TTL), LIMITED PRTNERSHIP",
            {
                # TODO
            },
        ),
        (
            "FAXINFORME, GESTÃO DE INFORMAÇÃO E SERVIÇOS, LDA - EM LIQUIDAÇÃO ",
            {
                "base": "FAXINFORME GESTÃO DE INFORMAÇÃO E SERVIÇOS",
                "legal": "LDA",
                "other": "EM LIQUIDAÇÃO",
            },
        ),
        (
            "BRUSKO, Joze Saje s.p.",
            {
                "base": "BRUSKO Joze Saje",
                "legal": "s.p."
            }
        )
    ),
)
def test_company_name(name, expected):
    parsed = parse_name(name)
    print(parsed)
    for key, value in expected.items():
        assert value == getattr(parsed, key).original_name

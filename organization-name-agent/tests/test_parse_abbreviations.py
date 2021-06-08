import pytest

from company_name.compare import parse_name


@pytest.mark.skip
@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "SDB (Schoonmaak Diensten Bennekom)",
            {"base": "Schoonmaak Diensten Bennekom"},
        ),
        (
            "I.m.e.o. (international Multicultural Events Organisation) Emmanuel Kollie",
            {"base": "international Multicultural Events Organisation"},
        ),
        (
            "Genossenschaft für die Wasserversorgung Kalpetran (GWK)",
            {"base": "Genossenschaft für die Wasserversorgung Kalpetran"},
        ),
        (
            "ASSOCIATION OF RESPIRATORY CARE PRACTITIONERS PHILS. (ARCPP) INC.",
            {"base:": "ASSOCIATION OF RESPIRATORY CARE PRACTITIONERS PHILS."},
        ),
        (
            "I.C.M (INDUSTRIAL CONSTRUCTION AND MAINTENANCE) CO., LTD",
            {"base": "INDUSTRIAL CONSTRUCTION AND MAINTENANCE"},
        ),
    ),
)
def test_name_with_additional_abbreviation(name, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert set(getattr(information, key)) == set(value.split())

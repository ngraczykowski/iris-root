import pytest

from organization_name_knowledge.names.parse.parse import parse_name


@pytest.mark.parametrize(
    ("name", "expected_base"),
    [
        (
            "Genossenschaft für die Wasserversorgung Kalpetran (GWK)",
            "Genossenschaft fur die Wasserversorgung Kalpetran",
        ),
        (
            "ASSOCIATION OF RESPIRATORY CARE PRACTITIONERS PHILS. (ARCPP) INC.",
            "ASSOCIATION OF RESPIRATORY CARE PRACTITIONERS PHILS",
        ),
    ],
)
def test_name_with_additional_abbreviation(name, expected_base):
    information = parse_name(name)
    assert information.base == tuple(expected_base.lower().split())


@pytest.mark.skip
@pytest.mark.parametrize(
    ("name", "expected_base"),
    (
        (
            "SDB (Schoonmaak Diensten Bennekom)",
            "Schoonmaak Diensten Bennekom",
        ),
        (
            "I.m.e.o. (international Multicultural Events Organisation) Emmanuel Kollie",
            "international Multicultural Events Organisation",
        ),
        (
            "I.C.M (INDUSTRIAL CONSTRUCTION AND MAINTENANCE) CO., LTD",
            "INDUSTRIAL CONSTRUCTION AND MAINTENANCE",
        ),
    ),
)
def test_name_with_additional_abbreviation_at_start(name, expected_base):
    information = parse_name(name)
    assert information.base == tuple(expected_base.lower().split())

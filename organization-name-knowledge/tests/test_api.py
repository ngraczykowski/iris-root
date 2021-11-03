import pytest

from organization_name_knowledge.api import get_countries, get_legal_terms, parse


@pytest.mark.parametrize("name, expected_countries", [("KGHM Poland", ("Poland",))])
def test_countries(name, expected_countries):
    countries = get_countries(name)
    assert countries == expected_countries


@pytest.mark.parametrize("name, expected_legal", [("Silent Eight PTE LTD", ("PTE", "LTD"))])
def test_countries(name, expected_legal):
    countries = get_legal_terms(name)
    assert countries == expected_legal


@pytest.mark.parametrize(
    "name, expected_base",
    [
        (
            "Silent Eight PTE LTD",
            (
                "silent",
                "eight",
            ),
        )
    ],
)
def test_countries(name, expected_base):
    name_information = parse(name)
    assert name_information.base == expected_base

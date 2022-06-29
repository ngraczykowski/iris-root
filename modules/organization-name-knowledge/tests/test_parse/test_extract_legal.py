import pytest

from organization_name_knowledge.names.parse.create_tokens import create_tokens
from organization_name_knowledge.names.parse.extract_legal_terms import extract_legal_terms


@pytest.mark.parametrize(
    "name, expected",
    [
        (
            "Skoda Volkswagen Company alfabet",
            {
                "without_legal": ("skoda", "volkswagen"),
                "legal": ("company",),
                "other": ("alfabet",),
            },
        ),
        (
            "Silent Eight Pte Ltd",
            {
                "without_legal": ("silent", "eight"),
                "legal": ("pte", "ltd"),
                "other": (),
            },
        ),
        (
            "Limited some name limited",
            {
                "without_legal": ("limited", "some", "name"),
                "legal": ("limited",),
                "other": (),
            },
        ),
    ],
)
def test_extract_legal_from_end(name, expected):
    without_legal, legal, other = extract_legal_terms(name=create_tokens(name))
    assert legal == expected["legal"]
    assert without_legal == expected["without_legal"]
    assert other == expected["other"]


@pytest.mark.parametrize(
    "name, expected",
    [
        (
            "JOINT STOCK COMPANY POLOTSK-STEKLO",
            {
                "without_legal": ("polotsk-steklo",),
                "legal": ("joint stock company",),
                "other": (),
            },
        ),
        (
            "Limited Liability Company ABCD",
            {
                "without_legal": ("abcd",),
                "legal": ("limited liability company",),
                "other": (),
            },
        ),
    ],
)
def test_extract_legal_from_start(name, expected):
    without_legal, legal, other = extract_legal_terms(name=create_tokens(name))
    assert legal == expected["legal"]
    assert without_legal == expected["without_legal"]
    assert other == expected["other"]


@pytest.mark.parametrize(
    "name, expected",
    [
        (
            "Company EjBiSiDi",
            {
                "without_legal": ("company", "ejbisidi"),
                "legal": ("company",),
                "other": (),
            },
        ),
        (
            "Corporation of Cracow sp. z. o. o.",
            {
                "without_legal": ("corporation", "of", "cracow"),
                "legal": ("sp z o o",),
                "other": (),
            },
        ),
        (
            "Underwater company of Dolphins",
            {
                "without_legal": ("underwater", "company", "of", "dolphins"),
                "legal": ("company", "of"),
                "other": (),
            },
        ),
    ],
)
def test_legal_as_part_of_name(name, expected):
    without_legal, legal, other = extract_legal_terms(name=create_tokens(name))
    assert legal == expected["legal"]
    assert without_legal == expected["without_legal"]
    assert other == expected["other"]

from typing import Dict

import pytest

from company_name.names.parse.create_tokens import create_tokens
from company_name.names.parse.extract_legal_terms import extract_legal_terms


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
            "Company EjBiSiDi",
            {
                "without_legal": ("company", "ejbisidi"),
                "legal": ("company",),
                "other": (),
            },
        ),
        (
            "JOINT STOCK COMPANY POLOTSK-STEKLO",
            {
                "without_legal": ("polotsk-steklo",),
                "legal": ("joint stock company",),
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
            "Limited some name limited",
            {
                "without_legal": ("limited", "some", "name"),
                "legal": ("limited",),
                "other": (),
            },
        ),
    ],
)
def test_extract_legal_terms(name: str, expected: Dict):
    without_legal, legal, other = extract_legal_terms(name=create_tokens(name))
    assert legal == expected["legal"]
    assert without_legal == expected["without_legal"]
    assert other == expected["other"]

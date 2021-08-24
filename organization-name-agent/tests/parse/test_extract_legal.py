from typing import Dict

import pytest

from company_name.names.parse.create_tokens import create_tokens
from company_name.names.parse.extract_information import extract_legal_terms


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
            "GM Company beta",
            {
                "without_legal": ("gm",),
                "legal": ("company",),
                "other": ("beta",),
            },
        ),
        (
            "Company EjBiSiDi",
            {
                "without_legal": ("company", "ejbisidi"),
                "legal": (),
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
    ],
)
def test_extract_legal_terms(name: str, expected: Dict):
    without_legal, legal, other = extract_legal_terms(name=create_tokens(name))
    assert legal == expected["legal"]
    assert without_legal == expected["without_legal"]
    assert other == expected["other"]

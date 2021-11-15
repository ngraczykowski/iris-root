from typing import Sequence

import pytest
from organization_name_knowledge import parse

from company_name import CompanyNameAgent, Solution
from company_name.scores.abbreviation import get_abbreviation_score
from company_name.utils.names_abbreviations_filtering import remove_redundant_abbreviations


@pytest.mark.parametrize(
    "abbreviation, full",
    [
        ("SCB", "Standard Chartered Bank"),
        ("ABC", "Alfa Beta Casa LTD"),
        ("FA", "Funny Abbreviations Limited"),
        ("BB", "Big Bank Corp."),
        ("B&B", "Bed and Breakfast"),
    ],
)
def test_abbreviation_recognition(abbreviation: str, full: str):
    abbreviation_from_full = get_abbreviation_score(parse(full), parse(abbreviation))
    assert abbreviation_from_full.status.name == "OK"
    assert abbreviation_from_full.value == 1


@pytest.mark.parametrize(
    "names, expected_names",
    [
        (
            ("Bed & Breakfast Company of Kraków", "Bed & Breakfast", " B&B", "BB"),
            ("Bed & Breakfast Company of Kraków", "Bed & Breakfast"),
        ),
        (
            (
                "Apple Inc.",
                "The Film Makers of California",
                "Narodowy Bank Polski",
                "NBP",
                "Microsoft",
                "FM",
                "TFMoC",
            ),
            (
                "Apple Inc.",
                "The Film Makers of California",
                "Narodowy Bank Polski",
                "Microsoft",
            ),
        ),
    ],
)
def test_members_abbreviations_removing(names: Sequence[str], expected_names: Sequence[str]):
    without_redundant_abbreviations = remove_redundant_abbreviations(
        [parse(name) for name in names]
    )
    names_without_redundant_abbreviations = [
        name.source.original for name in without_redundant_abbreviations
    ]
    assert names_without_redundant_abbreviations == list(expected_names)


@pytest.mark.parametrize(
    "names, valid_terms",
    [
        (("SCB", "Standard Chartered Bank"), ("Siam Commercial Bank",)),
        (
            ("Apple Inc.", "The Walmart", "Narodowy Bank Polski", "NBP", "Microsoft"),
            ("New Base Project",),
        ),
    ],
)
def test_resolving_with_many_names_abbreviation(names: Sequence[str], valid_terms: Sequence[str]):
    result = CompanyNameAgent().resolve(names, valid_terms)
    assert result.solution == Solution.NO_MATCH

from typing import Sequence

import pytest

from company_name import CompanyNameAgent, Solution, parse_name
from company_name.scores.abbreviation import get_abbreviation_score
from company_name.utils.abbreviations_filtering import remove_members_abbreviations


@pytest.mark.parametrize(
    "abbreviation, full",
    [
        ("SCB", "Standard Chartered Bank"),
        ("ABC", "Alfa Beta Casa LTD"),
        ("FA", "Funny Abbreviations Limited"),
        ("BB", "Big Bank Corp.")
    ],
)
def test_abbreviation_recognition(abbreviation: str, full: str):
    abbreviation_from_full = get_abbreviation_score(parse_name(full), parse_name(abbreviation))
    assert abbreviation_from_full.status.name == "OK"
    assert abbreviation_from_full.value == 1


@pytest.mark.parametrize(
    "names_list, expected_names_list",
    [
        (("Bed & Breakfast Company of Kraków", "Bed & Breakfast", " B&B", "BB"),
         ("Bed & Breakfast Company of Kraków", "Bed & Breakfast")),

        (("Apple Inc.", "The Film Makers of California", "Narodowy Bank Polski", "NBP", "Microsoft", "FM", "TFMoCa"),
         ("Apple Inc.", "The Film Makers of California", "Narodowy Bank Polski", "Microsoft"))
    ]
)
def test_members_abbreviations_removing(names_list: Sequence[str], expected_names_list: Sequence[str]):
    without_redundant_abbreviations = remove_members_abbreviations([parse_name(name) for name in names_list])
    names_without_redundant_abbreviations = [name.source.original for name in without_redundant_abbreviations]
    assert names_without_redundant_abbreviations == list(expected_names_list)


@pytest.mark.parametrize(
    "names_list, valid_terms",
    [
        (("SCB", "Standard Chartered Bank"), ("Siam Commercial Bank",)),
        (("Apple Inc.", "The Walmart", "Narodowy Bank Polski", "NBP", "Microsoft"), ("New Base Project",))
    ],
)
def test_resolving_after_abbreviation_removal(names_list: Sequence[str], valid_terms: Sequence[str]):
    result = CompanyNameAgent().resolve(names_list, valid_terms)
    assert result.solution == Solution.NO_MATCH

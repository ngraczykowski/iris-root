from typing import Mapping

from company_name.names.name_information import NameInformation

from company_name.names.parse_name import parse_name

from company_name.scores.score import Score
from company_name.scores.blacklist import blacklist_score
from company_name.scores.potential_subsidiary import potential_subsidiary_score
from company_name.scores.phonetics import phonetic_score
from company_name.scores.fuzzy import (
    fuzzy_score,
    partial_fuzzy_score,
    sorted_fuzzy_score,
)
from company_name.scores.abbreviation import abbreviation_score
from company_name.scores.parenthesis_match import parenthesis_score
from company_name.scores.tokenization import tokenization_score
from company_name.scores.country import country_score
from company_name.scores.legal import legal_score


def compare_names(
    first: NameInformation, second: NameInformation
) -> Mapping[str, Score]:
    scores = {
        "parenthesis_match": parenthesis_score(first, second),
        "abbreviation": abbreviation_score(first, second),
        "fuzzy_on_base": fuzzy_score(first.base, second.base),
        "fuzzy_on_suffix": fuzzy_score(first.common_suffixes, second.common_suffixes),
        "fuzzy": fuzzy_score(first.name(), second.name()),
        "partial_fuzzy": partial_fuzzy_score(first.name(), second.name()),
        "sorted_fuzzy": sorted_fuzzy_score(first.name(), second.name()),
        "legal_terms": legal_score(first.legal, second.legal),
        "tokenization": tokenization_score(first.name(), second.name()),
        "absolute_tokenization": tokenization_score(
            first.name(), second.name(), absolute=True
        ),
        "blacklisted": blacklist_score(first, second),
        "country": country_score(
            first.countries,
            second.countries,
        ),
        "phonetics_on_base": phonetic_score(first.base, second.base),
        "phonetics": phonetic_score(first.name(), second.name()),
        "potential_subsidiary": potential_subsidiary_score(first, second),
    }

    return scores


def compare(first: str, second: str) -> Mapping[str, Score]:
    return compare_names(parse_name(first), parse_name(second))

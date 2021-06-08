from typing import Mapping

from company_name.names.name_information import NameInformation

from company_name.names.parse.parse import parse_name

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
from company_name.scores.legal_terms import legal_score
from company_name.scores.token_inclusion import token_inclusion_score


def compare_names(
    alerted_name: NameInformation, watchlist_name: NameInformation
) -> Mapping[str, Score]:
    scores = {
        "parenthesis_match": parenthesis_score(alerted_name, watchlist_name),
        "abbreviation": abbreviation_score(alerted_name, watchlist_name),
        "fuzzy_on_base": fuzzy_score(alerted_name.base, watchlist_name.base),
        "fuzzy_on_suffix": fuzzy_score(
            alerted_name.common_suffixes, watchlist_name.common_suffixes
        ),
        "fuzzy": fuzzy_score(alerted_name.name(), watchlist_name.name()),
        "partial_fuzzy": partial_fuzzy_score(
            alerted_name.name(), watchlist_name.name()
        ),
        "sorted_fuzzy": sorted_fuzzy_score(alerted_name.name(), watchlist_name.name()),
        "legal_terms": legal_score(alerted_name.legal, watchlist_name.legal),
        "tokenization": tokenization_score(alerted_name.name(), watchlist_name.name()),
        "absolute_tokenization": tokenization_score(
            alerted_name.name(), watchlist_name.name(), absolute=True
        ),
        "blacklisted": blacklist_score(alerted_name, watchlist_name),
        "country": country_score(
            alerted_name.countries,
            watchlist_name.countries,
        ),
        "phonetics_on_base": phonetic_score(alerted_name.base, watchlist_name.base),
        "phonetics": phonetic_score(alerted_name.name(), watchlist_name.name()),
        "potential_subsidiary": potential_subsidiary_score(
            alerted_name, watchlist_name
        ),
        "token_inclusion": token_inclusion_score(alerted_name, watchlist_name),
    }

    return scores


def compare(alerted_name: str, watchlist_name: str) -> Mapping[str, Score]:
    return compare_names(parse_name(alerted_name), parse_name(watchlist_name))

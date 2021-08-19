from typing import Mapping

from company_name.names.name_information import NameInformation
from company_name.names.parse.parse import parse_name
from company_name.scores.abbreviation import get_abbreviation_score
from company_name.scores.blacklist import get_blacklist_score
from company_name.scores.country import get_country_score
from company_name.scores.first_token import get_first_token_score
from company_name.scores.fuzzy import (
    fuzzy_score,
    get_sorted_fuzzy_score,
    partial_fuzzy_score,
)
from company_name.scores.legal_terms import get_legal_score
from company_name.scores.parenthesis_match import get_parenthesis_score
from company_name.scores.phonetics import get_phonetic_score
from company_name.scores.potential_subsidiary import get_potential_subsidiary_score
from company_name.scores.score import Score
from company_name.scores.token_inclusion import get_token_inclusion_score
from company_name.scores.tokenization import get_tokenization_score


def compare_names(
    alerted_name: NameInformation, watchlist_name: NameInformation
) -> Mapping[str, Score]:
    scores = {
        "parenthesis_match": get_parenthesis_score(alerted_name, watchlist_name),
        "abbreviation": get_abbreviation_score(alerted_name, watchlist_name),
        "fuzzy_on_base": fuzzy_score(alerted_name.base, watchlist_name.base),
        "fuzzy_on_suffix": fuzzy_score(
            alerted_name.common_suffixes, watchlist_name.common_suffixes
        ),
        "fuzzy": fuzzy_score(alerted_name.name(), watchlist_name.name()),
        "partial_fuzzy": partial_fuzzy_score(
            alerted_name.name(), watchlist_name.name()
        ),
        "sorted_fuzzy": get_sorted_fuzzy_score(
            alerted_name.name(), watchlist_name.name()
        ),
        "legal_terms": get_legal_score(alerted_name.legal, watchlist_name.legal),
        "tokenization": get_tokenization_score(
            alerted_name.name(), watchlist_name.name()
        ),
        "absolute_tokenization": get_tokenization_score(
            alerted_name.name(), watchlist_name.name(), absolute=True
        ),
        "blacklisted": get_blacklist_score(alerted_name, watchlist_name),
        "country": get_country_score(
            alerted_name.countries,
            watchlist_name.countries,
        ),
        "phonetics_on_base": get_phonetic_score(alerted_name.base, watchlist_name.base),
        "phonetics": get_phonetic_score(alerted_name.name(), watchlist_name.name()),
        "potential_subsidiary": get_potential_subsidiary_score(
            alerted_name, watchlist_name
        ),
        "token_inclusion": get_token_inclusion_score(alerted_name, watchlist_name),
        "first_token": get_first_token_score(alerted_name, watchlist_name),
    }

    return scores


def compare(alerted_name: str, watchlist_name: str) -> Mapping[str, Score]:
    return compare_names(parse_name(alerted_name), parse_name(watchlist_name))

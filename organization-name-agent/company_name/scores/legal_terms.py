import statistics
from typing import Sequence, Set

from organization_name_knowledge import generate_matching_legal_terms, get_all_legal_terms_meanings
from organization_name_knowledge.knowledge_base.legal_terms import LegalTerm
from organization_name_knowledge.names.name_information import TokensSequence

from company_name.scores.score import Score


def _coverage(terms: Sequence[Sequence[LegalTerm]], all_meanings: Set[str]) -> float:
    scores = []
    for possible_terms in terms:
        values = [
            statistics.mean(meaning in all_meanings for meaning in possible_term.meaning)
            for possible_term in possible_terms
            if possible_term.meaning
        ]
        if values:
            scores.append(max(values))
    return statistics.mean(scores) if scores else 1


def get_legal_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    legal_terms_first = list(generate_matching_legal_terms(first))
    legal_terms_second = list(generate_matching_legal_terms(second))

    appeared_in_first: Set[str] = get_all_legal_terms_meanings(legal_terms_first)
    appeared_in_second: Set[str] = get_all_legal_terms_meanings(legal_terms_second)

    first_coverage: float = (
        _coverage(legal_terms_first, appeared_in_second) if legal_terms_first else 0
    )
    second_coverage: float = (
        _coverage(legal_terms_second, appeared_in_first) if legal_terms_second else 0
    )
    score_value = (
        (first_coverage + second_coverage) / 2 if first_coverage and second_coverage else 0
    )

    return Score(
        value=score_value,
        compared=(first.original_tuple, second.original_tuple),
    )

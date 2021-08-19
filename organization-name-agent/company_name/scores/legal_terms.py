import statistics
from typing import Generator, Sequence, Set

from company_name.knowledge_base import KnowledgeBase
from company_name.knowledge_base.legal_terms import LegalTerm
from company_name.names.name_information import TokensSequence
from company_name.scores.score import Score


def _get_legal_terms(
    values: TokensSequence,
) -> Generator[Sequence[LegalTerm], None, None]:
    for value in values:
        key = tuple(value.cleaned.split())
        if key in KnowledgeBase.legal_terms.source_to_legal_terms:
            yield KnowledgeBase.legal_terms.source_to_legal_terms[key]


def _all_meanings(values: Sequence[Sequence[LegalTerm]]) -> Set[str]:
    return {meaning for value in values for possibility in value for meaning in possibility.meaning}


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

    legal_terms_first = list(_get_legal_terms(first))
    legal_terms_second = list(_get_legal_terms(second))

    appeared_in_first: Set[str] = _all_meanings(legal_terms_first)
    appeared_in_second: Set[str] = _all_meanings(legal_terms_second)

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

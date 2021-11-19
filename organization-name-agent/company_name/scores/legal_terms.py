import statistics
from typing import Generator, Sequence, Set

from organization_name_knowledge import KnowledgeBase, LegalTerm, TokensSequence

from company_name.scores.score import Score


def get_legal_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    legal_terms_first = list(_generate_matching_legal_terms(first))
    legal_terms_second = list(_generate_matching_legal_terms(second))

    appeared_in_first: Set[str] = _get_all_legal_terms_meanings(legal_terms_first)
    appeared_in_second: Set[str] = _get_all_legal_terms_meanings(legal_terms_second)

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


def _generate_matching_legal_terms(
    tokens: TokensSequence,
) -> Generator[Sequence[LegalTerm], None, None]:
    """For given TokensSequence, yielding LegalTerm sequences that match any of tokens"""

    for token in tokens:
        key = tuple(token.cleaned.split())
        if key in KnowledgeBase.legal_terms.source_to_legal_terms:
            yield KnowledgeBase.legal_terms.source_to_legal_terms[key]


def _get_all_legal_terms_meanings(legal_terms_sequence: Sequence[Sequence[LegalTerm]]) -> Set[str]:
    return {
        meaning
        for legal_terms in legal_terms_sequence
        for legal_term in legal_terms
        for meaning in legal_term.meaning
    }

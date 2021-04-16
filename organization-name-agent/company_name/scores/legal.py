import itertools
import statistics
from typing import Sequence, Set, Generator

from company_name.names.legal_terms import LEGAL_TERMS
from company_name.names.name_information import NameSequence
from .score import Score


def _normalized_legal_terms(values: NameSequence) -> Generator[Sequence[str], None, None]:
    for value in values:
        key = tuple(value.cleaned.split())
        yield LEGAL_TERMS.legal_terms_mapping.get(key, key)


def legal_score(first: NameSequence, second: NameSequence) -> Score:
    if not first and not second:
        return Score()

    normalized_first = list(_normalized_legal_terms(first))
    normalized_second = list(_normalized_legal_terms(second))

    appeared_in_first: Set[str] = set(itertools.chain.from_iterable(normalized_first))
    appeared_in_second: Set[str] = set(itertools.chain.from_iterable(normalized_second))

    first_coverage: float = statistics.mean(
        any(possible_name in appeared_in_second for possible_name in legal_term)
        for legal_term in normalized_first
    ) if normalized_first else 0
    second_coverage: float = statistics.mean(
        any(possible_name in appeared_in_first for possible_name in legal_term)
        for legal_term in normalized_second
    ) if normalized_second else 0

    return Score(
        value=(first_coverage + second_coverage) / 2,
        compared=(first.original_tuple, second.original_tuple)
    )

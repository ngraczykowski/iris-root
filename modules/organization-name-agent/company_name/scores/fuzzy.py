from typing import Tuple

import rapidfuzz.fuzz as fuzz
from organization_name_knowledge import TokensSequence

from company_name.scores.score import Score


def _without_whitespaces(*names: TokensSequence) -> Tuple[str, ...]:
    return tuple("".join(name.cleaned_tuple) for name in names)


def _to_compared(*names) -> Tuple[Tuple[str], ...]:
    return tuple((n.original_name,) if n else () for n in names)


def get_fuzzy_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    return Score(
        value=fuzz.ratio(*_without_whitespaces(first, second)) / 100,
        compared=_to_compared(first, second),
    )


def get_partial_fuzzy_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    return Score(
        value=fuzz.partial_ratio(*_without_whitespaces(first, second)) / 100,
        compared=_to_compared(first, second),
    )


def get_sorted_fuzzy_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    return Score(
        value=fuzz.token_sort_ratio(first.cleaned_name, second.cleaned_name) / 100,
        compared=_to_compared(first, second),
    )

from typing import Tuple

import fuzzywuzzy.fuzz

from company_name.names.name_information import TokensSequence
from company_name.scores.score import Score


def _without_whitespaces(*names: TokensSequence) -> Tuple[str, ...]:
    return tuple("".join(name.cleaned_tuple) for name in names)


def _to_compared(*names) -> Tuple[Tuple[str], ...]:
    return tuple((n.original_name,) if n else () for n in names)


def fuzzy_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    return Score(
        value=fuzzywuzzy.fuzz.ratio(*_without_whitespaces(first, second)) / 100,
        compared=_to_compared(first, second),
    )


def partial_fuzzy_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    return Score(
        value=fuzzywuzzy.fuzz.partial_ratio(*_without_whitespaces(first, second)) / 100,
        compared=_to_compared(first, second),
    )


def sorted_fuzzy_score(first: TokensSequence, second: TokensSequence) -> Score:
    if not first and not second:
        return Score()

    return Score(
        value=fuzzywuzzy.fuzz.token_sort_ratio(first, second) / 100,
        compared=_to_compared(first, second),
    )

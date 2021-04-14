from typing import Tuple

import fuzzywuzzy.fuzz

from company_name.names.name_information import NameSequence
from .score import Score


def _without_whitespaces(*names: NameSequence) -> Tuple[str, ...]:
    return tuple("".join(name.cleaned_tuple) for name in names)


def _to_compared(*names):
    return tuple((n.original_name, ) if n else () for n in names)


def fuzzy_score(first: NameSequence, second: NameSequence) -> Score:
    return Score(
        value=fuzzywuzzy.fuzz.ratio(*_without_whitespaces(first, second)) / 100,
        compared=_to_compared(first, second)
    )


def partial_fuzzy_score(first: NameSequence, second: NameSequence) -> Score:
    return Score(
        value=fuzzywuzzy.fuzz.partial_ratio(*_without_whitespaces(first, second)) / 100,
        compared=_to_compared(first, second)
    )


def sorted_fuzzy_score(first: NameSequence, second: NameSequence) -> Score:
    return Score(
        value=fuzzywuzzy.fuzz.token_sort_ratio(first, second) / 100,
        compared=_to_compared(first, second)
    )

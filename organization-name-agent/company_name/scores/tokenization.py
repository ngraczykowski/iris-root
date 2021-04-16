import collections
from typing import List, Iterable, Counter

from company_name.names.special_words import WEAK_WORDS
from company_name.names.name_information import NameSequence
from .score import Score


def _filter_weak_words(names: Iterable[str]) -> List[str]:
    return [n for n in names if n not in WEAK_WORDS]


def _common(first: Counter[str], second: Counter[str]) -> List[str]:
    return _filter_weak_words((first & second).elements())


def _different(first: Counter[str], second: Counter[str]) -> List[str]:
    return _filter_weak_words(((first - second) + (second - first)).elements())


def tokenization_score(
    first_name: NameSequence, second_name: NameSequence, absolute: bool = False
) -> Score:
    if not first_name and not second_name:
        return Score()

    first_tokens: Counter[str] = collections.Counter(first_name.cleaned_tuple)
    second_tokens: Counter[str] = collections.Counter(second_name.cleaned_tuple)

    common = _common(first_tokens, second_tokens)
    different = _different(first_tokens, second_tokens)
    return Score(
        value=len(common) if absolute else len(common) / (len(common) + len(different)),
        compared=(first_name.original_tuple, second_name.original_tuple),
    )

import collections
from typing import Counter, Iterable, List

from company_name.knowledge_base import KnowledgeBase
from company_name.names.name_information import TokensSequence, NameInformation
from company_name.names.parse.extract_information import extract_countries
from company_name.scores.score import Score


def _filter_weak_words(names: Iterable[str]) -> List[str]:
    return [n for n in names if n not in KnowledgeBase.weak_words]


def _common(first: Counter[str], second: Counter[str]) -> List[str]:
    return _filter_weak_words((first & second).elements())


def _different(first: Counter[str], second: Counter[str]) -> List[str]:
    return _filter_weak_words(((first - second) + (second - first)).elements())


def get_tokenization_score(
    first_name_inf: NameInformation, second_name_inf: NameInformation, absolute: bool = False
) -> Score:
    if not first_name_inf and not second_name_inf:
        return Score()
    f_other, f_countries = extract_countries(first_name_inf.other)
    s_other, s_countries = extract_countries(second_name_inf.other)
    first_name = first_name_inf.name() + f_other
    second_name = second_name_inf.name() + s_other
    first_tokens: Counter[str] = collections.Counter(first_name.cleaned_tuple)
    second_tokens: Counter[str] = collections.Counter(second_name.cleaned_tuple)

    common = _common(first_tokens, second_tokens)
    different = _different(first_tokens, second_tokens)
    if not common and not different:
        return Score()

    return Score(
        value=len(common) if absolute else len(common) / (len(common) + len(different)),
        compared=(first_name.original_tuple, second_name.original_tuple),
    )

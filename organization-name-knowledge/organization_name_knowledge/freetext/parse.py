from itertools import combinations
from typing import List

from organization_name_knowledge.freetext.name_matching import get_all_contained_legal_terms
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse.parse import parse_name
from organization_name_knowledge.utils.text import (
    clear_freetext,
    contains_conjunction,
    starts_with_conjunction,
)

NAME_TOKENS_LOWER_LIMIT = 2
NAME_TOKENS_UPPER_LIMIT = 7


def parse_freetext_names(freetext: str, base_tokens_limit: int) -> List[NameInformation]:
    freetext = clear_freetext(freetext).lower()
    freetext_tokens = freetext.split()
    freetext_tokens_number = len(freetext_tokens)
    substrings = [
        " ".join(freetext_tokens[first:last])
        for first, last in combinations(range(freetext_tokens_number + 1), 2)
        if NAME_TOKENS_LOWER_LIMIT <= last - first <= NAME_TOKENS_UPPER_LIMIT
    ]
    parsed_names = [parse_name(substring) for substring in substrings]

    parsed_names_valid = [
        name
        for name in parsed_names
        if name.legal
        and base_tokens_limit
        >= len(name.base)
        != len(get_all_contained_legal_terms(name.base.original_name))
    ]

    return _get_names_with_unique_bases(parsed_names_valid)


def _get_names_with_unique_bases(names: List[NameInformation]) -> List[NameInformation]:
    unique_base_names = []
    bases = []
    for name in sorted(names, key=lambda name_inf: len(name_inf.legal), reverse=True):
        if (
            name.base.cleaned_name not in bases
            and not starts_with_conjunction(name.base.cleaned_name)
            and not contains_conjunction(name.legal.cleaned_tuple)
        ):
            unique_base_names.append(name)
            bases.append(name.base.cleaned_name)

    return unique_base_names

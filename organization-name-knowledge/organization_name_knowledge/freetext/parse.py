from itertools import combinations
from typing import List

from organization_name_knowledge.freetext.matching import get_all_contained_legal_terms
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse.parse import parse_name
from organization_name_knowledge.utils.term_variants import get_text_variants
from organization_name_knowledge.utils.text import (
    clear_freetext,
    contains_conjunction,
    starts_with_conjunction,
)


def parse_freetext_names(
    freetext: str,
    base_tokens_upper_limit: int = 3,
    name_tokens_lower_limit: int = 2,
    name_tokens_upper_limit: int = 7,
) -> List[NameInformation]:

    freetext = clear_freetext(freetext).lower()
    freetext_names = []

    for freetext_variant in get_text_variants(freetext):
        tokens = freetext_variant.split()

        substrings = [
            " ".join(tokens[first:last])
            for first, last in combinations(range(len(tokens) + 1), 2)
            if name_tokens_lower_limit <= last - first <= name_tokens_upper_limit
        ]
        parsed_names = [parse_name(substring) for substring in substrings]

        parsed_names_valid = _get_valid_names(parsed_names, base_tokens_upper_limit)
        freetext_names.extend(parsed_names_valid)

    return _get_names_with_unique_bases(freetext_names)


def _get_valid_names(
    parsed_names: List[NameInformation], base_tokens_upper_limit: int
) -> List[NameInformation]:
    parsed_names_valid = [
        name
        for name in parsed_names
        if name.legal
        and len(get_all_contained_legal_terms(name.base.original_name))
        != len(name.base)
        <= base_tokens_upper_limit
    ]
    if len(parsed_names_valid) >= 2:
        parsed_names_valid = [
            name
            for name in parsed_names_valid
            if not name.base.cleaned_name.startswith(name.legal.cleaned_name)
        ]
    return parsed_names_valid


def _get_names_with_unique_bases(names: List[NameInformation]) -> List[NameInformation]:
    unique_base_names = []
    bases = []
    for name in sorted(names, key=lambda name_inf: len(name_inf.legal), reverse=True):
        if (
            name.base.cleaned_name not in bases
            and not starts_with_conjunction(name.base.cleaned_tuple)
            and not contains_conjunction(name.legal.cleaned_tuple)
        ):
            unique_base_names.append(name)
            bases.append(name.base.cleaned_name)

    return unique_base_names

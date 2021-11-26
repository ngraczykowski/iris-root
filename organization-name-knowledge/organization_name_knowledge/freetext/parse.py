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
    found_valid_names = []

    for freetext_variant in get_text_variants(freetext):
        tokens = freetext_variant.split()

        substrings = [
            " ".join(tokens[first:last])
            for first, last in combinations(range(len(tokens) + 1), 2)
            if name_tokens_lower_limit <= last - first <= name_tokens_upper_limit
        ]
        parsed_names = [parse_name(substring) for substring in substrings]

        parsed_names_valid = _get_valid_names(parsed_names, base_tokens_upper_limit)
        found_valid_names.extend(parsed_names_valid)

    names_to_remove = _get_names_to_remove(found_valid_names)
    found_valid_names = [name for name in found_valid_names if name not in names_to_remove]
    return _get_names_with_unique_bases(found_valid_names)


def _get_valid_names(
    names: List[NameInformation], base_tokens_upper_limit: int
) -> List[NameInformation]:
    valid_names = [
        name
        for name in names
        if name.legal
        and len(get_all_contained_legal_terms(name.base.original_name))
        != len(name.base)
        <= base_tokens_upper_limit
        and not (
            name.base.cleaned_name.startswith(name.legal.cleaned_name) and len(name.legal) >= 2
        )  # to remove names with 2 or more legals at start
    ]

    return valid_names


def _get_names_to_remove(names: List[NameInformation]) -> List[NameInformation]:
    names_with_start_legal = []
    end_legals = set()
    for name in names:
        source, base, legal = name.source.cleaned, name.base.cleaned_name, name.legal.cleaned_name

        if base.endswith(legal) or source.endswith(legal):
            end_legals.add(legal)
            end_legals.add(name.legal.cleaned_tuple[-1])  # to check last token only also

        elif base.startswith(legal) or source.startswith(legal):
            names_with_start_legal.append(name)

    names_with_duplicated_legal = [
        name
        for name in names_with_start_legal
        if name.legal.cleaned_name in end_legals
        or name.legal.cleaned_tuple[0] in end_legals  # check if just first token in ends
    ]
    names_with_duplicated_legal_from_prefix = [
        name
        for name in names
        if name.common_prefixes.cleaned_name in end_legals
        or name.base.cleaned_tuple[0] in end_legals
    ]
    return names_with_duplicated_legal + names_with_duplicated_legal_from_prefix


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

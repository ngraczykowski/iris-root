from itertools import combinations
from typing import List, Set

from organization_name_knowledge.freetext.matching import (
    get_all_contained_legal_terms,
    get_names_from_org_name_markers,
)
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse.parse import parse_name
from organization_name_knowledge.utils.text import (
    PREPOSITIONS,
    alpha_char_count,
    clear_freetext,
    contains_conjunction,
    starts_with_conjunction,
    starts_with_preposition,
)
from organization_name_knowledge.utils.variants import get_text_variants


def parse_freetext_names(
    freetext: str,
    base_tokens_upper_limit: int = 3,
    name_tokens_lower_limit: int = 2,
    name_tokens_upper_limit: int = 7,
) -> List[NameInformation]:

    found_valid_names = []

    for freetext_variant in get_text_variants(freetext):

        freetext_variant = clear_freetext(freetext_variant)
        freetext_variant_tokens = freetext_variant.split()

        substrings = [
            " ".join(freetext_variant_tokens[first:last])
            for first, last in combinations(range(len(freetext_variant_tokens) + 1), 2)
            if name_tokens_lower_limit <= last - first <= name_tokens_upper_limit
        ]
        parsed_names = [parse_name(substring) for substring in substrings]

        parsed_names_valid = _get_valid_names(parsed_names, base_tokens_upper_limit)
        found_valid_names.extend(parsed_names_valid)
        found_valid_names.extend(get_names_from_org_name_markers(freetext_variant_tokens))
    names_to_remove = _get_names_to_remove(found_valid_names)
    found_valid_names = [name for name in found_valid_names if name not in names_to_remove]
    unique_base_names = _get_names_with_unique_bases(found_valid_names)
    return sorted(set(unique_base_names), key=lambda name: name.base.cleaned_name)


def _get_valid_names(
    names: List[NameInformation], base_tokens_upper_limit: int
) -> List[NameInformation]:
    """Rules to met for name to be valid:
    - must contain at least one legal term
    - alpha characters number in base must be at least 2
    - number of base tokens does not exceed the base_tokens_upper_limit
    - base tokens must not be legal terms only
    - if the number of legal terms is 2 or more, it can not appear at the start of the name
    """

    valid_names = [
        name
        for name in names
        if name.legal
        and alpha_char_count(name.base.cleaned_name) >= 2
        and len(get_all_contained_legal_terms(name.base.original_name))
        != len(name.base)
        <= base_tokens_upper_limit
        and not (
            name.source.cleaned.startswith(name.legal.cleaned_name)
            and len([legal for legal in name.legal if legal not in PREPOSITIONS]) >= 2
        )  # to remove names with 2 or more legals at start
    ]

    return valid_names


def _get_names_to_remove(names: List[NameInformation]) -> Set[NameInformation]:
    """If 2 or more names with the same legal(s) appear, use the one with legal
    at the end of a name, not at start - so the rest should go to the 'to remove' set.
    If any of the legal terms becomes a prefix, a name with that prefix should be added
    to 'to remove' set also.
    """

    names_with_start_legal = []
    end_legals = set()
    for name in names:
        source, base, legal = name.source.cleaned, name.base.cleaned_name, name.legal.cleaned_name
        if legal:
            if base.endswith(legal) or source.endswith(legal):
                end_legals.add(legal)
                end_legals.add(name.legal.cleaned_tuple[-1])  # to check last token only also

            elif (
                base.startswith(legal)
                or source.startswith(legal)
                or source.startswith(name.legal.cleaned_tuple[0])
            ):
                names_with_start_legal.append(name)

    names_with_duplicated_legal = {
        name
        for name in names_with_start_legal
        if name.legal.cleaned_name in end_legals
        or name.legal.cleaned_tuple[0] in end_legals  # check if just first token in ends
    }
    names_with_duplicated_legal_from_prefix = {
        name
        for name in names
        if name.common_prefixes.cleaned_name in end_legals
        or name.base.cleaned_tuple[0] in end_legals
    }
    names_with_duplicated_legal.update(names_with_duplicated_legal_from_prefix)
    return names_with_duplicated_legal


def _get_names_with_unique_bases(names: List[NameInformation]) -> List[NameInformation]:
    unique_base_names = []
    bases = []
    names_sorted_by_source_len = sorted(names, key=lambda name_inf: len(name_inf.source.cleaned))
    for name in sorted(
        names_sorted_by_source_len,
        key=lambda name_inf: (len(name_inf.legal), len(name_inf.common_prefixes)),
        reverse=True,
    ):
        if (
            name.base.cleaned_name not in bases
            and not starts_with_conjunction(name.base.cleaned_tuple)
            and not starts_with_preposition(name.base.cleaned_tuple)
            and not contains_conjunction(name.legal.cleaned_tuple)
        ):
            unique_base_names.append(name)
            bases.append(name.base.cleaned_name)

    return unique_base_names

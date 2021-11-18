from typing import List

from organization_name_knowledge.freetext.name_matching import (
    cut_name_to_leftmost_match,
    get_all_contained_legal_terms,
)
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse.parse import parse_name
from organization_name_knowledge.utils.clear_name import clear_freetext
from organization_name_knowledge.utils.term_variants import get_name_variants


def parse_freetext_names(freetext: str, tokens_limit: int) -> List[NameInformation]:
    freetext = clear_freetext(freetext)
    names_with_legals = [
        (name, get_all_contained_legal_terms(name)) for name in get_name_variants(freetext.lower())
    ]
    cut_names = {
        cut_name_to_leftmost_match(name, legal_terms)
        for name, legal_terms in names_with_legals
        if legal_terms
    }
    parsed_names = [parse_name(name) for name in cut_names]
    parsed_names_filtered = list(filter(lambda name: len(name.base) <= tokens_limit, parsed_names))
    names_from_long_names = _get_long_names_substrings_based_names(parsed_names_filtered)
    names = parsed_names_filtered + names_from_long_names
    _filter_duplicate_bases(names)
    return names


def _get_long_names_substrings_based_names(names: List[NameInformation]) -> List[NameInformation]:
    names_from_substrings = []
    for name_information in names:
        base_tokens_number = len(name_information.base)
        if base_tokens_number >= 2:
            _add_names_from_name_substrings(
                base_tokens_number, name_information, names_from_substrings
            )
    return [parse_name(name) for name in names_from_substrings]


def _add_names_from_name_substrings(
    tokens_number: int, name_information: NameInformation, names_from_substrings: List[str]
):
    for index in range(1, tokens_number):
        replaced = name_information.source.original
        for token_from_first_text_part in name_information.base.original_tuple[:index]:
            replaced = replaced.replace(token_from_first_text_part + " ", "")
        names_from_substrings.append(replaced.strip())


def _filter_duplicate_bases(names: List[NameInformation]):
    bases = []
    for name in names:
        if name.base in bases:
            names.remove(name)
        else:
            bases.append(name.base)

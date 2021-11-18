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
    freetext = clear_freetext(freetext).lower()
    names_to_their_legals = [
        (name, get_all_contained_legal_terms(name)) for name in get_name_variants(freetext)
    ]
    names_cut_to_leftmost_legals = {
        cut_name_to_leftmost_match(name, legal_terms)
        for name, legal_terms in names_to_their_legals
        if legal_terms
    }
    parsed_names = [parse_name(name) for name in names_cut_to_leftmost_legals]
    parsed_names_proper_length = [name for name in parsed_names if len(name.base) <= tokens_limit]

    names_from_long_names = _get_long_names_substrings_based_names(parsed_names_proper_length)
    names = parsed_names_proper_length + names_from_long_names
    _remove_names_with_duplicated_bases(names)
    return names


def _get_long_names_substrings_based_names(names: List[NameInformation]) -> List[NameInformation]:
    """For each name information, produces all consecutive shorter name-based name information list
    i.e. for 'Silent Eight Pte Ltd' which base is 'Silent Eight' and legal 'Pte Ltd',
    we want to produce also another one, with base 'Eight' and the same legal 'Pte Ltd'
    This is needed to properly check all variations produced from freetext
    """
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


def _remove_names_with_duplicated_bases(names: List[NameInformation]):
    bases = []
    for name in names:
        if name.base in bases:
            names.remove(name)
        else:
            bases.append(name.base)

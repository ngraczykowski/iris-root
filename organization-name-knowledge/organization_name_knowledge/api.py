from itertools import combinations
from typing import Set

from organization_name_knowledge.knowledge_base.knowledge_base import KnowledgeBase
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse import create_tokens, parse_name


def parse(name: str) -> NameInformation:
    """
    Parser for organization names, to extract base organization name, and its surrounding:
    legal terms, country names, prefixes, suffixes, parentheses.
    For details, check out *NameInformation* class docstrings.

    :param name: string, in main use case it is an organization name
    :return: NameInformation class object
    """
    name_information = parse_name(name)
    return name_information


def get_all_legal_terms(name: str) -> Set[str]:
    """
    Legal terms returned by this function are slightly different than those returned by main
    "parse" function - they may come from i.e. part of the name, or from inside the name string.
    In other words, they are all of the substrings that match any of known legal terms.

    :param name: string, in main use case it is an organization name
    :return: Set of name substrings that match any of known legal terms
    """
    name_tokens = create_tokens(name)
    legal_term_sources = KnowledgeBase.legal_terms.legal_term_sources

    found_legal_terms = set()
    for first_index, last_index in combinations(range(len(name_tokens) + 1), 2):
        name_tokens_substring = name_tokens[first_index:last_index]

        if name_tokens_substring.cleaned_tuple in legal_term_sources:
            found_legal_terms.add(name_tokens_substring.original_name)

    return found_legal_terms

from itertools import combinations
from typing import Generator, List, Sequence, Set

from organization_name_knowledge.knowledge_base.knowledge_base import KnowledgeBase
from organization_name_knowledge.knowledge_base.legal_terms import LegalTerm
from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse import create_tokens, parse_name
from organization_name_knowledge.names.tokens_sequence import TokensSequence
from organization_name_knowledge.utils import cut_name_to_leftmost_match
from organization_name_knowledge.utils.clear_name import split_text_by_too_long_numbers
from organization_name_knowledge.utils.term_variants import get_name_variants


def parse(name: str) -> NameInformation:
    """Parse organization name, to extract base of name, and its surrounding:
    legal terms, country names, prefixes, suffixes, parentheses.
    For details, check out *NameInformation* class docstrings.

    Parameters
    ----------
    name : str
        In most of use cases it is an organization name

    Returns
    -------
    NameInformation
        NameInformation class object
    """

    name_information = parse_name(name)
    return name_information


def parse_freetext(freetext: str, tokens_limit: int = 5) -> List[NameInformation]:
    """Parse freetext to find each organization name that is present within passed text

    Parameters
    ----------
    freetext : str
        Any string which contains or not, an organization name(s)
    tokens_limit : int
        The maximum number of tokens in parsed organization name(s)

    Returns
    -------
    List[NameInformation]
        A list of found and parsed organization names, as NameInformation objects
    """
    freetext_cleaned = split_text_by_too_long_numbers(freetext)
    names_with_legals = [
        (name, get_all_legal_terms(name)) for name in get_name_variants(freetext_cleaned.lower())
    ]
    cut_names = {
        cut_name_to_leftmost_match(name, legal_terms)
        for name, legal_terms in names_with_legals
        if legal_terms
    }

    parsed_names = [parse(name) for name in sorted(cut_names)]
    parsed_names_filtered = list(filter(lambda name: len(name.base) <= tokens_limit, parsed_names))
    return parsed_names_filtered


def get_all_legal_terms(name: str) -> Set[str]:
    """Legal terms returned by this function are slightly different than those returned by main
    "parse" function - they may come from i.e. part of the name, or from inside the name string.
    In other words, they are all of the substrings that match any of known legal terms.

    Parameters
    ----------
    name : str
        In most of use cases it is an organization name

    Returns
    -------
    Set[str]
        Set of name substrings that match any of known legal terms
    """

    legal_term_sources = KnowledgeBase.legal_terms.legal_term_sources
    return _get_matching_tokens(name, legal_term_sources)


def _get_matching_tokens(name: str, term_sources: TermSources) -> Set[str]:
    """For given name, produces all substrings, i. e.:
    "A B C" -> "A", "B", "C" ,"A B", "B C", "A B C"
    Then producing a set of all these that are present in given term_sources

    Parameters
    ----------
    name : str
        In most of use cases it is an organization name
    term_sources : TermSources
        TermSources object, containing set of known terms to check name tokens matching

    Returns
    -------
    Set[str]
        Set with all name tokens subsets, that match any of given term_sources
    """
    name_tokens = create_tokens(name)
    found_tokens = set()

    for first_index, last_index in combinations(range(len(name_tokens) + 1), 2):
        name_tokens_subset = name_tokens[first_index:last_index]

        if name_tokens_subset.cleaned_tuple in term_sources:
            found_tokens.add(name_tokens_subset.original_name)
    return found_tokens


# functions from scores


def generate_matching_legal_terms(
    tokens: TokensSequence,
) -> Generator[Sequence[LegalTerm], None, None]:
    """For given TokensSequence, yielding LegalTerm sequences that match any of tokens"""
    for token in tokens:
        key = tuple(token.cleaned.split())
        if key in KnowledgeBase.legal_terms.source_to_legal_terms:
            yield KnowledgeBase.legal_terms.source_to_legal_terms[key]

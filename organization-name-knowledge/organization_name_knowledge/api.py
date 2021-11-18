from typing import Generator, List, Sequence, Set

from organization_name_knowledge.freetext.freetext import parse_freetext_names
from organization_name_knowledge.freetext.name_matching import get_matching_tokens
from organization_name_knowledge.knowledge_base.knowledge_base import KnowledgeBase
from organization_name_knowledge.knowledge_base.legal_terms import LegalTerm
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse import parse_name
from organization_name_knowledge.names.tokens_sequence import TokensSequence


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

    names: List[NameInformation] = parse_freetext_names(freetext, tokens_limit)
    return sorted(set(names), key=lambda name: name.base.cleaned_name)


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
    return get_matching_tokens(name, legal_term_sources)


# functions from scores


def generate_matching_legal_terms(
    tokens: TokensSequence,
) -> Generator[Sequence[LegalTerm], None, None]:
    """For given TokensSequence, yielding LegalTerm sequences that match any of tokens"""
    for token in tokens:
        key = tuple(token.cleaned.split())
        if key in KnowledgeBase.legal_terms.source_to_legal_terms:
            yield KnowledgeBase.legal_terms.source_to_legal_terms[key]

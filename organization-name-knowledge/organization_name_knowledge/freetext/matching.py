from itertools import combinations
from typing import Set

from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.parse import create_tokens


def get_all_contained_legal_terms(text: str) -> Set[str]:
    legal_term_sources = KnowledgeBase.legal_terms.legal_term_sources
    return _get_matching_tokens(text, legal_term_sources)


def _get_matching_tokens(text: str, term_sources: TermSources) -> Set[str]:
    """For given name, produces all substrings, i.e.:
    "A B C" -> "A", "B", "C" ,"A B", "B C", "A B C"
    Then producing a set of all these that are present in given term_sources

    Parameters
    ----------
    text : str
        A string which may contain a terms from term sources
    term_sources : TermSources
        An object containing set of known terms to check tokens matching in text

    Returns
    -------
    Set[str]
        Set with all tokens subsets, that match any of given term_sources
    """
    tokens = create_tokens(text)
    found_tokens = set()

    for first_index, last_index in combinations(range(len(tokens) + 1), 2):
        tokens_subset = tokens[first_index:last_index]

        if tokens_subset.cleaned_tuple in term_sources:
            found_tokens.add(tokens_subset.original_name)
    return found_tokens

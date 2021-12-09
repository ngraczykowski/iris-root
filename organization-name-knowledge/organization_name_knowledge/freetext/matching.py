from itertools import combinations
from typing import List, Set, Tuple

from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse import create_tokens, parse_name
from organization_name_knowledge.utils.text import PREPOSITIONS, is_dependent_token


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


def get_names_from_org_name_markers(tokens: List[str]) -> List[NameInformation]:
    """Check if in tokens is present a member of organization name markers, such as 'bank'
    or 'group',which are not legal terms, but indicates an organization name. When a marker
    is found, get lefts (-1, -2) and rights (+2) tokens combined with marker, and then check
    if respective name is in a proper form and if yes, parse it as a NameInformation.
    Note that tokens must be lowercase!
    """

    tokens_num = len(tokens)
    org_name_candidates = []
    for idx, token in enumerate(tokens):
        if token in KnowledgeBase.markers:
            lefts_2, lefts_1 = (idx - 2, idx - 1, idx), (idx - 1, idx)
            org_name_candidates.append(_get_lefts_candidate(lefts_2, tokens, tokens_num))
            org_name_candidates.append(_get_lefts_candidate(lefts_1, tokens, tokens_num))

            rights_2 = (idx, idx + 1, idx + 2)
            org_name_candidates.append(_get_rights_candidate(rights_2, tokens, tokens_num))
    return [candidate for candidate in org_name_candidates if candidate]


def _get_lefts_candidate(
    indexes: Tuple[int, ...], tokens: List[str], tokens_num: int
) -> NameInformation:
    if all(_range_met(idx, tokens_num) for idx in indexes):
        if not is_dependent_token(tokens[indexes[-2]]):  # to avoid matches like "XYZ about group"
            candidate = " ".join((tokens[index] for index in indexes))
            return parse_name(candidate)


def _get_rights_candidate(
    indexes: Tuple[int, int, int], tokens: List[str], tokens_num: int
) -> NameInformation:
    if (
        all(_range_met(idx, tokens_num) for idx in indexes)
        and tokens[indexes[1]] in PREPOSITIONS  # to find only names like: "Bank of Scotland"
        and not is_dependent_token(tokens[indexes[2]])
    ):
        candidate = " ".join((tokens[index] for index in indexes))
        return parse_name(candidate)


def _range_met(index: int, range_upper_limit: int) -> bool:
    return 0 <= index < range_upper_limit

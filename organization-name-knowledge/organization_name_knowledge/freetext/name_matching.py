from itertools import combinations
from typing import Dict, Set

from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.parse import create_tokens


def cut_name_to_leftmost_match(name: str, matches: Set[str]) -> str:
    def get_match_last_index(match: Dict):
        return 1 + match["idx"] + len(match["match"])  # 1 because of leading " " cutting

    matches = filter(lambda match: match in name, matches)  # to avoid using no-matches
    match_to_index = [{"match": match, "idx": name.find(" " + match)} for match in matches]

    if not match_to_index:
        return name

    sorted_matches = sorted(match_to_index, key=lambda x: (x["idx"], len(x["match"])))

    if len(sorted_matches) == 1:
        leftmost_legal_sequence_last_char_index = get_match_last_index(sorted_matches[0])

    # two consecutive legal terms - take the last index of the last one
    elif sorted_matches[1]["idx"] - get_match_last_index(sorted_matches[0]) <= 2:
        leftmost_legal_sequence_last_char_index = get_match_last_index(sorted_matches[1])

    else:
        leftmost_legal_sequence_last_char_index = get_match_last_index(sorted_matches[0])

    return name[:leftmost_legal_sequence_last_char_index]


def get_matching_tokens(name: str, term_sources: TermSources) -> Set[str]:
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

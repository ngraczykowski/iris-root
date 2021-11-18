import dataclasses
from itertools import combinations
from typing import Set, Tuple

from organization_name_knowledge.names.parse import create_tokens


@dataclasses.dataclass
class TermSources:
    terms: Set[Tuple[str, ...]] = dataclasses.field(default_factory=set)
    max_term_length: int = None

    def __post_init__(self):
        if not self.max_term_length:
            self.max_term_length = max(len(t) for t in self.terms) if self.terms else 0

    def __add__(self, other: "TermSources") -> "TermSources":
        return TermSources(
            {*self.terms, *other.terms},
            max_term_length=max(self.max_term_length, other.max_term_length),
        )

    def __sub__(self, other: "TermSources") -> "TermSources":
        return TermSources(
            self.terms.difference(other.terms),
            max_term_length=self.max_term_length
            if self.max_term_length > other.max_term_length
            else None,
        )

    def __contains__(self, item):
        return item in self.terms


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
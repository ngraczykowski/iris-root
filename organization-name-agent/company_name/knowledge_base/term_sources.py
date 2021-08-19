import dataclasses
from typing import Set, Tuple


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

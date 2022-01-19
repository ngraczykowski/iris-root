import dataclasses
import enum
from typing import Mapping, Optional, Sequence

from company_name.scores.score import Score


class Solution(enum.Enum):
    MATCH = "MATCH"
    INCONCLUSIVE = "INCONCLUSIVE"
    NO_MATCH = "NO_MATCH"
    NO_DATA = "NO_DATA"
    AGENT_ERROR = "AGENT_ERROR"

    def __lt__(self, other: "Solution") -> bool:
        order = list(self.__class__.__members__)
        return order.index(self.name) > order.index(other.name)

    def __eq__(self, other):
        if isinstance(other, Solution):
            return self.value == other.value
        if isinstance(other, str):
            return self.value == other
        return NotImplementedError


@dataclasses.dataclass
class PairResult:
    solution: Solution
    solution_probability: Optional[float]
    scores: Mapping[str, Score]
    alerted_party_name: str
    watchlist_party_name: str

    def __lt__(self, other: "PairResult") -> bool:
        return (self.solution, self.solution_probability or 0) < (
            other.solution,
            other.solution_probability or 0,
        )


@dataclasses.dataclass
class Reason:
    results: Sequence[PairResult] = ()


@dataclasses.dataclass
class Result:
    solution: Solution
    reason: Reason = Reason()

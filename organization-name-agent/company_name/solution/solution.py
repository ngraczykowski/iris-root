import dataclasses
import enum
from typing import Optional, Sequence, Tuple


class Solution(enum.Enum):
    MATCH = "MATCH"
    INCONCLUSIVE = "INCONCLUSIVE"
    NO_MATCH = "NO_MATCH"
    NO_DATA = "NO_DATA"
    UNEXPECTED_ERROR = "UNEXPECTED_ERROR"

    def __lt__(self, other: "Solution") -> bool:
        order = list(self.__class__.__members__)
        return order.index(self.name) > order.index(other.name)


@dataclasses.dataclass
class PairResult:
    solution: Solution
    solution_probability: Optional[float]
    names: Tuple[str, str]

    def __lt__(self, other: "PairResult") -> bool:
        return (self.solution, self.solution_probability or 0) < (
            other.solution,
            other.solution_probability or 0,
        )


@dataclasses.dataclass
class Reason:
    partials: Sequence[PairResult] = ()


@dataclasses.dataclass
class Result:
    solution: Solution
    reason: Reason = Reason()

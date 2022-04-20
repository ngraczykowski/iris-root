import dataclasses
import enum
from typing import List


class Solution(enum.Enum):
    NORMAL = "NORMAL"
    INCONCLUSIVE = "INCONCLUSIVE"
    SCATTER = "SCATTER"
    ENTITY_TYPE_MISMATCH = "ENTITY_TYPE_MISMATCH"
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
class Result:
    solution: Solution
    hit_categories: List[str] = dataclasses.field(default_factory=list)
    normal_categories: List[str] = dataclasses.field(default_factory=list)

    def __eq__(self, other: "Result") -> bool:
        return (
            self.solution == other.solution
            and sorted(self.hit_categories) == sorted(other.hit_categories)
            and sorted(self.normal_categories) == sorted(other.normal_categories)
        )

import dataclasses
from enum import Enum


class Solution(Enum):
    MATCH = "MATCH"
    NO_MATCH = "NO_MATCH"
    AGENT_ERROR = "AGENT_ERROR"


@dataclasses.dataclass
class Result:
    solution: Solution
    count: int

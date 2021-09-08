import dataclasses
from enum import Enum
from typing import Sequence


class Solution(Enum):
    NO_DECISION = "NO_DECISION"
    MATCH = "MATCH"
    NO_MATCH = "NO_MATCH"
    AGENT_ERROR = "AGENT_ERROR"


class Reason:
    conclusion: str = None
    matching_text: str = None
    raw_matched_sequence: str = None
    matching_field: str = None
    partial_match: str = None
    watchlist_type: str = None
    search_codes: Sequence[str] = None
    bic_code: str = None

    def __post_init__(self):
        self.conclusion = self.__class__.__name__


@dataclasses.dataclass
class Result:
    solution: Solution
    reason: Reason = Reason()

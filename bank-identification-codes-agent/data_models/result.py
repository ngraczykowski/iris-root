import dataclasses
from enum import Enum
from typing import Optional


class Solution(Enum):
    NO_DECISION = "NO_DECISION"
    MATCH = "MATCH"
    NO_MATCH = "NO_MATCH"
    AGENT_ERROR = "AGENT_ERROR"


@dataclasses.dataclass
class Reason:
    conclusion: Optional[str] = dataclasses.field(init=False)
    # altered_party_matching_sequence: str = None
    # altered_party_matching_field: str = None
    # partial_match_text: str = None
    # watchlist_matching_text: str = None
    # watchlist_type: str = None
    # watchlist_search_codes: Sequence[str] = None
    # watchlist_bic_code: str = None

    def __post_init__(self):
        self.conclusion = self.__class__.__name__


@dataclasses.dataclass
class Result:
    solution: Solution
    reason: Reason = Reason()

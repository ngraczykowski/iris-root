from enum import Enum
from typing import NewType, Union, List

from attr import attrs, attrib


@attrs(frozen=True)
class SearchCodeMismatchAgentInput:
    message_type: str = attrib()
    message_tag: str = attrib()
    matching_field: str = attrib()
    matching_text: str = attrib()
    wl_type: str = attrib()
    wl_search_codes: str = attrib()


class Result(Enum):
    NO_DECISION = "NO_DECISION"
    MATCH = "MATCH"
    NO_MATCH = "NO_MATCH"
    AGENT_ERROR = "AGENT_ERROR"


@attrs(frozen=True)
class NoSearchCodeInWatchlistReason:
    pass


@attrs(frozen=True)
class MatchingTextTooShortToBeCodeReason:
    matching_text: str = attrib()


@attrs(frozen=True)
class MatchingTextMatchesWlSearchCodeReason:
    matching_text: str = attrib()
    search_code: str = attrib()
    wl_type: str = attrib()


@attrs(frozen=True)
class MatchingTextIsPartOfLongerSequenceReason:
    matching_text: str = attrib()
    raw_matched_sequence: str = attrib()
    matching_field: str = attrib()
    partial_match: str = attrib()
    wl_type: str = attrib()


@attrs(frozen=True)
class MatchingTextIsOnlyPartialMatchForSearchCodeReason:
    matching_text: str = attrib()
    search_code: str = attrib()
    wl_type: str = attrib()


@attrs(frozen=True)
class MatchingTextDoesNotMatchWlSearchCodeReason:
    matching_text: str = attrib()
    search_codes: List[str] = attrib()
    wl_type: str = attrib()


@attrs(frozen=True)
class MatchingTextDoesNotMatchMatchingFieldReason:
    matching_text: str = attrib()
    matching_field: str = attrib()


Reason = NewType('Reason', Union[NoSearchCodeInWatchlistReason,
                                 MatchingTextTooShortToBeCodeReason,
                                 MatchingTextMatchesWlSearchCodeReason,
                                 MatchingTextIsPartOfLongerSequenceReason,
                                 MatchingTextDoesNotMatchMatchingFieldReason])

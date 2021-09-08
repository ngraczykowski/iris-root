from typing import List

from attr import attrs, attrib


from idmismatchagent.result import Reason


@attrs(frozen=True)
class SearchCodeMismatchAgentInput:
    matching_field: str = attrib()
    matching_text: str = attrib()
    watchlist_type: str = attrib()
    watchlist_search_codes: list = attrib()
    watchlist_bic_codes: list = attrib()


@attrs(frozen=True)
class NoSearchCodeInWatchlistReason(Reason):
    pass


@attrs(frozen=True)
class MatchingTextTooShortToBeCodeReason(Reason):
    matching_text: str = attrib()


@attrs(frozen=True)
class MatchingTextMatchesWlSearchCodeReason(Reason):
    matching_text: str = attrib()
    search_codes: List[str] = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextMatchesWlBicCodeReason(Reason):
    matching_text: str = attrib()
    bic_code: str = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextIsPartOfLongerSequenceReason(Reason):
    matching_text: str = attrib()
    matched_sequence: str = attrib()
    matching_field: str = attrib()
    partial_match: str = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextIsOnlyPartialMatchForSearchCodeReason(Reason):
    matching_text: str = attrib()
    search_codes: List[str] = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextDoesNotMatchWlSearchCodeReason(Reason):
    matching_text: str = attrib()
    search_codes: List[str] = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextDoesNotMatchMatchingFieldReason(Reason):
    matching_text: str = attrib()
    matching_field: str = attrib()


# some comment to avoid this newline trouble

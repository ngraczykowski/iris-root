from typing import List

from attr import attrs, attrib


from idmismatchagent.result import Reason


@attrs(frozen=True)
class SearchCodeMismatchAgentInput:
    altered_party_matching_field: str = attrib()
    watchlist_matching_text: str = attrib()
    watchlist_type: str = attrib()
    watchlist_search_codes: list = attrib()
    watchlist_bic_codes: list = attrib()


@attrs(frozen=True)
class NoSearchCodeInWatchlistReason(Reason):
    pass


@attrs(frozen=True)
class MatchingTextTooShortToBeCodeReason(Reason):
    watchlist_matching_text: str = attrib()


@attrs(frozen=True)
class MatchingTextMatchesWlSearchCodeReason(Reason):
    watchlist_matching_text: str = attrib()
    watchlist_search_codes: List[str] = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextMatchesWlBicCodeReason(Reason):
    watchlist_matching_text: str = attrib()
    watchlist_bic_code: str = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextIsPartOfLongerSequenceReason(Reason):
    watchlist_matching_text: str = attrib()
    altered_party_matching_sequence: str = attrib()
    altered_party_matching_field: str = attrib()
    partial_match_text: str = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextIsOnlyPartialMatchForSearchCodeReason(Reason):
    watchlist_matching_text: str = attrib()
    watchlist_search_codes: List[str] = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextDoesNotMatchWlSearchCodeReason(Reason):
    watchlist_matching_text: str = attrib()
    watchlist_search_codes: List[str] = attrib()
    watchlist_type: str = attrib()


@attrs(frozen=True)
class MatchingTextDoesNotMatchMatchingFieldReason(Reason):
    watchlist_matching_text: str = attrib()
    altered_party_matching_field: str = attrib()

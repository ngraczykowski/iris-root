import dataclasses
from typing import List

from data_models.result import Reason


class NoSearchCodeInWatchlistReason(Reason):
    pass


@dataclasses.dataclass
class MatchingTextTooShortToBeCodeReason(Reason):
    watchlist_matching_text: str


@dataclasses.dataclass
class MatchingTextMatchesWlSearchCodeReason(Reason):
    watchlist_matching_text: str
    watchlist_search_codes: List[str]
    watchlist_type: str


@dataclasses.dataclass
class MatchingTextMatchesWlBicCodeReason(Reason):
    watchlist_matching_text: str
    watchlist_bic_code: str
    watchlist_type: str


@dataclasses.dataclass
class MatchingTextIsPartOfLongerSequenceReason(Reason):
    watchlist_matching_text: str
    altered_party_matching_sequence: str
    altered_party_matching_field: str
    partial_match_text: str
    watchlist_type: str


@dataclasses.dataclass
class MatchingTextIsOnlyPartialMatchForSearchCodeReason(Reason):
    watchlist_matching_text: str = None
    watchlist_search_codes: List[str] = None
    watchlist_type: str = None


@dataclasses.dataclass
class MatchingTextDoesNotMatchWlSearchCodeReason(Reason):
    watchlist_matching_text: str
    watchlist_search_codes: List[str]
    watchlist_type: str


@dataclasses.dataclass
class MatchingTextDoesNotMatchMatchingFieldReason(Reason):
    watchlist_matching_text: str
    altered_party_matching_field: str

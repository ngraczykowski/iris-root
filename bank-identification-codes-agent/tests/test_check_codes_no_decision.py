import pytest

from bank_identification_codes_agent.bank_identification_codes import BankIdentificationCodes
from data_models.reasons import (
    MatchingTextDoesNotMatchMatchingFieldReason,
    MatchingTextDoesNotMatchWlSearchCodeReason,
    MatchingTextTooShortToBeCodeReason,
    NoSearchCodeInWatchlistReason,
)
from data_models.result import Solution

WL_TYPE = "Some text"  # this param is constant, not modified by any check rule


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [("Text", "Text", [], []), ("Some text", "Different Text", [], [])],
)
def test_empty_codes_lists(ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_DECISION
    assert isinstance(result.reason, NoSearchCodeInWatchlistReason)
    assert result.reason.conclusion == "NoSearchCodeInWatchlistReason"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [("Code", "Co", ["some search code"], ["some bic code"])],
)
def test_too_short_wl_matching_text(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_DECISION
    assert isinstance(result.reason, MatchingTextTooShortToBeCodeReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.conclusion == "MatchingTextTooShortToBeCodeReason"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [("Some Text", "Another Text", ["some search code"], ["some bic code"])],
)
def test_wl_text_not_match_ap_matching_field(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_DECISION
    assert isinstance(result.reason, MatchingTextDoesNotMatchMatchingFieldReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.altered_party_matching_field == ap_matching_field
    assert result.reason.conclusion == "MatchingTextDoesNotMatchMatchingFieldReason"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [("Some Text", "some text", ["some search code"], ["some bic code"])],
)
def test_matching_text_not_match_any_of_codes(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_DECISION
    assert isinstance(result.reason, MatchingTextDoesNotMatchWlSearchCodeReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.watchlist_type == WL_TYPE
    assert result.reason.watchlist_search_codes == [wl_search_codes[0].upper()]
    assert result.reason.conclusion == "MatchingTextDoesNotMatchWlSearchCodeReason"

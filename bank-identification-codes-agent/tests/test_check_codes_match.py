import pytest

from bank_identification_codes_agent.bank_identification_codes import BankIdentificationCodes
from data_models.reasons import (
    MatchingTextIsOnlyPartialMatchForSearchCodeReason,
    MatchingTextIsPartOfLongerSequenceReason,
    MatchingTextMatchesWlBicCodeReason,
)
from data_models.result import Solution

WL_TYPE = "Some text"  # this param is constant, not modified by any check rule


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        ("bic code", "bic code", ["some search code"], ["matching biccode", "different bic"]),
        ("Your code is: 1234", "1234", ["some search code"], ["bic: 1234"]),
    ],
)
def test_wl_matching_text_is_part_of_one_of_bic_codes(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.MATCH
    assert isinstance(result.reason, MatchingTextMatchesWlBicCodeReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.watchlist_type == WL_TYPE
    assert result.reason.watchlist_bic_code == wl_bic_codes[0].upper()
    assert result.reason.conclusion == "MatchingTextMatchesWlBicCodeReason"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes, expected_ap_matching_text",
    [
        (
            "search code123 XYZ",
            "search code",
            ["matching searchcode", "different search code"],
            [],
            "SEARCH CODE123",
        ),
        ("December2020", "2020", ["12 2020"], ["not matching bic"], "DECEMBER2020"),
    ],
)
def test_wl_matching_text_is_part_of_one_of_search_codes_longer_ap(
    ap_matching_field,
    wl_matching_text,
    wl_search_codes,
    wl_bic_codes,
    expected_ap_matching_text,
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.MATCH
    assert isinstance(result.reason, MatchingTextIsPartOfLongerSequenceReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.watchlist_type == WL_TYPE
    assert result.reason.altered_party_matching_field == ap_matching_field
    assert result.reason.partial_match_text == wl_search_codes[0].upper()
    assert result.reason.altered_party_matching_sequence == expected_ap_matching_text
    assert result.reason.conclusion == "MatchingTextIsPartOfLongerSequenceReason"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        ("search code xyz", "search code", ["matching searchcode", "some other search code"], []),
        ("2020", "2020", ["Year 2020"], ["not matching bic"]),
    ],
)
def test_wl_matching_text_is_part_of_one_of_search_codes(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.MATCH
    assert isinstance(result.reason, MatchingTextIsOnlyPartialMatchForSearchCodeReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.watchlist_type == WL_TYPE
    assert result.reason.watchlist_search_codes == [wl_search_codes[0].upper()]
    assert result.reason.conclusion == "MatchingTextIsOnlyPartialMatchForSearchCodeReason"

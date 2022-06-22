import pytest

from bank_identification_codes.codes_check.bank_identification_codes import BankIdentificationCodes
from bank_identification_codes.solution.reasons import (
    MatchingTextMatchesWlBicCodeReason,
    MatchingTextMatchesWlSearchCodeReason,
)
from bank_identification_codes.solution.result import Solution

WL_TYPE = "Some text"  # this param is constant, not modified by any check rule


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        (
            "Text with bic codeXXX",  # XXX - headquarters string at the end
            "bic code",
            ["some search code"],
            ["matching biccode", "some other bic"],
        ),
        ("Headquarters codeXXXXXX", "code", [], ["code"]),
    ],
)
def test_wl_matching_text_in_bic_codes_headquarters_string(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_MATCH
    assert isinstance(result.reason, MatchingTextMatchesWlBicCodeReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.watchlist_type == WL_TYPE
    assert result.reason.watchlist_bic_code == wl_bic_codes[0].upper()
    assert result.reason.conclusion == "MatchingTextMatchesWlBicCodeReason"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        ("Search code", "search code", ["searchcode"], []),
        ("Here is code123", "123", ["code123", "45"], []),
        ("Longer text containing code", "code", ["code"], []),
    ],
)
def test_ap_matched_text_exact_match_search_code(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_MATCH
    assert isinstance(result.reason, MatchingTextMatchesWlSearchCodeReason)
    assert result.reason.watchlist_matching_text == wl_matching_text
    assert result.reason.watchlist_type == WL_TYPE
    assert result.reason.watchlist_search_codes == [wl_search_codes[0].upper()]
    assert result.reason.conclusion == "MatchingTextMatchesWlSearchCodeReason"

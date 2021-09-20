import pytest

from bank_identification_codes_agent.bank_identification_codes import BankIdentificationCodes
from data_models.reasons import (
    MatchingTextMatchesWlBicCodeReason,
    MatchingTextMatchesWlSearchCodeReason,
)
from data_models.result import Solution

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
        ("Longer text containing code", "code", ["code"], []),
        ("Here is code 123", "123", ["123", "45"], []),
    ],
)
def test_exact_match_search_code(
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

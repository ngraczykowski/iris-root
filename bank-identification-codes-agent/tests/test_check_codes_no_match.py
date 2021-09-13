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
            "Text with bic codeXXX",  # XXX - headquarters string
            "bic code",
            ["some search code"],
            ["some other bic", "matching biccode"],
        )
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


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        ("Search code", "search code", ["searchcode"], []),
        ("Longer text containing code", "code", ["code"], []),
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

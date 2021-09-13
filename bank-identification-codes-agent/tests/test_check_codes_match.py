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
        ("bic code", "bic code", ["some search code"], ["different bic", "matching biccode"]),
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


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        ("search code123", "search code", ["different search code", "matching searchcode"], []),
        ("December2020", "2020", ["12 2020"], ["not matching bic"]),
    ],
)
def test_wl_matching_text_is_part_of_one_of_search_codes_longer_ap(
    ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes
):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.MATCH
    assert isinstance(result.reason, MatchingTextIsPartOfLongerSequenceReason)


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes",
    [
        ("search code xyz", "search code", ["some other search code", "matching searchcode"], []),
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

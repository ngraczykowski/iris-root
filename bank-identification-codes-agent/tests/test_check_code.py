import pytest

from bank_identification_codes_agent.bank_identification_codes import BankIdentificationCodes
from data_models.reasons import NoSearchCodeInWatchlistReason
from data_models.result import Solution

WL_TYPE = "Some text"


@pytest.mark.parametrize(
    "ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes", [("abc", "abc", [], [])]
)
def test_empty_watchlist_codes(ap_matching_field, wl_matching_text, wl_search_codes, wl_bic_codes):
    codes = BankIdentificationCodes(
        ap_matching_field, wl_matching_text, WL_TYPE, wl_search_codes, wl_bic_codes
    )
    result = codes.check()
    assert result.solution == Solution.NO_DECISION
    assert isinstance(result.reason, NoSearchCodeInWatchlistReason)

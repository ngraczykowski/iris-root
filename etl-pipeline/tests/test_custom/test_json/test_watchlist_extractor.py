import pytest

from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from tests.test_custom.constant import EXAMPLES


@pytest.mark.parametrize(
    ("match_record", "updated_match_record_with_wl_values"),
    EXAMPLES,
)
def test_watchlist_extractor(match_record, updated_match_record_with_wl_values):
    WatchlistExtractor().update_match_with_wl_values(match_record)
    assert match_record == updated_match_record_with_wl_values


@pytest.mark.parametrize(
    ("match_record", "updated_match_record_with_wl_values"),
    [
        (
            {"entity": {"dobs": [{"Y": 2010, "M": 10, "D": 2, "dob": "2010-10-02"}]}},
            ["2010-10-02", "2/10/2010"],
        ),
        (
            {
                "entity": {
                    "dobs": [{"Y": 2010, "M": 10, "D": 2, "S8_extracted_value": "2010-10-02"}]
                }
            },
            ["2010-10-02", "2/10/2010"],
        ),
        (
            {"entity": {"dobs": {"Y": 2010, "M": 10, "D": 2, "S8_extracted_value": "2010-10-02"}}},
            ["2010-10-02", "2/10/2010"],
        ),
        (
            {"entity": {"dobs": {"Y": 2010, "M": 10, "D": 2, "dob": "2010-10-02"}}},
            ["2010-10-02", "2/10/2010"],
        ),
        (
            {"entity": {"dobs": {"dob": "2010-10-02"}}},
            ["2010-10-02", ""],
        ),
        ({"entity": {"dobs": {"Y": 2010, "M": 10, "D": 2}}}, ["2/10/2010"]),
    ],
)
def test_extract_dob(match_record, updated_match_record_with_wl_values):
    result = WatchlistExtractor().extract_dob(match_record)
    assert result == updated_match_record_with_wl_values

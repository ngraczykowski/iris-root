import pytest

from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from tests.test_custom.constant import EXAMPLES, EXAMPLES_WITH_INVALID_OUTPUT


@pytest.mark.parametrize(
    ("match_record", "updated_match_record_with_wl_values"),
    EXAMPLES + EXAMPLES_WITH_INVALID_OUTPUT,
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
        (
            {"entity": {"dobs": {"dob": "2010-10-02"}}},
            ["2010-10-02", ""],
        ),
        (
            {"entity": {"dobs": {"dob": {"Y": 2010, "M": 10, "D": 2, "dob": "2010-10-02"}}}},
            [["2010-10-02"], [], [2, 10, 2010], ""],
        ),
    ],
)
def test_extract_dob(match_record, updated_match_record_with_wl_values):
    result = WatchlistExtractor().extract_dob(match_record)
    assert result == updated_match_record_with_wl_values


@pytest.mark.parametrize(
    ("match_record", "updated_match_record_with_wl_values"),
    [({"entity": {"aliases": [{"alias": "JJ", "type": "ok", "category": "nok"}]}}, [["JJ"]])],
)
def test_extract_alias(match_record, updated_match_record_with_wl_values):
    result = WatchlistExtractor().extract_wl_data_by_path(match_record, "aliases", "alias")
    assert result == updated_match_record_with_wl_values

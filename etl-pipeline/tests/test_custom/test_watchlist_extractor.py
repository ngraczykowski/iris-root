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

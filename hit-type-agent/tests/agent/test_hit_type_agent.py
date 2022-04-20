import pytest

from hit_type import HitTypeAgent, Result
from tests.agent.constant import TEST_CASES


@pytest.mark.parametrize("payload_with_data_and_result", TEST_CASES)
def test_when_no_names(payload_with_data_and_result):
    data = payload_with_data_and_result["data"]
    result = HitTypeAgent().resolve(**data)
    assert result == Result(**payload_with_data_and_result["result"])

import pytest

from etl_pipeline.custom.ms.datatypes.field import InputRecordField
from etl_pipeline.custom.ms.trigger_discovery.discoverer import TriggeredTokensDiscoverer


@pytest.mark.parametrize(
    ("matched_tokens", "fields", "expected_result"),
    [
        (["OK123"], {"TEST": InputRecordField(name="TEST", value="OK3")}, {"OK123": {}}),
        (
            ["DIRECT_MATCH"],
            {
                "TEST": InputRecordField(name="TEST", value="DIRECT_MATCH"),
                "TEST1": InputRecordField(name="TEST1", value="ANY_CHANCE_THERE_IS"),
            },
            {"DIRECT_MATCH": {"TEST": ["DIRECT_MATCH"]}},
        ),
        (
            ["OK123", "MAMA"],
            {
                "TEST": InputRecordField(name="TEST", value="OK3"),
                "TEST1": InputRecordField(name="TEST1", value="MAMA"),
                "TEST2": InputRecordField(name="TEST2", value="ORCHARD"),
            },
            {"MAMA": {"TEST1": ["MAMA"]}, "OK123": {}},
        ),
    ],
)
def test_discover(matched_tokens, fields, expected_result):
    TRIGGERS_MAP = {field: fields[field].value for field in fields if fields[field]}
    uut = TriggeredTokensDiscoverer(71)
    discovered = uut.discover(matched_tokens, TRIGGERS_MAP)
    assert discovered == expected_result

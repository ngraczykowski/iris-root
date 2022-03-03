import json

import pytest

from etl_pipeline.custom.ms.payload_loader import PayloadLoader


@pytest.mark.parametrize(
    ("payload", "expected_result"),
    [
        (
            {
                "matchesPayloads[0].stopDescriptors[0].name": "JOHNNY DOE",
                "matchesPayloads[1].stopDescriptors[0].name": "ss DOE",
            },
            {
                "matchesPayloads": [
                    {"stopDescriptors": [{"name": "JOHNNY DOE"}]},
                    {"stopDescriptors": [{"name": "ss DOE"}]},
                ]
            },
        ),
        (
            {"matchesPayloads[0].stopDescriptors[0].name": "JOHNNY DOE"},
            {"matchesPayloads": [{"stopDescriptors": [{"name": "JOHNNY DOE"}]}]},
        ),
    ],
)
def test_load_payload_from_json(payload, expected_result):
    assert PayloadLoader().load_payload_from_json(payload) == expected_result


def extract(payload, record):
    for key, value in payload.items():
        splitted_keys = key.split(".")
        current_dict = record
        for i in splitted_keys:
            index_of_element = PayloadLoader.LIST_ELEMENT_REGEX.search(i)
            if index_of_element:
                index_of_element = int(index_of_element.groups()[0])
                current_dict = current_dict[PayloadLoader.LIST_ELEMENT_REGEX.sub("", i)][
                    index_of_element
                ]
            else:
                current_dict = current_dict[i]
        if current_dict != value:
            value = json.loads(value)
        assert current_dict == value


def test_big_payload():
    with open("tests/shared/test_big_payload_file.txt", "r") as f:
        text = json.load(f)
    extract(text, PayloadLoader().load_payload_from_json(text))

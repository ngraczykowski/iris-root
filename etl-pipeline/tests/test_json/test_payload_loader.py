import json

import pytest

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader

cn = pipeline_config.cn


def extract_dict_for_comparison(reference_payload, record):
    for key, value in reference_payload.items():
        splitted_keys = key.split(".")
        current_dict = record
        for i in splitted_keys:
            index_of_element = PayloadLoader.LIST_TYPE_REGEX.search(i)
            if index_of_element:
                index_of_element = int(index_of_element.groups()[0])
                current_dict = current_dict[PayloadLoader.LIST_TYPE_REGEX.sub("", i)][
                    index_of_element
                ]
            else:
                current_dict = current_dict[i]
        if current_dict != value:
            value = json.loads(value)
        assert current_dict == value


@pytest.mark.parametrize(
    ("payload", "expected_result"),
    [
        (
            {
                "matchesPayloads[0].stopDescriptors[0].name": "JOHNNY DOE",
                "matchesPayloads[1].stopDescriptors[0].name": "ss DOE",
                "matchesPayloads[2].stopDescriptors[0].name": "another DOE",
                "matchesPayloads[1].stopDescriptors[1].name": "second DOE",
                "matchesPayloads[1].stopDescriptors[1].field[0]": "second DOE",
                "matchesPayloads[1].stopDescriptors[2].field[0]": "another DOE",
                "matchesPayloads[2].stopDescriptors[2].field[1]": "another DOE",
            },
            {
                "matchesPayloads": [
                    {"stopDescriptors": [{"name": "JOHNNY DOE"}]},
                    {
                        "stopDescriptors": [
                            {"name": "ss DOE"},
                            {"name": "second DOE", "field": ["second DOE"]},
                            {"field": ["another DOE"]},
                        ]
                    },
                    {
                        "stopDescriptors": [
                            {"name": "another DOE"},
                            {},
                            {"field": ["", "another DOE"]},
                        ]
                    },
                ]
            },
        ),
        (
            {
                "alertPayload.headerInfo.uniqueCustomerId": "R_RUS_Active_Address_2010-02-11-11.11",
                "alertPayload.headerInfo.datasetId": "1044",
                "alertPayload.headerInfo.datasetName": "R_US_Active_Address",
                "alertPayload.headerInfo.masterId": "637451852",
                "alertPayload.headerInfo.currentVersionId": "678175153",
                "alertPayload.headerInfo.stopDescriptors[0]": "JOHN DOE",
                "alertPayload.headerInfo.stopDescriptors[1]": "DOE JOHN",
                "alertPayload.headerInfo.firstVersionCreatedDt": "2010-02-02T22: 24: 24.222+02: 20",
                "alertPayload.headerInfo.lastVersionUpdatedDt": "2010-02-02T22: 24: 24.222+02: 20",
                "alertPayload.headerInfo.masterVersion": "a172863871263234234",
                "alertPayload.headerInfo.inputVersionSample": "R_RUS_Active_Address_2010-02-11-11.11",
                "alertPayload.inputRecord.version": "a1231231231241343454251234234",
                "alertPayload.inputRecord.createdDate": "01/05/10",
                "alertPayload.inputRecord.fields[0].name": "SOURCE_REF",
                "alertPayload.inputRecord.fields[0].isScreenable": "false",
                "alertPayload.inputRecord.fields[0].value": "R_RUS_Active_Address_2010-02-11-11.11",
                "alertPayload.inputRecord.fields[0].sortOrder": "1",
                "alertPayload.inputRecord.fields[1].name": "UNIQUE_KEY",
                "alertPayload.inputRecord.fields[1].isScreenable": "false",
                "alertPayload.inputRecord.fields[1].value": "ALA12321312305004514758",
                "alertPayload.inputRecord.fields[1].sortOrder": "12",
                "alertPayload.inputRecord.status": "X",
            },
            {
                "alertPayload": {
                    "headerInfo": {
                        "uniqueCustomerId": "R_RUS_Active_Address_2010-02-11-11.11",
                        "datasetId": "1044",
                        "datasetName": "R_US_Active_Address",
                        "masterId": "637451852",
                        "currentVersionId": "678175153",
                        "stopDescriptors": ["JOHN DOE", "DOE JOHN"],
                        "firstVersionCreatedDt": "2010-02-02T22: 24: 24.222+02: 20",
                        "lastVersionUpdatedDt": "2010-02-02T22: 24: 24.222+02: 20",
                        "masterVersion": "a172863871263234234",
                        "inputVersionSample": "R_RUS_Active_Address_2010-02-11-11.11",
                    },
                    "inputRecord": {
                        "version": "a1231231231241343454251234234",
                        "createdDate": "01/05/10",
                        cn.FIELDS: [
                            {
                                "name": "SOURCE_REF",
                                "isScreenable": "false",
                                "value": "R_RUS_Active_Address_2010-02-11-11.11",
                                "sortOrder": "1",
                            },
                            {
                                "name": "UNIQUE_KEY",
                                "isScreenable": "false",
                                "value": "ALA12321312305004514758",
                                "sortOrder": "12",
                            },
                        ],
                        "status": "X",
                    },
                }
            },
        ),
    ],
)
def test_load_payload_from_json(payload, expected_result):
    assert PayloadLoader().load_payload_from_json(payload) == expected_result


def test_big_payload():
    with open("tests/shared/test_big_payload_file.json", "r") as f:
        text = json.load(f)
    extract_dict_for_comparison(text, PayloadLoader().load_payload_from_json(text))

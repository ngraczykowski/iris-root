# pylint: disable=missing-module-docstring
# pylint: disable=missing-class-docstring
# pylint: disable=missing-function-docstring
import json
import os

import pytest
from fastapi.testclient import TestClient

from company_name.rest_api.__main__ import app
from company_name.rest_api.data_models import CompareOutput, ResolvePairsOutput


def _read_json_resource(file_name: str) -> CompareOutput:
    with open(os.path.join("tests/server/resources", file_name)) as file:
        data = json.load(file)
    return CompareOutput(**data)


CLIENT = TestClient(app)


@pytest.mark.parametrize(
    "ap_name, wl_name, expected_result",
    [
        ("Silent Eight Pte Ltd", "Facebook", _read_json_resource("compare_names/S8_Fb.json")),
        ("", "ABC Company", _read_json_resource("compare_names/empty_alerted.json")),
        ("ABC Company", "", _read_json_resource("compare_names/empty_watchlist.json")),
        ("", "", _read_json_resource("compare_names/empty_both.json")),
    ],
)
def test_compare(ap_name, wl_name, expected_result):
    response = CLIENT.request(
        "POST",
        "/organization_name_agent/compare_names",
        json={"ap_name": ap_name, "wl_name": wl_name},
    )
    assert response.status_code == 200
    result = CompareOutput(**response.json())
    assert result == expected_result


@pytest.mark.parametrize(
    "ap_names, wl_names, expected_results",
    [
        (
            ["Silent Eight Pte Ltd"],
            ["Facebook"],
            [{"solution": "NO_MATCH", "proba": 0.97}],
        ),
        (
            ["Standard Chartered Bank"],
            ["SCB"],
            [{"solution": "MATCH", "proba": 0.8}],
        ),
        (
            ["ABC", "DEF"],
            ["ABCDE"],
            [{"solution": "MATCH", "proba": 1.0}, {"solution": "NO_MATCH", "proba": 0.94}],
        ),
        (
            [""],
            ["Some Not Empty Text"],
            [{"solution": "INCONCLUSIVE", "proba": 0.0}],
        ),
        (
            [],
            ["Not a None"],
            [],  # as cartesian product of none and sth is none, there is no result
        ),
    ],
)
def test_resolve_pairs(ap_names, wl_names, expected_results):
    response = CLIENT.request(
        "POST",
        "/organization_name_agent/resolve_pairs",
        json={"ap_names": ap_names, "wl_names": wl_names},
    )
    assert response.status_code == 200
    results = [ResolvePairsOutput(**resp) for resp in response.json()]
    assert len(results) == len(expected_results)
    for result, expected in zip(results, expected_results):
        assert result.solution == expected["solution"]
        assert round(result.solution_probability, 2) == expected["proba"]

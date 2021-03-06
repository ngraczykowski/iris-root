# pylint: disable=missing-module-docstring
# pylint: disable=missing-class-docstring
# pylint: disable=missing-function-docstring
import pytest
from fastapi.testclient import TestClient

from organization_name_knowledge.__main__ import app

CLIENT = TestClient(app)


@pytest.mark.parametrize(
    "name, expected",
    [
        (
            "Silent Eight Pte Ltd",
            {"source": "Silent Eight Pte Ltd", "base": "Silent Eight", "legal": "Pte Ltd"},
        ),
        ("Hello World", {"source": "Hello World", "base": "Hello World", "legal": ""}),
    ],
)
def test_parse(name, expected):
    response = CLIENT.request("GET", f"/organization_name_knowledge/parse/{name}")
    assert response.status_code == 200
    result = response.json()
    assert result["source"]["original"] == expected["source"]
    assert result["base"]["original_name"] == expected["base"]
    assert result["legal"]["original_name"] == expected["legal"]


@pytest.mark.parametrize(
    "freetext, expected_results",
    [
        (
            "Silent Eight Pte Ltd",
            [{"base": "Eight", "legal": "Pte Ltd"}, {"base": "Silent Eight", "legal": "Pte Ltd"}],
        ),
        ("Hello World", []),
        ("HSBC Bank", [{"base": "HSBC Bank", "legal": ""}]),
    ],
)
def test_parse_freetext(freetext, expected_results):
    response = CLIENT.request("GET", f"/organization_name_knowledge/parse_freetext/{freetext}")
    assert response.status_code == 200
    results = response.json()
    assert len(results) == len(expected_results)
    for expected, result in zip(expected_results, results):
        assert result["base"]["original_name"] == expected["base"]
        assert result["legal"]["original_name"] == expected["legal"]


@pytest.mark.parametrize(
    "endpoint_name, request_content, expected_status_code",
    [
        ("parse", "", 404),
        ("parse", " ", 200),
        ("parse_freetext", "", 404),
        ("parse_freetext", " ", 200),
    ],
)
def test_errors(endpoint_name, request_content, expected_status_code):
    response = CLIENT.request(
        "GET", f"/organization_name_knowledge/{endpoint_name}/{request_content}"
    )
    assert response.status_code == expected_status_code

import json
import pathlib

import pytest

from company_name import CompanyNameAgent

with open(pathlib.Path(__file__).parent / "known_cases.json") as f:
    KNOWN_CASES = json.load(f)


@pytest.mark.parametrize(("first", "second", "expected"), KNOWN_CASES)
def test_when_no_names(first, second, expected):
    print(first, second)
    result = CompanyNameAgent().resolve(first, second)
    print(result)
    assert result.solution.value == expected

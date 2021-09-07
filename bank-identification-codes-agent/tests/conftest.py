import csv
import json
import os
from pathlib import Path
from typing import Tuple

from attr import attrs, attrib
from cattr import structure_attrs_fromdict

from id_mismatch_agent.api import (
    SearchCodeMismatchAgentInput,
)
from id_mismatch_agent.result import Solution, Reason


@attrs(frozen=True)
class Case:
    input: SearchCodeMismatchAgentInput = attrib()
    expected_values: Tuple[Solution, Reason] = attrib()


def read_test_cases():
    datadir = Path(os.path.dirname(os.path.realpath(__file__))) / "test_agent"
    csvfile = datadir / "logic-test-cases.csv"

    test_cases = []
    with csvfile.open("rt") as f:
        reader = csv.DictReader(f)

        for row in reader:

            input = SearchCodeMismatchAgentInput(
                row["T_TYPE"],
                row["AP_TAG"],
                row["AP_MATCHINGFIELD"],
                row["WL_MATCHINGTEXT"],
                row["WL_TYPE"],
                row["WL_SEARCH_CODES"].split(),
                row["WL_BIC_CODES"].split(),
            )

            result_value = row["RESULT"]
            result = Solution[result_value] if result_value else Solution.NO_DECISION

            reason = None
            reason_type_name = row["REASON_TYPE"]
            if reason_type_name:
                reason_dict = {
                    attribute[7:]: value
                    for (attribute, value) in row.items()
                    if attribute.startswith("REASON_") and attribute != "REASON_TYPE"
                }
                if reason_dict.get("search_codes", ""):
                    reason_dict["search_codes"] = json.loads(reason_dict["search_codes"])

                import id_mismatch_agent.api as api_module

                reason_type = getattr(api_module, reason_type_name)

                reason = structure_attrs_fromdict(reason_dict, reason_type)

            expected_value = (result, reason)
            test_cases.append(Case(input, expected_value))

    return test_cases


def pytest_generate_tests(metafunc):
    if "logic_test_case" in metafunc.fixturenames:
        test_cases = read_test_cases()
        metafunc.parametrize("logic_test_case", test_cases)

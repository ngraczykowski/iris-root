import csv
import dataclasses
import json
import os
from pathlib import Path
from typing import Dict, List, Tuple

import data_models.reasons as reasons_data_models
from bank_identification_codes_agent.agent import BankIdentificationCodesAgentInput
from data_models.result import Reason, Solution


@dataclasses.dataclass
class Case:
    input: BankIdentificationCodesAgentInput
    expected_values: Tuple[Solution, Reason]


def load_test_cases() -> List[Case]:
    datadir = Path(os.path.dirname(os.path.realpath(__file__))) / "resources"
    csvfile = datadir / "logic-test-cases.csv"

    test_cases = []
    with csvfile.open("rt") as f:
        reader = csv.DictReader(f)

        for row in reader:

            input = BankIdentificationCodesAgentInput(
                row["AP_MATCHINGFIELD"],
                row["WATCHLIST_MATCHINGTEXT"],
                row["WATCHLIST_TYPE"],
                row["WATCHLIST_SEARCH_CODES"].split(),
                row["WATCHLIST_BIC_CODES"].split(),
            )

            result_value = row["RESULT"]
            result = Solution[result_value] if result_value else Solution.NO_DECISION
            reason = get_reason_from_row_values(row)
            expected_value = (result, reason)
            test_cases.append(Case(input, expected_value))

    return test_cases


def get_reason_from_row_values(row: Dict[str, str]) -> Reason:
    reason = None
    reason_type_name = row["REASON_TYPE"]
    if reason_type_name:
        reason_dict = {
            attribute[7:]: value
            for (attribute, value) in row.items()
            if attribute.startswith("REASON_") and attribute != "REASON_TYPE"
        }
        if reason_dict.get("watchlist_search_codes", ""):
            reason_dict["watchlist_search_codes"] = json.loads(
                reason_dict["watchlist_search_codes"]
            )

        reason_class_name = getattr(reasons_data_models, reason_type_name)
        values = []
        for attribute_name in reason_class_name.__annotations__:
            try:
                values.append(reason_dict[attribute_name])
            except KeyError:
                pass
        reason = reason_class_name(*values)
    return reason


def pytest_generate_tests(metafunc):
    if "logic_test_case" in metafunc.fixturenames:
        test_cases = load_test_cases()
        metafunc.parametrize("logic_test_case", test_cases)

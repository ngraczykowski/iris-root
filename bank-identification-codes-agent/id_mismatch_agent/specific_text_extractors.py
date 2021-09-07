import re
from typing import Optional

ACC_NO_REGEX = re.compile(
    r"""
^[\s\\n]* # Match starting chars. Acc no is often prefixed with \, / or some other whitespace chars
(?P<ACC>\d+) # Capture digits to group named ACC
""",
    re.VERBOSE,
)

PAN_NO_REGEX = re.compile(
    r"""
PAN\s*(NO[\s\.:]*)* # Matches "PAN", "PAN NO." or "PANNO" etc 
(?P<PAN>\d+) # Actual PAN number matching group, just 1-inf digits
""",
    re.VERBOSE,
)


def try_extracting_acc_no(text: str) -> Optional[str]:
    acc_no_match = ACC_NO_REGEX.search(text)

    if acc_no_match:
        return acc_no_match.groupdict()["ACC"]

    return None


def try_extracting_pan_no(text: str) -> Optional[str]:
    pan_no_match = PAN_NO_REGEX.search(text)

    if pan_no_match:
        return pan_no_match.groupdict()["PAN"]

    return None

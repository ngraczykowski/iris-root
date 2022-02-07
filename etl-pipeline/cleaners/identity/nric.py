import re
from typing import Any, Dict, List, Tuple

NRIC_S_SYMBOL = "s"
NRIC_T_SYMBOL = "t"
NRIC_SYMBOLS = [NRIC_S_SYMBOL, NRIC_T_SYMBOL]
NRIC_REGEX = re.compile(r"^NRIC:.*?([STGF]\d{7}[A-Z])")
DOB_REGEX = re.compile(r"^NRIC:.*?([STGF]\d{7}[A-Z])")
POSSIBLE_NRIC_REGEX = re.compile(r"([STGF]\d{7}[A-Z])")
STGF_REGEX = re.compile(r"^[STGF]\d{7}[A-Z]$")


def _extract_birth_year_from_st_nrics(nrics: List[str]):
    for nric in nrics:
        nric_type = nric[0]
        if nric_type.lower() in NRIC_SYMBOLS:
            two_digit_year = nric[1:3]
            birth_year = None
            if nric_type.lower() == NRIC_S_SYMBOL:
                if int(two_digit_year) >= 68:
                    birth_year = "19" + two_digit_year
            else:
                birth_year = "20" + two_digit_year

            return birth_year


def extract_wl_nric_dob(custom_field: str) -> Dict[str, Tuple[Any, ...]]:
    nric_match = NRIC_REGEX.match(custom_field)
    dob_match = DOB_REGEX.match(custom_field)
    possible_nric_match = POSSIBLE_NRIC_REGEX.findall(custom_field)
    nrics = nric_match.groups() if nric_match else None
    dobs = dob_match.groups() if dob_match else None
    possible_nrics = possible_nric_match if possible_nric_match else None
    if nrics:
        dobs = _extract_birth_year_from_st_nrics(nrics)
    return {"nric": nrics, "dob": dobs, "possible_nric": possible_nrics}


def extract_ap_nric(ap_id_numbers: List[str]) -> List[str]:
    ap_nrics = []
    for id_number in set(ap_id_numbers):
        if id_number and STGF_REGEX.findall(id_number.upper()):
            ap_nrics.append(id_number)
    return ap_nrics

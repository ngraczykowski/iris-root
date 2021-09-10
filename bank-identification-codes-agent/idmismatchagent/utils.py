import re
from typing import List

SEPARATORS_PATTERN = re.compile(r"[-),./]+")
HEADQUARTERS_INDICATOR = "XXX"


def _remove_ids_separators(text: str) -> str:
    return SEPARATORS_PATTERN.sub("", text).strip().upper()


def _is_headquarters(bic_code: str) -> bool:
    return bic_code is not None and len(bic_code) > 0 and bic_code.endswith(HEADQUARTERS_INDICATOR)


def _filter_none_values(data: List[str]) -> List[str]:
    return list(filter(lambda el: el.upper() != "NONE", data))


def get_text_pattern(text: str) -> str:
    text_pattern = "[ ]{0,1}".join(text)
    pattern = fr"""
    ([\w]{{0,}}                 # Matches 0 to inf characters before actual pattern (no spaces)
    ({text_pattern})   # 2nd capture group for matching actual pattern
    [\w]{{0,}})                 # Matches 0 to inf characters after actual pattern
    """
    return pattern

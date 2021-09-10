import re
from typing import List, Union

SEPARATORS_PATTERN = re.compile(r"[-),./]+")
HEADQUARTERS_INDICATOR = "XXX"


def remove_no_word_characters(text: str) -> str:
    return re.sub(r"\W+", "", text)


def remove_separators(text: str) -> str:
    return SEPARATORS_PATTERN.sub("", text).strip().upper()


def remove_words_separators(text: str) -> str:
    return text.replace(" ", "").replace(",", "").replace("-", "")


def is_headquarters(bic_code: str) -> bool:
    return bic_code is not None and len(bic_code) > 0 and bic_code.endswith(HEADQUARTERS_INDICATOR)


def filter_none_values(data: List[str]) -> List[str]:
    return list(filter(lambda el: el.upper() != "NONE", data))


def get_text_pattern(text: str) -> str:
    text_pattern = "[ ]{0,1}".join(text)
    pattern = fr"""
    ([\w]{{0,}}                 # Matches 0 to inf characters before actual pattern (no spaces)
    ({text_pattern})   # 2nd capture group for matching actual pattern
    [\w]{{0,}})                 # Matches 0 to inf characters after actual pattern
    """
    return pattern


def get_first_match(first: str, second: str) -> Union[str, None]:
    pattern = get_text_pattern(text=first)
    matching_field_without_separators = remove_separators(second)
    matching_text_in_field_match = re.search(
        pattern, matching_field_without_separators, re.VERBOSE
    )
    if matching_text_in_field_match:
        return matching_text_in_field_match.group(0)
    else:
        return None

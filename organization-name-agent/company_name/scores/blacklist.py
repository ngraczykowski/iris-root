import re
from typing import List

from company_name.names.name_information import NameInformation
from .score import Score


BLACKLIST = {"gazprom", "vtb"}
BLACKLIST_REGEX = re.compile(r"\b(" + "|".join(BLACKLIST) + r")\b", re.IGNORECASE)


def _blacklisted(name: str) -> List[str]:
    return BLACKLIST_REGEX.findall(name)


def blacklist_score(first: NameInformation, second: NameInformation) -> Score:
    first_blacklisted, second_blacklisted = _blacklisted(first.original.cleaned), _blacklisted(second.original.cleaned)
    return Score(
        value=float(bool(first_blacklisted or second_blacklisted)),
        compared=(tuple(first_blacklisted), tuple(second_blacklisted))
    )

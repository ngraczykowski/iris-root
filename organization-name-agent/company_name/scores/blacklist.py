import re
from typing import List

from company_name.names.name_information import NameInformation
from .score import Score


BLACKLIST = {"gazprom", "vtb"}
BLACKLIST_REGEX = re.compile(r"\b(" + "|".join(BLACKLIST) + r")\b", re.IGNORECASE)


def _blacklisted(name: str) -> List[str]:
    return BLACKLIST_REGEX.findall(name)


def blacklist_score(name: NameInformation) -> Score:
    first_blacklisted = _blacklisted(name.source.cleaned)
    return Score(
        value=float(bool(first_blacklisted)) if name.source.cleaned else None,
        compared=(tuple(first_blacklisted), ()),
    )

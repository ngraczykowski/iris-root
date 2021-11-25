import re
from typing import List

from organization_name_knowledge import NameInformation

from company_name.scores.score import Score

BLACKLIST = {"gazprom", "vtb"}
BLACKLIST_REGEX = re.compile(r"\b(" + "|".join(BLACKLIST) + r")\b", re.IGNORECASE)


def _blacklisted(name: str) -> List[str]:
    return BLACKLIST_REGEX.findall(name)


def get_blacklist_score(name: NameInformation, other_name: NameInformation) -> Score:
    first_blacklisted = _blacklisted(name.source.cleaned)
    status = (
        Score.ScoreStatus.OK
        if name.source.cleaned
        else Score.ScoreStatus.recognize(name, other_name)
    )
    return Score(
        status=status,
        value=float(bool(first_blacklisted)),
        compared=(tuple(first_blacklisted), ()),
    )

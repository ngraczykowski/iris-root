import re
from typing import List

from agent_base.utils.config import ConfigurationException
from organization_name_knowledge import NameInformation

from company_name.scores.score import Score

try:
    with open("config/blacklist.txt", "r") as ftr:
        BLACKLIST = ftr.read().split("\n")
    BLACKLIST_REGEX = re.compile(r"|".join(BLACKLIST), re.IGNORECASE)

except FileNotFoundError:
    raise ConfigurationException("Configuration file blacklist.txt missing")


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

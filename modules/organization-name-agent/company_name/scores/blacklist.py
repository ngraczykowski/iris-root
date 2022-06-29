import re
from typing import List

from agent_base.utils.config import Config
from organization_name_knowledge import NameInformation

from company_name.scores.score import Score


class Blacklist:
    def __init__(self, config: Config):
        self.configuration_file_name = "blacklist.txt"
        blacklist_file_path = config.get_config_path(self.configuration_file_name, required=True)
        with open(blacklist_file_path, "r") as ftr:
            blacklist_ = ftr.read().split("\n")
        self.BLACKLIST_REGEX = re.compile(r"|".join(blacklist_), re.IGNORECASE)

    def _blacklisted(self, name: str) -> List[str]:
        return self.BLACKLIST_REGEX.findall(name)

    def get_blacklist_score(self, name: NameInformation, other_name: NameInformation) -> Score:
        first_blacklisted = self._blacklisted(name.source.cleaned)
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

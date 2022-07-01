import dataclasses
from typing import List


@dataclasses.dataclass
class BaseRuleConfig:
    def dict(self):
        return {key: value for key, value in dataclasses.asdict(self).items() if value}


@dataclasses.dataclass
class AlphabetRuleConfig(BaseRuleConfig):
    acceptable_alphabets: List[str] = None
    min_acceptable_fraction: float = None


@dataclasses.dataclass
class InclusionRuleConfig(BaseRuleConfig):
    without_part: List[str] = None
    without_token: List[str] = None


@dataclasses.dataclass
class LengthRuleConfig(BaseRuleConfig):
    max_length: int = None
    max_unique_tokens: int = None

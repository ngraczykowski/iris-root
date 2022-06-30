import collections
import re
import unicodedata
from typing import Any, Callable, Mapping

from agent_base.utils.config import Config, ConfigurationException
from organization_name_knowledge import NameInformation

from company_name.utils.clear_name import clear_name


class BasicPreconditionRule:
    rules_checks: Mapping[str, Callable[[NameInformation, Any], bool]] = None

    def __init__(self, rules_config: Mapping[str, Any]):
        self.rules_config = rules_config
        unrecognized_rules = set(self.rules_config.keys()).difference(self.rules_checks)
        if unrecognized_rules:
            raise ConfigurationException(f"Unrecognized rules {unrecognized_rules}")

    def check(self, name: NameInformation) -> bool:
        return all(
            self.rules_checks[rule](name, value) for rule, value in self.rules_config.items()
        )


class LengthRule(BasicPreconditionRule):
    rules_checks = {
        "max_length": lambda name, value: len(name.source.original) <= int(value),
        "max_unique_tokens": lambda name, value: len(name.tokens()) <= int(value),
    }


class InclusionRule(BasicPreconditionRule):
    rules_checks = {
        "without_part": lambda name, value: not any(v in name.source.cleaned for v in value),
        "without_token": lambda name, value: not any(
            re.search(rf"\b{v}\b", name.source.cleaned) or v in name.tokens() for v in value
        ),
    }

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.rules_config = {
            key: [clear_name(t) for t in value] for key, value in self.rules_config.items()
        }


class AlphabetRule(BasicPreconditionRule):
    rules_checks = {"min_acceptable_fraction", "acceptable_alphabets"}

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        if self.rules_config:
            if not all(r in self.rules_config for r in self.rules_checks):
                raise ConfigurationException(
                    f"For alphabet {', '.join(self.rules_checks)} must be defined"
                )

    @staticmethod
    def _get_used_alphabets(source: str):
        # inspired by https://github.com/EliFinkelshteyn/alphabet-detector
        return collections.Counter(
            unicodedata.name(char).split(" ")[0].lower() for char in source if char.isalnum()
        )

    def check(self, name: NameInformation):
        if self.rules_config:
            alphabets = self._get_used_alphabets(name.source.original)
            if not alphabets:
                return False
            acceptable_fraction = sum(
                v for k, v in alphabets.items() if k in self.rules_config["acceptable_alphabets"]
            ) / sum(alphabets.values())
            return acceptable_fraction >= self.rules_config["min_acceptable_fraction"]

        return True


class NamePreconditions:
    configuration_file_name = "name-preconditions.yaml"

    preconditions = {
        "length": LengthRule,
        "inclusion": InclusionRule,
        "alphabet": AlphabetRule,
    }

    def __init__(self, config: Config):
        preconditions_config = config.load_yaml_config(self.configuration_file_name, required=False)
        unrecognized_rules = set(preconditions_config.keys()).difference(self.preconditions)
        if unrecognized_rules:
            raise ConfigurationException(f"Unrecognized rules {unrecognized_rules}")
        self.preconditions_rules = {
            key: self.preconditions[key](value) for key, value in preconditions_config.items()
        }

    def preconditions_met(self, name: NameInformation) -> bool:
        return all(rule.check(name) for rule in self.preconditions_rules.values())

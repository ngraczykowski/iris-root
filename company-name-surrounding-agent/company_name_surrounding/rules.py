import dataclasses

from agent_base.utils import Config

from company_name_surrounding.data_models import Solution


@dataclasses.dataclass
class Rule:
    threshold: int
    solution: Solution


def load_rules(config: Config):
    rules = [
        Rule(threshold=rule["threshold"], solution=Solution[rule["solution"]])
        for rule in config.application_config["solution_rules"]
    ]
    return sorted(rules, key=lambda rule: rule.threshold, reverse=True)

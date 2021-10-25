import dataclasses

from agent_base.utils import Config


@dataclasses.dataclass
class Rule:
    solution: str
    lower_bound: int
    upper_bound: int


def load_rules(config: Config):
    try:
        rules = [
            Rule(
                solution=str(rule["solution"]),
                lower_bound=int(rule["lower_bound"]),
                upper_bound=int(rule["upper_bound"]),
            )
            for rule in config.application_config["solution_rules"]
        ]
        return rules
    except Exception as exc:
        raise ValueError("There was an error loading solution rules: " + exc.__str__())

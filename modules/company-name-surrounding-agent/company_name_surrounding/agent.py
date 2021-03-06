import logging
from typing import List

from agent_base.agent import Agent

from company_name_surrounding.data_models import Result
from company_name_surrounding.rules import Rule, load_rules
from company_name_surrounding.surrounding_check import get_company_token_number


class CompanyNameSurroundingAgent(Agent):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.rules: List[Rule] = load_rules(self.config)
        self.default_solution = self.config.application_config["default_response"]
        self.logger = logging.getLogger("main").getChild("agent")

    def resolve(self, names: List[str]) -> Result:
        self.logger.info("Checking {0}".format(names))
        number = get_company_token_number(names=names)
        solution = self._get_solution(number)
        self.logger.info("For {0} get count {1} and solution {2}".format(names, number, solution))
        return Result(solution=solution, result=number)

    def _get_solution(self, number: int) -> str:
        solution = next(
            (
                rule.solution
                for rule in self.rules
                if rule.upper_bound >= number >= rule.lower_bound
            ),
            self.default_solution,
        )
        return solution

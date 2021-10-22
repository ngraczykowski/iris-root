import logging
from typing import List

from agent_base.agent import Agent

from company_name_surrounding.data_models import Result, Solution
from company_name_surrounding.rules import Rule, load_rules
from company_name_surrounding.surrounding_check import get_company_token_count

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler()
c_handler.setLevel(logging.DEBUG)


class CompanyNameSurroundingAgent(Agent):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.rules: List[Rule] = load_rules(self.config)

    def resolve(self, names: List[str]) -> Result:
        logger.info("Checking {0}".format(names))
        count = get_company_token_count(ap_names=names)
        solution = self._get_solution(count)
        return Result(solution, count)

    def _get_solution(self, count: int) -> Solution:
        solution = next(
            (rule.solution for rule in self.rules if count >= rule.threshold), Solution.NO_MATCH
        )
        return solution

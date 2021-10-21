import logging
from typing import List

from agent_base.agent import Agent

from company_name_surrounding.surrounding_check import get_company_token_count

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler()
c_handler.setLevel(logging.DEBUG)


class CompanyNameSurroundingAgent(Agent):
    def resolve(self, names: List[str]) -> int:
        logger.info("Checking {0}".format(names))
        return get_company_token_count(ap_names=names)

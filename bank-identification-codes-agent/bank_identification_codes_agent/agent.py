import dataclasses
import logging

from agent_base.agent import Agent

from bank_identification_codes_agent.bank_identification_codes import BankIdentificationCodes
from bank_identification_codes_agent.text_utils import filter_none_values
from data_models.result import Result, Solution


@dataclasses.dataclass
class BankIdentificationCodesAgentInput:
    altered_party_matching_field: str
    watchlist_matching_text: str
    watchlist_type: str
    watchlist_search_codes: list
    watchlist_bic_codes: list


class BankIdentificationCodesAgent(Agent):
    def resolve(self, agent_input: BankIdentificationCodesAgentInput) -> Result:
        try:
            codes = BankIdentificationCodes(
                agent_input.altered_party_matching_field,
                agent_input.watchlist_matching_text,
                agent_input.watchlist_type,
                filter_none_values(agent_input.watchlist_search_codes),
                filter_none_values(agent_input.watchlist_bic_codes),
            )

            return codes.check()
        except Exception:
            logging.exception(f"For agent input {agent_input}")
            return Result(solution=Solution.AGENT_ERROR)

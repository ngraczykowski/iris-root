import logging

from agent_base.agent import Agent
from attr import attrib, attrs

from data_models.result import Result, Solution
from idmismatchagent.bank_identification_codes import BankIdentificationCodes
from idmismatchagent.utils import _filter_none_values


@attrs(frozen=True)
class BankIdentificationCodesAgentInput:
    altered_party_matching_field: str = attrib()
    watchlist_matching_text: str = attrib()
    watchlist_type: str = attrib()
    watchlist_search_codes: list = attrib()
    watchlist_bic_codes: list = attrib()


class BankIdentificationCodesAgent(Agent):
    def resolve(self, agent_input: BankIdentificationCodesAgentInput) -> Result:
        try:
            codes = BankIdentificationCodes(
                agent_input.altered_party_matching_field,
                agent_input.watchlist_matching_text,
                agent_input.watchlist_type,
                _filter_none_values(agent_input.watchlist_search_codes),
                _filter_none_values(agent_input.watchlist_bic_codes),
            )

            return codes.check()
        except Exception:
            logging.exception(f"For agent input {agent_input}")
            return Result(solution=Solution.AGENT_ERROR)

from typing import Sequence

from company_name.agent.company_name_agent_exchange import CompanyNameAgentExchange
from company_name.base.agent import Agent
from company_name.solution.resolver import Resolver, Result


class CompanyNameAgent(Agent):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.resolve_callback = Resolver(self.configuration_dir).resolve
        self.agent_exchange = CompanyNameAgentExchange(
            config=self.config, callback=self.resolve_callback
        )

    def resolve(self, ap_names: Sequence[str], mp_names: Sequence[str]) -> Result:
        return self.resolve_callback(ap_names, mp_names)

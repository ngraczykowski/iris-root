from typing import Sequence

from agent_base.agent import Agent

from company_name.solution.resolver import Resolver, Result


class CompanyNameAgent(Agent):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.resolver = Resolver(self.config)

    def resolve(self, ap_names: Sequence[str], mp_names: Sequence[str]) -> Result:
        return self.resolver.resolve(ap_names, mp_names)

from agent_base.agent_exchange import AgentExchange
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentOutput


class BankIdentificationCodesAgentExchange(AgentExchange):
    def _create_agent_output_feature(self, feature, result):
        return AgentOutput.Feature(
            feature=feature,
            feature_solution=AgentOutput.FeatureSolution(
                solution=result.solution.value, reason=result.reason
            ),
        )

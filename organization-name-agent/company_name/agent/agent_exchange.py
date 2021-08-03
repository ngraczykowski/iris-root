from agent_base.agent_exchange import AgentExchange
from google.protobuf.struct_pb2 import Struct
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentOutput

from company_name.utils.json_encoder import simplify


class CompanyNameAgentExchange(AgentExchange):
    def _create_agent_output_feature(self, feature, result):
        reason = Struct()
        reason.update(simplify(result.reason))
        return AgentOutput.Feature(
            feature=feature,
            feature_solution=AgentOutput.FeatureSolution(
                solution=result.solution.value, reason=reason
            ),
        )

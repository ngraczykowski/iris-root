from agent_base.agent_exchange.agent_data_source import AgentDataSource
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest
from silenteight.datasource.api.name.v1.name_pb2 import (
    BatchGetMatchNameInputsRequest,
    BatchGetMatchNameInputsResponse,
    NameFeatureInput,
)
from silenteight.datasource.api.name.v1.name_pb2_grpc import NameInputServiceStub


class CompanyNameAgentDataSource(AgentDataSource):
    async def start(self):
        await super().start()
        stub = NameInputServiceStub(self.channel)
        self.command = stub.BatchGetMatchNameInputs

    def prepare_request(self, request: AgentExchangeRequest) -> BatchGetMatchNameInputsRequest:
        return BatchGetMatchNameInputsRequest(
            matches=request.matches,
            features=request.features,
        )

    def parse_response(self, response: BatchGetMatchNameInputsResponse):
        for name_input in response.name_inputs:
            match = name_input.match
            for feature_input in name_input.name_feature_inputs:
                feature = feature_input.feature
                if feature_input.alerted_party_type not in (
                    NameFeatureInput.EntityType.ORGANIZATION,
                    NameFeatureInput.EntityType.ENTITY_TYPE_UNSPECIFIED,
                ):
                    args = ((), ())
                else:
                    args = (
                        [name.name for name in feature_input.alerted_party_names],
                        [name.name for name in feature_input.watchlist_names],
                    )
                yield match, feature, args

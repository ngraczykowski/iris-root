import logging
import sys

from agent_base.agent_exchange.agent_data_source import AgentDataSource
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest
from silenteight.datasource.api.hittype.v1.hit_type_pb2 import (
    BatchGetMatchHitTypeInputsRequest,
    BatchGetMatchHitTypeInputsResponse,
)
from silenteight.datasource.api.hittype.v1.hit_type_pb2_grpc import HitTypeInputServiceStub

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler(sys.stdout)
c_handler.setLevel(logging.DEBUG)


class HitTypeAgentDataSource(AgentDataSource):
    async def start(self):
        await super().start()
        stub = HitTypeInputServiceStub(self.channel)
        self.channel_stream_method = stub.BatchGetMatchHitTypeInputs

    def prepare_request(self, request: AgentExchangeRequest) -> BatchGetMatchHitTypeInputsRequest:

        return BatchGetMatchHitTypeInputsRequest(
            matches=request.matches,
            features=request.features,
        )

    def parse_string_list(self, item):
        return list(item.tokens)

    def parse_token_map(self, item):
        dict_ = {}
        for token in item.tokens_map:
            triggers = item.tokens_map[token]
            dict_[token] = self.parse_string_list(triggers)
        return dict_

    def parse_response(self, response: BatchGetMatchHitTypeInputsResponse):
        for hit_type_input in response.hit_type_inputs:
            match = hit_type_input.match

            for feature_input in hit_type_input.hit_type_feature_inputs:
                feature = feature_input.feature
                try:
                    args = (
                        [i for i in feature_input.normal_trigger_categories],
                        {
                            key: self.parse_string_list(category)
                            for key, category in feature_input.trigger_categories.items()
                        },
                        {
                            key: self.parse_token_map(category)
                            for key, category in feature_input.triggered_tokens.items()
                        },
                    )
                except (KeyError, IndexError, TypeError):
                    args = ((), (), ())

                yield match, feature, args

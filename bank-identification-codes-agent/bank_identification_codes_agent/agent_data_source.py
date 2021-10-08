from agent_base.agent_exchange.agent_data_source import AgentDataSource
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from bank_identification_codes_pb2 import (
    BatchGetMatchBankIdentificationCodesInputsRequest,
    BatchGetMatchBankIdentificationCodesInputsResponse,
)
from bank_identification_codes_pb2_grpc import BankIdentificationCodesInputServiceStub


class BankIdentificationCodesAgentDataSource(AgentDataSource):
    async def start(self):
        await super().start()
        stub = BankIdentificationCodesInputServiceStub(self.channel)
        self.command = stub.BatchGetMatchBankIdentificationCodesInputs

    def prepare_request(
        self, request: AgentExchangeRequest
    ) -> BatchGetMatchBankIdentificationCodesInputsRequest:
        return BatchGetMatchBankIdentificationCodesInputsRequest(
            matches=request.matches, features=[request.features]
        )

    def parse_response(self, response: BatchGetMatchBankIdentificationCodesInputsResponse):
        for name_input in response.bank_identification_codes_inputs:
            match = name_input.match
            for feature_input in name_input.bank_identification_codes_feature_inputs:
                feature = feature_input.feature
                args = (
                    feature_input.alerted_party_matching_field,
                    feature_input.watchlist_matching_text,
                    feature_input.watchlist_type,
                    feature_input.watchlist_search_codes,
                    feature_input.watchlist_bic_codes,
                )
                yield match, feature, args

import logging
import sys

from agent_base.grpc_service.servicer import AgentGrpcServicer

from hit_type.solution.solution import Result
from temp_agent.hit_type_agent_pb2 import (
    DESCRIPTOR,
    CheckTriggersRequest,
    CheckTriggersResponse,
    HitTypesReason,
)
from temp_agent.hit_type_agent_pb2_grpc import (
    HitTypeAgentServicer,
    add_HitTypeAgentServicer_to_server,
)

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler(sys.stdout)
c_handler.setLevel(logging.DEBUG)


class HitTypeAgentGrpcServicer(HitTypeAgentServicer, AgentGrpcServicer):
    name = DESCRIPTOR.services_by_name["HitTypeAgent"].full_name

    def parse_string_list(self, item):
        return [token for token in item.tokens]

    def parse_token_map(self, item):
        dict_ = {}
        for token in item.tokens_map:
            triggers = item.tokens_map[token]
            dict_[token] = {token: self.parse_string_list(triggers)}
        return dict_

    async def CheckTriggers(self, request: CheckTriggersRequest, _context) -> CheckTriggersResponse:
        normal_trigger_categories = [i for i in request.normal_trigger_categories]
        trigger_categories = {
            key: self.parse_string_list(category)
            for key, category in request.trigger_categories.items()
        }
        triggered_tokens = {
            key: self.parse_token_map(category)
            for key, category in request.triggered_tokens.items()
        }
        args = (normal_trigger_categories, trigger_categories, triggered_tokens)
        result: Result = await self.create_resolve_task(*args)
        return CheckTriggersResponse(
            solution=result.solution.value,
            reason=HitTypesReason(
                hit_categories=result.hit_categories,
                normal_categories=result.normal_categories,
            ),
        )

    def add_to_server(self, server):
        add_HitTypeAgentServicer_to_server(self, server)

from agent_base.grpc_service.servicer import AgentGrpcServicer

from idmismatchagent.identification_mismatch_agent_pb2 import (
    DESCRIPTOR,
    CheckIdentificationMismatchRequest,
    CheckIdentificationMismatchResponse,
    CheckIdentificationMismatchReason,
)
from idmismatchagent.identification_mismatch_agent_pb2_grpc import (
    IdentificationMismatchAgentServicer,
    add_IdentificationMismatchAgentServicer_to_server,
)
from result import Result


class IdentificationMismatchAgentGrpcServicer(
    IdentificationMismatchAgentServicer, AgentGrpcServicer
):
    name = DESCRIPTOR.services_by_name["IdentificationMismatchAgent"].full_name

    async def CheckIdentificationMismatch(
        self, request: CheckIdentificationMismatchRequest, _context
    ) -> CheckIdentificationMismatchResponse:
        result: Result = await self.create_resolve_task(request)
        reason = CheckIdentificationMismatchReason(**vars(result.reason))
        return CheckIdentificationMismatchResponse(solution=result.solution.value, reason=reason)

    def add_to_server(self, server):
        add_IdentificationMismatchAgentServicer_to_server(self, server)

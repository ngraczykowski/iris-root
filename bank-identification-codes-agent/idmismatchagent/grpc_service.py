from agent_base.grpc_service.servicer import AgentGrpcServicer

from idmismatchagent.identification_mismatch_agent_pb2 import (
    DESCRIPTOR,
    CheckIdentificationMismatchReason,
    CheckIdentificationMismatchRequest,
    CheckIdentificationMismatchResponse,
)
from idmismatchagent.identification_mismatch_agent_pb2_grpc import (
    IdentificationMismatchAgentServicer,
    add_IdentificationMismatchAgentServicer_to_server,
)
from idmismatchagent.result import Result


class IdentificationMismatchAgentGrpcServicer(
    IdentificationMismatchAgentServicer, AgentGrpcServicer
):
    name = DESCRIPTOR.services_by_name["IdentificationMismatchAgent"].full_name

    async def CheckIdentificationMismatch(
        self, request: CheckIdentificationMismatchRequest, _context
    ) -> CheckIdentificationMismatchResponse:
        result: Result = await self.create_resolve_task(request)
        reason = CheckIdentificationMismatchReason(
            conclusion=result.reason.__class__.__name__, **vars(result.reason)
        )
        return CheckIdentificationMismatchResponse(solution=result.solution.value, reason=reason)

    def add_to_server(self, server):
        add_IdentificationMismatchAgentServicer_to_server(self, server)

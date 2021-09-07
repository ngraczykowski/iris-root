from agent_base.grpc_service.servicer import AgentGrpcServicer

from id_mismatch_agent_pb2 import (
    DESCRIPTOR,
    CheckIdMismatchRequest,
    CheckIdMismatchResponse,
)
from id_mismatch_agent_pb2_grpc import (
    IdMismatchAgentServicer,
    add_IdMismatchAgentServicer_to_server,
)
from result import Result


class CompanyNameAgentGrpcServicer(IdMismatchAgentServicer, AgentGrpcServicer):
    name = DESCRIPTOR.services_by_name["IdMismatchAgent"].full_name

    async def CompareOrganizationNames(
        self, request: CheckIdMismatchRequest, _context
    ) -> CheckIdMismatchResponse:
        result: Result = await self.create_resolve_task(request)
        return result

    def add_to_server(self, server):
        add_IdMismatchAgentServicer_to_server(self, server)

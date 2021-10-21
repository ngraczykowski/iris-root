from agent_base.grpc_service.servicer import AgentGrpcServicer

from company_name_surrounding.company_name_surrounding_agent_pb2 import (
    DESCRIPTOR,
    CheckCompanyNameSurroundingRequest,
    CheckCompanyNameSurroundingResponse,
)
from company_name_surrounding.company_name_surrounding_agent_pb2_grpc import (
    CompanyNameSurroundingAgentServicer,
    add_CompanyNameSurroundingAgentServicer_to_server,
)


class CompanyNameSurroundingAgentGrpcServicer(
    CompanyNameSurroundingAgentServicer, AgentGrpcServicer
):
    name = DESCRIPTOR.services_by_name["CompanyNameSurroundingAgent"].full_name

    async def CheckCompanyNameSurrounding(
        self, request: CheckCompanyNameSurroundingRequest, _context
    ) -> CheckCompanyNameSurroundingResponse:

        names = request.names
        result = await self.create_resolve_task(names)
        return CheckCompanyNameSurroundingResponse(
            names=names,
            result=result,
        )

    def add_to_server(self, server):
        add_CompanyNameSurroundingAgentServicer_to_server(self, server)

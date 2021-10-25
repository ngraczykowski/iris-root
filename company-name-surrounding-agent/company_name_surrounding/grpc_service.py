from agent_base.grpc_service.servicer import AgentGrpcServicer
from silenteight.agent.companynamesurrounding.v1.api.company_name_surrounding_agent_pb2 import (
    DESCRIPTOR,
    CheckCompanyNameSurroundingRequest,
    CheckCompanyNameSurroundingResponse,
)
from silenteight.agent.companynamesurrounding.v1.api.company_name_surrounding_agent_pb2_grpc import (
    CompanyNameSurroundingAgentServicer,
    add_CompanyNameSurroundingAgentServicer_to_server,
)

from company_name_surrounding.data_models import Result


class CompanyNameSurroundingAgentGrpcServicer(
    CompanyNameSurroundingAgentServicer, AgentGrpcServicer
):
    name = DESCRIPTOR.services_by_name["CompanyNameSurroundingAgent"].full_name

    async def CheckCompanyNameSurrounding(
        self, request: CheckCompanyNameSurroundingRequest, _context
    ) -> CheckCompanyNameSurroundingResponse:

        names = request.names
        result: Result = await self.create_resolve_task(names)
        return CheckCompanyNameSurroundingResponse(
            names=names,
            result=result.count,
            solution=result.solution,
        )

    def add_to_server(self, server):
        add_CompanyNameSurroundingAgentServicer_to_server(self, server)

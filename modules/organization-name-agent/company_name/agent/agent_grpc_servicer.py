from agent_base.grpc_service.servicer import AgentGrpcServicer
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2 import (
    DESCRIPTOR,
    CompareOrganizationNamesReason,
    CompareOrganizationNamesRequest,
    CompareOrganizationNamesResponse,
)
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2_grpc import (
    OrganizationNameAgentServicer,
    add_OrganizationNameAgentServicer_to_server,
)

from company_name.solution.solution import Result
from company_name.utils import simplify


class CompanyNameAgentGrpcServicer(OrganizationNameAgentServicer, AgentGrpcServicer):
    name = DESCRIPTOR.services_by_name["OrganizationNameAgent"].full_name

    async def CompareOrganizationNames(
        self, request: CompareOrganizationNamesRequest, _context
    ) -> CompareOrganizationNamesResponse:
        names = list(request.alerted_party_names), list(request.watchlist_party_names)
        result: Result = await self.create_resolve_task(*names)
        return CompareOrganizationNamesResponse(
            solution=result.solution.value,
            reason=CompareOrganizationNamesReason(
                results=[
                    CompareOrganizationNamesReason.CompareOrganizationNamesResult(
                        alerted_party_name=pair_result.alerted_party_name,
                        watchlist_party_name=pair_result.watchlist_party_name,
                        solution=simplify(pair_result.solution),
                        solution_probability=pair_result.solution_probability,
                    )
                    for pair_result in result.reason.results
                ]
            ),
        )

    def add_to_server(self, server):
        add_OrganizationNameAgentServicer_to_server(self, server)

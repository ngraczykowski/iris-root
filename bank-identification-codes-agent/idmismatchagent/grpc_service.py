from agent_base.grpc_service.servicer import AgentGrpcServicer
from silenteight.agent.bankidentificationcodes.v1.api.bank_identification_codes_agent_pb2 import (
    DESCRIPTOR,
    CheckBankIdentificationCodesReason,
    CheckBankIdentificationCodesRequest,
    CheckBankIdentificationCodesResponse,
)
from silenteight.agent.bankidentificationcodes.v1.api.bank_identification_codes_agent_pb2_grpc import (
    BankIdentificationCodesAgentServicer,
    add_BankIdentificationCodesAgentServicer_to_server,
)

from idmismatchagent.result import Result


class BankIdentificationCodesAgentGrpcServicer(
    BankIdentificationCodesAgentServicer, AgentGrpcServicer
):
    name = DESCRIPTOR.services_by_name["BankIdentificationCodesAgent"].full_name

    async def CheckBankIdentificationCodes(
        self, request: CheckBankIdentificationCodesRequest, _context
    ) -> CheckBankIdentificationCodesResponse:
        result: Result = await self.create_resolve_task(request)
        reason = CheckBankIdentificationCodesReason(
            conclusion=result.reason.__class__.__name__, **vars(result.reason)
        )
        return CheckBankIdentificationCodesResponse(solution=result.solution.value, reason=reason)

    def add_to_server(self, server):
        add_BankIdentificationCodesAgentServicer_to_server(self, server)

import logging
import sys

from agent_base.grpc_service.servicer import GrpcServicer
from agent_base.utils import Config

from company_name.agent.agent import CompanyNameAgent
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    DESCRIPTOR,
    ChangeOrganizationNameAgentConfigRequest,
    ChangeOrganizationNameAgentConfigResponse,
    Solution,
)
from company_name.agent.config_service.organization_name_agent_config_pb2_grpc import (
    OrganizationNameAgentConfigServicer,
    add_OrganizationNameAgentConfigServicer_to_server,
)
from company_name.solution.name_preconditions import AlphabetRule, InclusionRule, LengthRule
from company_name.solution.scores_reduction import FeatureRule, ModelRule, ModelSolutionRule
from company_name.solution.sklearn_model import SklearnModel

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler(sys.stdout)
c_handler.setLevel(logging.DEBUG)


class CompanyNameAgentConfigServicer(OrganizationNameAgentConfigServicer, GrpcServicer):
    def __init__(self, agent: CompanyNameAgent):
        self.agent = agent

    name = DESCRIPTOR.services_by_name["OrganizationNameAgentConfig"].full_name

    async def ChangeOrganizationNameAgentConfig(
        self, request: ChangeOrganizationNameAgentConfigRequest, _context
    ) -> ChangeOrganizationNameAgentConfigResponse:

        try:
            if any(
                (request.name_length_rule, request.name_alphabet_rule, request.name_inclusion_rule)
            ):
                await self.change_name_preconditions(request)

            if any((request.feature_rules, request.model_solution_rules, request.model_path)):
                await self.change_reduction_rules(request)

            logger.info(f"Successfully set rules: {request}")
            return ChangeOrganizationNameAgentConfigResponse(
                status=ChangeOrganizationNameAgentConfigResponse.Status.OK
            )
        except Exception as exc:
            logger.error(exc)
            return ChangeOrganizationNameAgentConfigResponse(
                status=ChangeOrganizationNameAgentConfigResponse.Status.ERROR
            )

    async def change_name_preconditions(self, request: ChangeOrganizationNameAgentConfigRequest):
        if request.name_length_rule.max_length and request.name_length_rule.max_unique_tokens:
            self.agent.name_preconditions.preconditions_rules["length"] = LengthRule(
                rules_config={
                    "max_length": request.name_length_rule.max_length,
                    "max_unique_tokens": request.name_length_rule.max_unique_tokens,
                }
            )
        if request.name_inclusion_rule:
            self.agent.name_preconditions.preconditions_rules["inclusion"] = InclusionRule(
                rules_config={
                    "without_part": list(request.name_inclusion_rule.without_part),
                    "without_token": list(request.name_inclusion_rule.without_token),
                }
            )
        if request.name_alphabet_rule:
            self.agent.name_preconditions.preconditions_rules["alphabet"] = AlphabetRule(
                rules_config={
                    "min_acceptable_fraction": request.name_alphabet_rule.min_acceptable_fraction,
                    "acceptable_alphabets": list(request.name_alphabet_rule.acceptable_alphabets),
                }
            )

    async def change_reduction_rules(self, request: ChangeOrganizationNameAgentConfigRequest):

        if request.feature_rules:
            feature_rules = [
                FeatureRule(
                    feature=rule.feature,
                    solution=Solution.Name(rule.solution),
                    solution_probability=rule.solution_probability,
                    threshold=rule.threshold,
                )
                for rule in request.feature_rules
            ]
            self.agent.reduction.rules = feature_rules + [
                rule for rule in self.agent.reduction.rules if isinstance(rule, ModelRule)
            ]
        if request.model_solution_rules:
            model_solution_rules = [
                ModelSolutionRule(
                    solution=Solution.Name(rule.solution),
                    threshold=rule.threshold,
                    label=rule.label,
                )
                for rule in request.model_solution_rules
            ]
        else:
            model_solution_rules = next(
                rule for rule in self.agent.reduction.rules if isinstance(rule, ModelRule)
            ).solutions

        if request.model_path:
            model_path = request.model_path
        else:
            model_path = next(
                rule for rule in self.agent.reduction.rules if isinstance(rule, ModelRule)
            ).source

        if request.model_path or request.model_solution_rules:
            model_rule = ModelRule(
                source=model_path,
                solutions=model_solution_rules,
                model=SklearnModel(path=Config().get_config_path(model_path, required=True)),
            )
            self.agent.reduction.rules = [
                rule for rule in self.agent.reduction.rules if isinstance(rule, FeatureRule)
            ] + [model_rule]

    def add_to_server(self, server):
        add_OrganizationNameAgentConfigServicer_to_server(self, server)

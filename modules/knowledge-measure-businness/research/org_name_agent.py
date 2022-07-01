import os
from typing import List, Union

import grpc
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    ChangeOrganizationNameAgentConfigRequest,
)
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    FeatureRule as RequestFeatureRule,
)
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    ModelSolutionRule as RequestModelSolutionRule,
)
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    NameAlphabetRule as RequestNameAlphabetRule,
)
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    NameInclusionRule as RequestNameInclusionRule,
)
from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    NameLengthRule as RequestNameLengthRule,
)
from company_name.agent.config_service.organization_name_agent_config_pb2_grpc import (
    OrganizationNameAgentConfigStub,
)
from company_name.solution.scores_reduction import FeatureRule, ModelSolutionRule

from business_layer.temp_org_proto.organization_name_agent_pb2 import (
    CompareOrganizationNamesRequest,
)
from business_layer.temp_org_proto.organization_name_agent_pb2_grpc import OrganizationNameAgentStub
from research.name_preconditions_rules import (
    AlphabetRuleConfig,
    InclusionRuleConfig,
    LengthRuleConfig,
)

DEFAULT_CONFIG_ABSPATH = os.path.abspath("research/config")


class OrgNameAgentService:
    def __init__(self):
        channel = grpc.insecure_channel("localhost:9090")
        self.config_stub = OrganizationNameAgentConfigStub(channel)
        self.call_stub = OrganizationNameAgentStub(channel)

    def change_config(
        self,
        feature_rules: List[FeatureRule] = (),
        model_solution_rules: List[ModelSolutionRule] = (),
        model_path: str = "",
        name_alphabet_rule: AlphabetRuleConfig = None,
        name_inclusion_rule: InclusionRuleConfig = None,
        name_length_rule: LengthRuleConfig = None,
    ):

        if feature_rules:
            feature_rules = [
                RequestFeatureRule(
                    threshold=rule.threshold,
                    feature=rule.feature,
                    solution=rule.solution.value,
                    solution_probability=rule.solution_probability,
                )
                for rule in feature_rules
            ]
        if model_solution_rules:
            msr = []
            for rule in model_solution_rules:
                val = RequestModelSolutionRule(
                    threshold=rule.threshold,
                    solution=rule.solution.value,
                    label=rule.label,
                )
                msr.append(val)
            model_solution_rules = msr

        if name_alphabet_rule:
            name_alphabet_rule = RequestNameAlphabetRule(**name_alphabet_rule.dict())

        if name_inclusion_rule:
            name_inclusion_rule = RequestNameInclusionRule(**name_inclusion_rule.dict())

        if name_length_rule:
            name_length_rule = RequestNameLengthRule(**name_length_rule.dict())

        config_request = {
            "feature_rules": feature_rules,
            "model_solution_rules": model_solution_rules,
            "model_path": model_path,
            "name_alphabet_rule": name_alphabet_rule,
            "name_inclusion_rule": name_inclusion_rule,
            "name_length_rule": name_length_rule,
        }
        request = ChangeOrganizationNameAgentConfigRequest(**config_request)
        response = self.config_stub.ChangeOrganizationNameAgentConfig(request)
        return response

    def resolve(self, ap_names: Union[List[str], str], wl_names: List[str]):
        request = CompareOrganizationNamesRequest(
            alerted_party_names=ap_names, watchlist_party_names=wl_names
        )
        response = self.call_stub.CompareOrganizationNames(request)
        return response

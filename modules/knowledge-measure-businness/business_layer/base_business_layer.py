from abc import ABC, abstractmethod
from typing import Any, Dict, List, Tuple

from business_layer.api import FieldMeasure, PolicyStep, ValueKnowledge
from business_layer.config import Config
from business_layer.errors import PolicyStepsError


class BaseBusinessLayer(ABC):
    def __init__(self, config_parameters: Dict[str, Any]):
        self.config = Config(config_parameters)

    def provide_custom_measures(
        self, data=None, knowledge=None, measures=None
    ) -> Dict[str, FieldMeasure]:
        custom_tools = self.config.measure_toolbox.custom_features
        results = {}
        for feature_name, custom_tool in custom_tools.items():
            field_measure = custom_tool.run(data=data, knowledge=knowledge, measures=measures)
            field_measure.domain_name = custom_tool.config.domain.name
            field_measure.ignore = custom_tool.config.domain.ignore
            results[feature_name] = field_measure
        return results

    @staticmethod
    def get_feature_vector(measures: Dict[str, FieldMeasure]) -> Dict[str, str]:
        feature_vector = {}
        for measure_domain, field_measure in measures.items():
            if not field_measure.ignore:
                feature_vector[measure_domain] = field_measure.recommendation
        return feature_vector

    def validate_policy_steps(self, policy_steps: List[Dict[str, Any]]) -> List[PolicyStep]:
        validated = []
        for step in policy_steps:
            policy_step = PolicyStep(**step)
            decision_type = policy_step.decision
            if decision_type not in self.config.decisions:
                raise PolicyStepsError(
                    f"Policy step type {decision_type} not match config decisions!"
                )

            for condition in policy_step.conditions:
                for feature in condition.keys():
                    if feature not in self.config.requested_mappings:
                        raise PolicyStepsError(
                            f"Policy step feature {feature} not match config feature mapping!"
                        )
            validated.append(policy_step)
        return validated

    @staticmethod
    def find_matching_policy(
        feature_vector: Dict[str, str], policy_steps: List[PolicyStep]
    ) -> Tuple[Dict[str, List[str]], str]:

        for policy_step in policy_steps:
            for condition in policy_step.conditions:
                conditions_met = []
                for feature, values in condition.items():
                    conditions_met.append(feature_vector[feature] in values)
                if all(conditions_met):
                    return condition, policy_step.decision
        return {}, "MI"

    @abstractmethod
    def get_comment(
        self, decision: str, condition: Dict[str, Any], measures: Dict[str, FieldMeasure]
    ) -> str:
        return


class BaseBusinessLayerNS(BaseBusinessLayer):
    @abstractmethod
    def provide_measures(
        self, data: Dict[str, List[str]], knowledge: Any
    ) -> Dict[str, FieldMeasure]:
        return


class BaseBusinessLayerTS(BaseBusinessLayer):
    @abstractmethod
    def provide_knowledge(self, data: Dict[str, str]) -> Dict[str, List[ValueKnowledge]]:
        return

    @abstractmethod
    def provide_measures(self, data: Dict[str, str], knowledge: Any) -> Dict[str, FieldMeasure]:
        return

    @abstractmethod
    def apply_domain_hierarchy(self, knowledge, measures):
        return

from typing import Any, Dict, List

from business_layer.api import FieldMeasure, SolvedHit, ValueKnowledge
from business_layer.base_business_layer import BaseBusinessLayerTS
from business_layer.comment.comment import CommentGenerator
from business_layer.config.datatypes import DomainType
from business_layer.errors import SolveInputDataError
from business_layer.message_dispatchers import KnowledgeMessageDispatcher, MeasureMessageDispatcher
from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory


class BusinessLayerTS(BaseBusinessLayerTS):
    def __init__(self, config_parameters: Dict[str, Any]):
        super().__init__(config_parameters=config_parameters)
        knowledge_service_wrappers = ServiceWrapperFactory().create_wrappers(
            self.config.knowledge_toolbox.standard_features_configs, DomainType.KNOWLEDGE
        )
        measure_service_wrappers = ServiceWrapperFactory().create_wrappers(
            self.config.measure_toolbox.standard_features_configs, DomainType.MEASURE
        )

        self.knowledge_dispatcher = KnowledgeMessageDispatcher(knowledge_service_wrappers)
        self.measure_dispatcher = MeasureMessageDispatcher(measure_service_wrappers)
        self.comment_generator = CommentGenerator(self.config)

    def provide_service_knowledge(self, data: Dict[str, str]) -> Dict[str, List[ValueKnowledge]]:
        results = {}
        for service_name, _ in self.config.knowledge_toolbox.standard_features_configs.items():
            try:
                payload = data["msg"]
            except KeyError:
                raise SolveInputDataError

            value_knowledge = self.knowledge_dispatcher.knowledge_call(
                f"{service_name}_knowledge", payload=payload
            )
            results[service_name] = value_knowledge
        return results

    def provide_custom_knowledge(self, data: Dict[str, str]) -> Dict[str, List[ValueKnowledge]]:
        results = {}
        for feature_name, feature_obj in self.config.knowledge_toolbox.custom_features.items():
            value_knowledge = feature_obj.run(data=data)
            results[feature_name] = value_knowledge
        return results

    def provide_knowledge(self, data: Dict[str, str]) -> Dict[str, List[ValueKnowledge]]:
        knowledge = self.provide_service_knowledge(data)
        knowledge.update(self.provide_custom_knowledge(data))
        return knowledge

    def provide_service_measures(self, data: Dict[str, str]):
        results = {}
        for (
            service_name,
            service_config,
        ) in self.config.measure_toolbox.standard_features_configs.items():
            context = service_config.context
            field = f"wl_{service_config.field}"
            try:
                ap_payload = data["msg"]
                wl_payload = [data[field]]
            except KeyError:
                raise SolveInputDataError

            field_measure = self.measure_dispatcher.measure_call(
                f"{service_name}_measure", ap_payload, wl_payload, context
            )
            field_measure.domain_name = service_config.domain["name"]
            results[service_name] = field_measure
        return results

    def provide_measures(self, data: Dict[str, str], knowledge=None):
        measures = self.provide_service_measures(data)
        custom_measures = self.provide_custom_measures(data, knowledge, measures)
        measures.update(custom_measures)
        return measures

    def apply_domain_hierarchy(self, knowledge, measures):
        """An example of applying domain hierarchy"""

        for value_measure in measures["geo_location"].results:
            for value_knowledge in knowledge["org_name_msg"]:
                if value_measure.ap_value.lower() in value_knowledge.original_input.lower():
                    value_measure.ignore = True
                    value_measure.ignore_reason = "org_name"
                    break

            for value_knowledge in knowledge["identity_msg"]:
                if value_measure.ap_value.lower() in value_knowledge.original_input.lower():
                    value_measure.ignore = True
                    value_measure.ignore_reason = "identity"
                    break

    def get_comment(
        self, decision: str, condition: Dict[str, Any], measures: Dict[str, FieldMeasure]
    ) -> str:
        return self.comment_generator.generate(decision, condition, measures)

    def solve_hit(self, data: Dict[str, str], policy_steps: List[Dict[Any, Any]]):
        policy_steps = self.validate_policy_steps(policy_steps)

        knowledge = self.provide_knowledge(data)
        measures = self.provide_measures(data)
        self.apply_domain_hierarchy(knowledge, measures)
        feature_vector = self.get_feature_vector(measures)

        matching_policy_condition, decision = self.find_matching_policy(
            feature_vector, policy_steps
        )

        comment = self.get_comment(decision, matching_policy_condition, measures)

        return SolvedHit(feature_vector=feature_vector, decision=decision, comment=comment)

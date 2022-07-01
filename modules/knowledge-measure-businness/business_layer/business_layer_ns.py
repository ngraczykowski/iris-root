from typing import Any, Dict, List, Union

from business_layer.api import FieldMeasure, SolvedHit
from business_layer.base_business_layer import BaseBusinessLayerNS
from business_layer.comment.comment import CommentGenerator
from business_layer.config.datatypes import DomainType
from business_layer.errors import SolveInputDataError
from business_layer.message_dispatchers import MeasureMessageDispatcher
from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory


class BusinessLayerNS(BaseBusinessLayerNS):
    def __init__(self, config_parameters: Dict[str, Any]):
        super().__init__(config_parameters=config_parameters)
        service_wrappers = ServiceWrapperFactory().create_wrappers(
            self.config.measure_toolbox.standard_features_configs, DomainType.MEASURE
        )
        self.measure_dispatcher = MeasureMessageDispatcher(service_wrappers)
        self.comment_generator = CommentGenerator(self.config)

    def provide_service_measures(self, data: Dict[str, List[str]]) -> Dict[str, FieldMeasure]:
        results = {}
        for (
            service_name,
            service_config,
        ) in self.config.measure_toolbox.standard_features_configs.items():
            context = service_config.context
            field = service_config.field
            try:
                ap_payload = data[f"ap_all_{field}_aggregated"]
                wl_payload = data[f"wl_all_{field}_aggregated"]
            except KeyError:
                raise SolveInputDataError

            field_measure = self.measure_dispatcher.measure_call(
                f"{service_name}_measure", ap_payload, wl_payload, context
            )
            field_measure.domain_name = service_config.domain["name"]
            field_measure.ignore = service_config.domain["ignore"]
            results[service_name] = field_measure
        return results

    def provide_measures(
        self, data: Dict[str, List[str]], knowledge=None
    ) -> Dict[str, FieldMeasure]:
        measures = self.provide_service_measures(data)
        custom_measures = self.provide_custom_measures(
            data=data,
            knowledge=knowledge,
            measures=measures,
        )
        measures.update(custom_measures)
        return measures

    def get_comment(
        self, decision: str, condition: Dict[str, List[Union[str, FieldMeasure]]], measures
    ) -> str:
        return self.comment_generator.generate(decision, condition, measures)

    def solve_hit(
        self, data: Dict[str, List[str]], policy_steps: List[Dict[Any, Any]]
    ) -> SolvedHit:
        policy_steps = self.validate_policy_steps(policy_steps)

        measures = self.provide_measures(data)
        feature_vector = self.get_feature_vector(measures)

        matching_policy_condition, decision = self.find_matching_policy(
            feature_vector, policy_steps
        )

        comment = self.get_comment(decision, matching_policy_condition, measures)

        return SolvedHit(feature_vector=feature_vector, decision=decision, comment=comment)

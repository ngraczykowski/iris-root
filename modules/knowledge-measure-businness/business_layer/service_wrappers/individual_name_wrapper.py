from typing import List

from silenteight.agent.name.v1.api.name_agent_pb2 import (
    CompareNamesInput,
    CompareNamesRequest,
    CompareNamesResponse,
)
from silenteight.agent.name.v1.api.name_agent_pb2_grpc import NameAgentStub

from business_layer.api import FieldMeasure, ValueMeasure
from business_layer.service_wrappers.base_wrappers import AbstractMeasureStubWrapper


class IndividualNameMeasureStubWrapper(AbstractMeasureStubWrapper):
    def __init__(self, stub: NameAgentStub):
        self.stub: NameAgentStub = stub

    def measure_call(
        self, ap_payload: List[str], wl_payload: List[str], context: str
    ) -> FieldMeasure:
        """
        This method ap input may be a list of strings - names, or a string - freetext.
        Using freetext, it's needed to parse it using org_knowledge call.
        """

        request = CompareNamesRequest(
            instance_name="advname-indv",
            exclude_reason=False,
            inputs=[
                CompareNamesInput(
                    alerted_names=ap_payload,
                    watchlist_names=[{"name": wl_name, "type": "name"} for wl_name in wl_payload],
                )
            ],
        )

        response_stream = self.stub.CompareNames(request)
        response: CompareNamesResponse = next(response_stream)
        ap_value = " | ".join(sorted(set(list(response.reason.alerted_values))))
        results = [
            ValueMeasure(
                ap_value=ap_value,
                wl_value=" | ".join(sorted(set([name.name for name in wl_group.watchlist_names]))),
                evaluation=wl_group.result,
            )
            for wl_group in response.reason.matched_value_groups
        ]

        return FieldMeasure(recommendation=response.result, results=results, context=context)

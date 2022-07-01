from typing import List

from silenteight.agent.geo.v1.api.common_pb2 import GeoComparerReason, LocationType
from silenteight.agent.geo.v1.api.geo_agent_pb2 import CompareLocationsRequest
from silenteight.agent.geo.v1.api.geo_agent_pb2_grpc import GeoLocationAgentStub
from silenteight.agent.geo.v1.api.geo_location_extractor_pb2 import ExtractLocationRequest
from silenteight.agent.geo.v1.api.geo_location_extractor_pb2_grpc import GeoLocationExtractorStub

from business_layer.api import FieldMeasure, ValueKnowledge, ValueMeasure
from business_layer.service_wrappers.base_wrappers import (
    AbstractKnowledgeStubWrapper,
    AbstractMeasureStubWrapper,
)


class GeoKnowledgeStubWrapper(AbstractKnowledgeStubWrapper):
    def __init__(self, stub: GeoLocationExtractorStub):
        self.stub: GeoLocationExtractorStub = stub

    def knowledge_call(self, payload: str) -> List[ValueKnowledge]:
        request = ExtractLocationRequest(value=payload)
        response = self.stub.ExtractLocation(request)
        output = []
        for location in response.identified_locations:
            output.append(
                ValueKnowledge(original_input=location.original_input, results=location.location)
            )
        return output


class GeoMeasureStubWrapper(AbstractMeasureStubWrapper):
    def __init__(self, stub: GeoLocationAgentStub):
        self.stub: GeoLocationAgentStub = stub

    def measure_call(self, ap_payload: str, wl_payload: str, context) -> FieldMeasure:
        """
        Arguments for single (non-batch) call of this method must be strings.
        Even if multiple values are to be passed on either the AP or the WL
        side as lists,  they will be forcefully converted to strings.
        This is necessary because of the reduction logic in the geo service.
        """
        if isinstance(ap_payload, list):
            ap_payload = " ".join(ap_payload)
        if isinstance(wl_payload, list):
            wl_payload = " ".join(wl_payload)

        request = CompareLocationsRequest(
            alerted_party_location=ap_payload, watchlist_party_location=wl_payload
        )
        response = self.stub.CompareLocations(request)
        results = []
        for result in response.reason.geo_compare_results:
            location_type = LocationType.Name(result.type)
            match_type = GeoComparerReason.MatchType.Name(result.match_type)
            evaluation = f"{location_type}_{match_type}"
            results.append(
                ValueMeasure(
                    ap_value=result.alerted_party_value,
                    wl_value=result.watchlist_party_value,
                    evaluation=evaluation,
                )
            )
        return FieldMeasure(recommendation=response.solution, results=results, context=context)

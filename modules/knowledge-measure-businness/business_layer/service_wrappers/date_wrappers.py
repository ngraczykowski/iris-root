from typing import Generator, List

from silenteight.agent.date.v2.api.date_agent_pb2 import (
    ExtractAndMeasureRequest,
    ExtractAndMeasureResponse,
    ExtractFactsRequest,
    ExtractFactsResponse,
)
from silenteight.agent.date.v2.api.date_agent_pb2_grpc import DateAgentStub

from business_layer.api import FieldMeasure, ValueKnowledge, ValueMeasure
from business_layer.service_wrappers.base_wrappers import (
    AbstractKnowledgeStubWrapper,
    AbstractMeasureStubWrapper,
)


class DateKnowledgeStubWrapper(AbstractKnowledgeStubWrapper):
    def __init__(self, stub: DateAgentStub):
        self.stub: DateAgentStub = stub

    def knowledge_call(self, payload: str) -> List[ValueKnowledge]:
        requests_stream = (ExtractFactsRequest(freetext=payload, id="123") for _ in range(1))

        responses = self.stub.ExtractFacts(requests_stream)
        response: ExtractFactsResponse = next(responses)
        output = [
            ValueKnowledge(
                original_input=result.original_input,
                results=[getattr(rv, "date" or "range") for rv in result.recognized_values],
            )
            for result in response.results
        ]

        return output

    def knowledge_streaming_call(
        self, freetext_collection: List[str]
    ) -> List[List[ValueKnowledge]]:

        requests_stream = (
            ExtractFactsRequest(freetext=freetext, id=str(index))
            for index, freetext in enumerate(freetext_collection)
        )
        responses_stream: Generator[ExtractFactsResponse, None, None] = self.stub.ExtractFacts(
            requests_stream
        )
        results_with_indexes = []
        for response in responses_stream:
            response_results = [
                ValueKnowledge(
                    original_input=result.original_input,
                    results=[getattr(rv, "date" or "range") for rv in result.recognized_values],
                )
                for result in response.results
            ]
            results_with_indexes.append({"id": response.id, "results": response_results})
        return [result["results"] for result in sorted(results_with_indexes, key=lambda x: x["id"])]


class DateMeasureStubWrapper(AbstractMeasureStubWrapper):
    def __init__(self, stub: DateAgentStub):
        self.stub: DateAgentStub = stub

    def measure_call(self, ap_payload: str, wl_payload: str, context) -> FieldMeasure:
        requests_stream = (
            ExtractAndMeasureRequest(first_freetext=ap_payload, second_freetext=wl_payload)
            for _ in range(1)
        )
        responses = self.stub.ExtractAndMeasure(requests_stream)
        response: ExtractAndMeasureResponse = next(responses)
        results = []
        status = ExtractAndMeasureResponse.ExtractAndMeasureStatus.Name(response.overall_status)
        for result in response.results:
            results.append(
                ValueMeasure(
                    ap_value=getattr(result.inputs.first, "date" or "range"),
                    wl_value=getattr(result.inputs.second, "date" or "range"),
                    evaluation=status,
                    metrics=result.measurements,
                )
            )
        return FieldMeasure(recommendation=status, results=results, context=context)

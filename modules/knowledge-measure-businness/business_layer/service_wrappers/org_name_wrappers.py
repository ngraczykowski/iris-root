from typing import List, Union

import requests

from business_layer.api import FieldMeasure, ValueKnowledge, ValueMeasure
from business_layer.service_wrappers.base_wrappers import (
    AbstractKnowledgeStubWrapper,
    AbstractMeasureStubWrapper,
)
from business_layer.temp_org_proto.organization_name_agent_pb2 import (
    CompareOrganizationNamesRequest,
    CompareOrganizationNamesResponse,
)
from business_layer.temp_org_proto.organization_name_agent_pb2_grpc import OrganizationNameAgentStub


class OrgNameKnowledgeStubWrapper(AbstractKnowledgeStubWrapper):
    def __init__(self, server: str, port: int):
        self.url = ":".join((server, str(port)))

    def knowledge_call(self, payload: str) -> List[ValueKnowledge]:
        response = requests.get(
            url=self.url + f"/organization_name_knowledge/parse_freetext/{payload}"
        )
        return [
            ValueKnowledge(original_input=result["source"]["original"], results=result)
            for result in response.json()
        ]


class OrgNameMeasureStubWrapper(AbstractMeasureStubWrapper):
    def __init__(self, stub: OrganizationNameAgentStub, org_knowledge: OrgNameKnowledgeStubWrapper):
        self.stub: OrganizationNameAgentStub = stub
        self.org_knowledge = org_knowledge

    def measure_call(
        self, ap_payload: Union[str, List[str]], wl_payload: List[str], context: str
    ) -> FieldMeasure:
        """
        This method ap input may be a list of strings - names, or a string - freetext.
        Using freetext, it's needed to parse it using org_knowledge call.
        """

        if isinstance(ap_payload, str):
            ap_payload = self._get_names_from_freetext(ap_payload)

        request = CompareOrganizationNamesRequest(
            alerted_party_names=ap_payload, watchlist_party_names=wl_payload
        )
        response: CompareOrganizationNamesResponse = self.stub.CompareOrganizationNames(request)
        results = [
            ValueMeasure(
                ap_value=result.alerted_party_name,
                wl_value=result.watchlist_party_name,
                evaluation=result.solution,
                metrics=result.scores,
            )
            for result in response.reason.results
        ]

        return FieldMeasure(recommendation=response.solution, results=results, context=context)

    def _get_names_from_freetext(self, freetext: str) -> List[str]:
        names = [
            name.original_input for name in self.org_knowledge.knowledge_call(payload=freetext)
        ]
        if not names:  # when input is a company name, but not parsed in freetext - i.e. base only
            names = [freetext]
        return names

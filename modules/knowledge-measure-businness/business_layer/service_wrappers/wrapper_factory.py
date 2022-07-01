import re
from typing import Dict, List, Union

import grpc
from silenteight.agent.date.v2.api.date_agent_pb2_grpc import DateAgentStub
from silenteight.agent.geo.v1.api.geo_agent_pb2_grpc import GeoLocationAgentStub
from silenteight.agent.geo.v1.api.geo_location_extractor_pb2_grpc import GeoLocationExtractorStub
from silenteight.agent.name.v1.api.name_agent_pb2_grpc import NameAgentStub

from business_layer.config.datatypes import DomainType
from business_layer.service_wrappers.base_wrappers import (
    AbstractKnowledgeStubWrapper,
    AbstractMeasureStubWrapper,
)
from business_layer.service_wrappers.date_wrappers import (
    DateKnowledgeStubWrapper,
    DateMeasureStubWrapper,
)
from business_layer.service_wrappers.geo_wrappers import (
    GeoKnowledgeStubWrapper,
    GeoMeasureStubWrapper,
)
from business_layer.service_wrappers.individual_name_wrapper import IndividualNameMeasureStubWrapper
from business_layer.service_wrappers.org_name_wrappers import (
    OrgNameKnowledgeStubWrapper,
    OrgNameMeasureStubWrapper,
)
from business_layer.temp_org_proto.organization_name_agent_pb2_grpc import OrganizationNameAgentStub


class ServiceWrapperFactory:
    @staticmethod
    def get_stub(cls, address, port):
        channel = grpc.insecure_channel(target=("{}:{}".format(address, port)))
        return cls(channel)

    def create_wrappers(
        self, requested_services: List[str], service_type: DomainType
    ) -> Dict[str, Union[AbstractKnowledgeStubWrapper, AbstractMeasureStubWrapper]]:

        wrappers = {}
        for requested_service in requested_services:
            service_name = f"{requested_service}_{service_type.value}"
            wrappers[service_name] = self.create_wrapper(service_name)
        return wrappers

    def create_wrapper(
        self, name: str
    ) -> Union[AbstractKnowledgeStubWrapper, AbstractMeasureStubWrapper]:

        if re.search("geo[_a-z]*_measure", name):
            return GeoMeasureStubWrapper(self.get_stub(GeoLocationAgentStub, "localhost", 24317))
        elif re.search("geo[_a-z]*_knowledge", name):
            return GeoKnowledgeStubWrapper(
                self.get_stub(GeoLocationExtractorStub, "localhost", 24317)
            )
        elif re.search("org_name[_a-z]*_measure", name):
            return OrgNameMeasureStubWrapper(
                stub=self.get_stub(OrganizationNameAgentStub, "localhost", 9090),
                org_knowledge=OrgNameKnowledgeStubWrapper(server="http://localhost", port=5000),
            )
        elif re.search("org_name[_a-z]*_knowledge", name):
            return OrgNameKnowledgeStubWrapper(server="http://localhost", port=5000)

        elif re.search("individual_name[_a-z]*_measure", name):
            return IndividualNameMeasureStubWrapper(
                stub=self.get_stub(NameAgentStub, "localhost", 5001),
            )

        elif re.search("date[_a-z]*_measure", name):
            return DateMeasureStubWrapper(self.get_stub(DateAgentStub, "localhost", 24313))
        elif re.search("date[_a-z]*_knowledge", name):
            return DateKnowledgeStubWrapper(self.get_stub(DateAgentStub, "localhost", 24313))

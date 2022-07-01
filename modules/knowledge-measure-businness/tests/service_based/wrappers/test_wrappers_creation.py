import unittest
from typing import List, Union

from business_layer.config.datatypes import DomainType
from business_layer.service_wrappers.base_wrappers import (
    AbstractKnowledgeStubWrapper,
    AbstractMeasureStubWrapper,
)
from business_layer.service_wrappers.geo_wrappers import (
    GeoKnowledgeStubWrapper,
    GeoMeasureStubWrapper,
)
from business_layer.service_wrappers.org_name_wrappers import (
    OrgNameKnowledgeStubWrapper,
    OrgNameMeasureStubWrapper,
)
from business_layer.service_wrappers.wrapper_factory import ServiceWrapperFactory


class CreateWrappersTests(unittest.TestCase):
    def test_create(self):
        services = ["geo", "org_name"]
        wrapper_factory = ServiceWrapperFactory()

        knowledge_wrappers = wrapper_factory.create_wrappers(
            requested_services=services, service_type=DomainType.KNOWLEDGE
        )
        measure_wrappers = wrapper_factory.create_wrappers(
            requested_services=services, service_type=DomainType.MEASURE
        )
        self._check_wrappers_created(
            wrapper_instances=list(knowledge_wrappers.values()),
            class_names=[GeoKnowledgeStubWrapper, OrgNameKnowledgeStubWrapper],
        )
        self._check_wrappers_created(
            wrapper_instances=list(measure_wrappers.values()),
            class_names=[GeoMeasureStubWrapper, OrgNameMeasureStubWrapper],
        )

    @staticmethod
    def _check_wrappers_created(
        wrapper_instances: List[Union[AbstractKnowledgeStubWrapper, AbstractMeasureStubWrapper]],
        class_names: List[type],
    ):
        assert len(wrapper_instances) == len(class_names)
        for wrapper, class_name in zip(wrapper_instances, class_names):
            assert isinstance(wrapper, class_name)

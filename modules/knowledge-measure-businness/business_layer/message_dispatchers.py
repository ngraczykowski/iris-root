from typing import Dict, List

from business_layer.api import FieldMeasure, ValueKnowledge
from business_layer.service_wrappers.base_wrappers import (
    AbstractKnowledgeStubWrapper,
    AbstractMeasureStubWrapper,
)


class KnowledgeMessageDispatcher:
    def __init__(self, wrappers: Dict[str, AbstractKnowledgeStubWrapper]):
        self.wrappers = wrappers

    def knowledge_call(self, wrapper_name: str, *args, **kwargs) -> List[ValueKnowledge]:
        return self.wrappers[wrapper_name].knowledge_call(*args, **kwargs)


class MeasureMessageDispatcher:
    def __init__(self, wrappers: Dict[str, AbstractMeasureStubWrapper]):
        self.wrappers = wrappers

    def measure_call(self, wrapper_name: str, *args, **kwargs) -> FieldMeasure:
        return self.wrappers[wrapper_name].measure_call(*args, **kwargs)

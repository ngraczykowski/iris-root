from abc import ABC
from typing import List, Union

from business_layer.api import FieldMeasure, ValueKnowledge


class AbstractKnowledgeStubWrapper(ABC):
    def knowledge_call(self, payload: str) -> List[ValueKnowledge]:
        return


class AbstractMeasureStubWrapper(ABC):
    def measure_call(
        self, ap_payload: Union[str, List[str]], wl_payload: Union[str, List[str]], context: str
    ) -> FieldMeasure:
        return

import re
from typing import List

from business_layer.api import BaseCustomKnowledge, ValueKnowledge

patterns = {"IBAN": r"\b[A-Z]{2}[0-9]{2}[A-Z0-9]{1,35}", "PESEL": r"\b[0-9]{11}"}


class Identity(BaseCustomKnowledge):
    def __init__(self, domain_config_parameters):
        self.config = domain_config_parameters
        self.patterns = patterns

    def run(self, data) -> List[ValueKnowledge]:
        text = data["msg"]
        found_patterns = []
        for pattern_name, pattern in self.patterns.items():
            found = re.search(pattern, text)
            if found:
                found_patterns.append(
                    ValueKnowledge(original_input=found.group(), results=pattern_name)
                )

        return found_patterns

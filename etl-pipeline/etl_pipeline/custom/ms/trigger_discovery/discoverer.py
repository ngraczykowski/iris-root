import re
from typing import Any, Dict, List, NamedTuple

from fuzzywuzzy import fuzz

MINIMUM_LENGHTH = 3


class NamedValues(NamedTuple):
    values: Dict[str, List[str]]


cleaner = re.compile(r"\W")


class TriggeredTokensDiscoverer:
    def __init__(self, fuzzy_threshold: int):
        self.fuzzy_threshold = fuzzy_threshold

    def discover(
        self, matched_tokens: List[str], values_dict: Dict[str, Any]
    ) -> Dict[str, Dict[str, List[str]]]:

        result = {}
        for whole_token in matched_tokens:
            for token in whole_token.split():
                result[token] = {}
                for key, values in values_dict.items():
                    result[token][key] = []
                    if values is None:
                        del result[token][key]
                        continue

                    for value in [values] if isinstance(values, str) else values:
                        if value is None:
                            continue
                        ratio = fuzz.partial_ratio(value.lower(), token.lower())
                        if ratio > self.fuzzy_threshold:
                            result[token][key].append(value)

                    if len(result[token][key]) == 0:
                        del result[token][key]
        return result

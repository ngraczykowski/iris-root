from typing import Any, Dict, List, NamedTuple

from fuzzywuzzy import fuzz


class NamedValues(NamedTuple):
    values: Dict[str, List[str]]


class TriggeredTokensDiscoverer:
    def __init__(self, fuzzy_threshold: int):
        self.fuzzy_threshold = fuzzy_threshold

    def discover(
        self, matched_tokens: List[str], values_dict: Dict[str, Any]
    ) -> Dict[str, Dict[str, List[str]]]:

        result = {}
        for whole_token in matched_tokens:
            for token in whole_token.split():
                result[token] = self.find_tokens(values_dict, token)

        return result

    def find_tokens(self, values_dict, token):
        token_dict = {}
        for key, values in values_dict.items():
            if values is None:
                continue

            for value in [values] if isinstance(values, str) else values:
                if value:
                    ratio = fuzz.partial_ratio(value.lower(), token.lower())
                    if ratio > self.fuzzy_threshold:
                        if not token_dict.get(key):
                            token_dict[key] = []
                        token_dict[key].append(value)

        return token_dict

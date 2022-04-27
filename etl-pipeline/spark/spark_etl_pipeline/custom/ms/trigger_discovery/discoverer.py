import re
from typing import Any, Dict, List, NamedTuple

from fuzzywuzzy import fuzz


class NamedValues(NamedTuple):
    values: Dict[str, List[str]]


cleaner = re.compile(r"\W")


class TriggeredTokensDiscoverer:
    def __init__(self, fuzzy_threshold: int):
        self.fuzzy_threshold = fuzzy_threshold

    def discover(  # noqa C901
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

                        clean_value = cleaner.sub("", value)
                        clean_token = cleaner.sub("", token)
                        # naive checking of matching
                        if (
                            abs(len(clean_value) - len(clean_token))
                            / max([len(clean_value), len(clean_token)])
                            < 0.5
                        ):
                            continue
                        elif (clean_value.lower() in clean_token.lower()) or (
                            clean_token.lower() in clean_value.lower()
                        ):
                            result[token][key].append(value)

                    if len(result[token][key]) == 0:
                        del result[token][key]
        for match in result:
            for key in result[match]:
                result[match][key] = [i for i in result[match][key] if i]

        for match in result:
            result[match] = {
                key: result[match][key]
                for key in result[match]
                if result[match][key]
                if result[match][key]
            }

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

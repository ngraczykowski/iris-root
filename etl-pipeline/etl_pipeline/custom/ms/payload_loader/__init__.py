import collections.abc
import json
import re


class PayloadLoader:
    LIST_ELEMENT_REGEX = re.compile(r"\[(\d+)\]")

    def get_index(self, value):
        return [idx for idx, val in enumerate(value) if val][0]

    def collect_list(self, dictionary_to_be_updated, key, value):
        if dictionary_to_be_updated.get(key, None) is None:
            dictionary_to_be_updated[key] = []
        try:
            idx = self.get_index(value)
            try:
                value = self.deep_update(dictionary_to_be_updated[key][idx], value[idx])
                dictionary_to_be_updated[key][idx] = value
            except IndexError:
                dictionary_to_be_updated[key].append(value[idx])
        except IndexError:
            dictionary_to_be_updated[key].extend(value)
        return dictionary_to_be_updated[key]

    def deep_update(self, dictionary_to_be_updated, updater):
        for key, value in updater.items():
            if isinstance(value, collections.abc.Mapping):
                dictionary_to_be_updated[key] = self.deep_update(
                    dictionary_to_be_updated.get(key, {}), value
                )
            elif isinstance(value, list):
                dictionary_to_be_updated[key] = self.collect_list(
                    dictionary_to_be_updated, key, value
                )
            else:
                dictionary_to_be_updated[key] = value
        return dictionary_to_be_updated

    def jsonify(self, value):
        if isinstance(value, str) and value.startswith("["):
            return json.loads(value)
        return value

    def get_list_and_temporary_key(self, new_key, extracted_value):
        temporary_key = self.LIST_ELEMENT_REGEX.sub("", new_key)
        idx = int(self.LIST_ELEMENT_REGEX.search(new_key).groups()[0])
        values = [type(extracted_value)() for _ in range(idx + 1)]
        values[idx] = extracted_value
        return {temporary_key: values}

    def extract_dict(self, splitted_keys, value):
        is_list_array = False
        dict_ = {}

        try:
            new_key, next_keys = splitted_keys[0], splitted_keys[1:]
            if next_keys is []:
                raise IndexError
            if self.LIST_ELEMENT_REGEX.findall(new_key):
                extracted_value, is_list_array = self.extract_dict(next_keys, value)
                extracted_value = self.jsonify(extracted_value)
                return self.get_list_and_temporary_key(new_key, extracted_value), is_list_array
            else:
                extracted_value, is_list_array = self.extract_dict(next_keys, value)
                extracted_value = self.jsonify(extracted_value)
                if is_list_array:
                    dict_[new_key] = self.get_list_and_temporary_key(next_keys, extracted_value)
                    is_list_array = False
                else:
                    dict_[new_key] = self.jsonify(extracted_value)
        except IndexError:
            extracted_value = self.jsonify(value)
            if len(splitted_keys) == 1:
                new_key = splitted_keys[0]
                if self.LIST_ELEMENT_REGEX.findall(new_key):
                    dict_ = self.get_list_and_temporary_key(new_key, extracted_value)
                else:
                    dict_[splitted_keys[0]] = self.jsonify(value)
            else:
                dict_[splitted_keys[0]] = self.jsonify(extracted_value)

        return dict_, is_list_array

    def load_payload_from_json(self, payload):
        new_payload = {}
        for key, value in payload.items():
            splitted_keys = key.split(".")
            new_dict, is_list_array = self.extract_dict(splitted_keys, value)
            new_payload = self.deep_update(new_payload, new_dict)
        return new_payload

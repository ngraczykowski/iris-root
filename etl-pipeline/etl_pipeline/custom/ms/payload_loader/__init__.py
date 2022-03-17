import collections.abc
import json
import re


class PayloadLoader:
    # TODO needs bigger refactor + tests
    LIST_TYPE_REGEX = re.compile(r"\[(\d+)\]")

    def get_index_of_element(self, value):
        return [idx for idx, val in enumerate(value) if val][0]

    def add_new_elements(self, dictionary_to_be_updated, value, idx):
        for _ in range(idx - len(dictionary_to_be_updated) + 1):
            dictionary_to_be_updated.append(type(dictionary_to_be_updated[0])())
        try:
            if isinstance(value[idx], dict):
                self.deep_update(dictionary_to_be_updated[idx], value[idx])
            if isinstance(value[idx], str):
                dictionary_to_be_updated[idx] = value[idx]
        except IndexError:
            dictionary_to_be_updated[idx].append(value)

    def update_or_create_new_elements(self, list_to_be_updated, value, idx):
        try:
            value = self.deep_update(list_to_be_updated[idx], value[idx])
            list_to_be_updated[idx] = value
        except IndexError:
            self.add_new_elements(list_to_be_updated, value, idx)

    def collect_list(self, dictionary_to_be_updated, key, value):
        if dictionary_to_be_updated.get(key, None) is None:
            dictionary_to_be_updated[key] = []
        try:
            idx = self.get_index_of_element(value)
            array_element = dictionary_to_be_updated[key]
            self.update_or_create_new_elements(array_element, value, idx)
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

    def remove_array_bracket_and_get_index_of_element(self, string):
        return self.LIST_TYPE_REGEX.sub("", string), int(
            self.LIST_TYPE_REGEX.search(string).groups()[0]
        )

    def create_dict_with_array_value(self, new_key, extracted_value):
        key, idx = self.remove_array_bracket_and_get_index_of_element(new_key)
        fake_list = [type(extracted_value)() for _ in range(idx + 1)]
        fake_list[idx] = extracted_value
        return {key: fake_list}

    def extract_dict(self, splitted_keys, value):
        extracted_value_is_array = False
        dictionary = {}
        try:
            new_key, next_keys = splitted_keys[0], splitted_keys[1:]
            if next_keys is []:
                raise IndexError
            extracted_value, extracted_value_is_array = self.extract_dict(next_keys, value)
            extracted_value = self.jsonify(extracted_value)
            if self.LIST_TYPE_REGEX.findall(new_key):
                return (
                    self.create_dict_with_array_value(new_key, extracted_value),
                    extracted_value_is_array,
                )
            else:
                if extracted_value_is_array:
                    dictionary[new_key] = self.create_dict_with_array_value(
                        next_keys, extracted_value
                    )
                    extracted_value_is_array = False
                else:
                    dictionary[new_key] = extracted_value
        except IndexError:
            self.update_dictionary_with_value_or_list(dictionary, value, splitted_keys)

        return dictionary, extracted_value_is_array

    def update_dictionary_with_value_or_list(self, dict_, value, splitted_keys):
        extracted_value = self.jsonify(value)
        if len(splitted_keys) == 1:
            new_key = splitted_keys[0]

            if self.LIST_TYPE_REGEX.findall(new_key):
                dict_.update(self.create_dict_with_array_value(new_key, extracted_value))
            else:
                dict_[splitted_keys[0]] = self.jsonify(value)
        else:
            dict_[splitted_keys[0]] = self.jsonify(extracted_value)

    def load_payload_from_json(self, payload):
        new_payload = {}
        for key, value in payload.items():
            splitted_keys = key.split(".")
            extracted_dict, _ = self.extract_dict(splitted_keys, value)
            new_payload = self.deep_update(new_payload, extracted_dict)
        return new_payload

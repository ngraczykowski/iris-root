import json
from collections import OrderedDict

from etl_pipeline.config import columns_namespace as cn


class WatchlistExtractor:
    def as_list(self, x):
        if not x:
            return []
        elif isinstance(x, list):
            return x
        else:
            return [x]

    def extract_dob(self, record):
        result = []
        entry_list = []
        dobs = record.get("entity", {}).get("dobs", {})
        for entry in dobs:
            entry_list.append(dobs[entry])
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is OrderedDict:
                result.append(item["#text"])
            else:
                result.append(item)
        return result

    def extract_nationality(self, record):
        result = []
        entry_list = []
        for entry in record.get("entity", {}).get("nationalities", []):
            entry_list.append(entry.get("nationality", ""))
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is OrderedDict:
                result.append(item["#text"])
            else:
                result.append(item)
        return result

    def extract_citizenships(self, record):
        result = []
        entry_list = []
        for entry in record.get("entity", {}).get("citizenships", []):
            entry_list.append(entry.get("citizenship", ""))
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is OrderedDict:
                result.append(item["#text"])
            else:
                result.append(item)
        return result

    def extract_wl_data_by_path(self, record, field1, field2):
        result = []
        entry_list = []
        for entry in record.get("entity", {}).get(field1, []):
            entry_list.append(entry.get(field2, ""))
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is OrderedDict:
                result.append(item["#text"])
            else:
                result.append(item)
        return result

    def extract_wl_addresses(self, record):
        result = {}
        addresses = []
        entity = record.get("entity", {})
        if "addresses" in entity and "address" in entity["addresses"]:
            addresses = self.as_list(record["entity"]["addresses"]["address"])
        idx = 0
        for item in addresses:
            if type(item) is OrderedDict:
                for k, v in item.items():
                    full_key = "WL_" + k.upper()
                    if full_key not in result:
                        result[full_key] = []
                    result[full_key].append(v)
            else:
                result["WL_ADDRESS" + str(idx)] = item
                idx += 1

        for k, v in result.items():
            if isinstance(v, list):
                result[k] = json.dumps(v)

        return result

    def extract_wl_routing_codes(self, record):
        routing_codes_dict = {}
        if (
            "entity" in record
            and "routingCodes" in record["entity"]
            and "routingCode" in record["entity"]["routingCodes"]
        ):
            routing_codes = self.as_list(record["entity"]["routingCodes"]["routingCode"])
            for routing_code in routing_codes:
                key_name = "WL_ROUTING_CODE_" + routing_code["@type"].replace(" ", "_")
                if key_name not in routing_codes_dict:
                    routing_codes_dict[key_name] = []

                routing_codes_dict[key_name].append(routing_code["#text"])

        # Convert lists to JSON
        for k, v in routing_codes_dict.items():
            routing_codes_dict[k] = json.dumps(v)

        return routing_codes_dict

    def extract_wl_matched_tokens(self, payload):
        input_tokens = []
        for descriptor in payload.get("stopDescriptors", {}):
            details = descriptor.get("stopDescriptorDetail", {})
            input_tokens.append(details.get("inputToken", ""))
        return {cn.WL_MATCHED_TOKENS: json.dumps(input_tokens)}

    def extract_country(self, match):
        return match.get("entity", {}).get("addresses", {}).get("address", {}).get("country")

    def extract_country_name(self, match):
        return match.get("entity", {}).get("addresses", {}).get("address", {}).get("countryName")

    def update_match_with_wl_values(self, match):
        wl_record_data = {
            "SRC_REF_KEY": match.get("uniqueCustomerId", ""),
            "VERSION_ID": match.get("inputVersionId", ""),
            "ENTITY_ID": match.get("entityId", ""),
            "ENTITY_VERSION": match.get("entityVersion", ""),
            "WL_NAME": match.get("entity", {}).get("name", ""),
            "WL_DOB": self.extract_dob(match),
            "WL_ENTITYTYPE": match.get("entityType", ""),
            "WL_COUNTRY": self.extract_country(match),
            "WL_COUNTRY_NAME": self.extract_country_name(match),
            "WL_NATIONALITY": self.extract_nationality(match),
            "WL_CITIZENSHIP": self.extract_citizenships(match),
            "WL_POB": self.extract_wl_data_by_path(match, "pobs", "pob"),
            "WL_ALIASES": self.extract_wl_data_by_path(match, "aliases", "alias"),
        }
        wl_record_data.update(self.extract_wl_addresses(match))
        wl_record_data.update(self.extract_wl_matched_tokens(match))
        wl_record_data.update(self.extract_wl_routing_codes(match))

        try:
            wl_record_data["WL_DOCUMENT_NUMBER"] = self.extract_wl_data_by_path(
                match, ["ids", "id"]
            )
        except (KeyError, TypeError):
            wl_record_data["WL_DOCUMENT_NUMBER"] = ""

        match.update(wl_record_data)

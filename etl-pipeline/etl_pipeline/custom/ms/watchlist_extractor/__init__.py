import json

# from collections import OrderedDict
import re

from etl_pipeline.config import columns_namespace as cn


class WatchlistExtractor:
    def as_list(self, x):
        if not x:
            return []
        elif isinstance(x, list):
            return x
        else:
            return [x]

    """def extract_dob(self, record):
        result = []
        entry_list = []
        dobs = record.get("entity", {}).get("dobs", {})
        for entry in dobs:
            entry_list.append(dobs[entry])
        # data_item_list = self.as_list(entry_list)
        for item in entry_list:
            if type(item) is dict:
                result.append(item.get("#text", item.get("text")))
            else:
                result.append(item)
        return result"""

    def parse_dob_dict(self, dob):
        dmy = ["", "", ""]
        result = []
        date_range = []
        for k, v in dob.items():
            if k.upper() == "Y":
                dmy[-1] = v
            elif k.upper() == "M":
                dmy[1] = v
            elif k.upper() == "D":
                dmy[0] = v
            elif k == "S8_extracted_value":
                if " TO " in v.upper():
                    date_range.extend(sorted(re.findall(r"\d\d\d\d", v), key=lambda x: int(x)))
                else:
                    result.append(v)
            else:
                result.append(v)
        return result, date_range, dmy

    def extract_dob(self, record):
        result = []
        entity = record.get("entity", {})
        dobs = entity.get("dobs", [])
        try:
            dob = dobs[0]
        except IndexError:
            dob = {}
        except KeyError:
            dob = dobs

        if isinstance(dob, str):
            return [dob]
        if isinstance(dob, list):
            return dob
        if dob is None:
            return []
        if isinstance(dob, dict):
            result, date_range, dmy = self.parse_dob_dict(dob)
            result.append("/".join(dmy))
            if len(date_range) == 2:
                result.extend(
                    [str(elem) for elem in range(int(date_range[0]), int(date_range[-1]) + 1)]
                )
            return result
        return ""

    def extract_nationality(self, record):
        result = []
        entry_list = []
        entry = record.get("entity", {}).get("nationalities", {})
        try:
            entry_list.append(entry.get("nationality", ""))
        except AttributeError:
            pass
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is dict:
                result.append(item.get("#text", item.get("text")))
            else:
                result.append(item)
        return result

    def extract_citizenships(self, record):
        result = []
        entry_list = []
        entry = record.get("entity", {}).get("citizenships", {})
        try:
            entry_list.append(entry.get("citizenship", ""))
        except AttributeError:
            pass
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is dict:
                result.append(item.get("#text", item.get("text")))
            else:
                result.append(item)
        return result

    def extract_wl_data_by_path(self, record, field1, field2):
        result = []
        entry_list = []
        entry = record.get("entity", {}).get(
            field1, {}
        )  # returning [] by get can cause error in next line
        try:
            destination = entry.get(field2, "")
        except AttributeError:
            destination = []
            pass
        if isinstance(destination, list):
            entry_list.extend(destination)
        else:
            entry_list.append(destination)
        data_item_list = self.as_list(entry_list)
        for item in data_item_list:
            if type(item) is dict:
                result.append(item.get("#text", item.get("text")))
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
            if type(item) is dict:
                for k, v in item.items():
                    full_key = "WL_" + k.upper()
                    if full_key not in result:
                        result[full_key] = []
                    result[full_key].append(v)
            else:
                result["WL_ADDRESS" + str(idx)] = item
                idx += 1

        # for k, v in result.items():
        #     if isinstance(v, list):
        #         result[k] = json.dumps(v)

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
                key_name = "WL_ROUTING_CODE_" + routing_code.get(
                    "@type", routing_code.get("type")
                ).replace(" ", "_")
                if key_name not in routing_codes_dict:
                    routing_codes_dict[key_name] = []
                routing_codes_dict[key_name].append(
                    routing_code.get("#text", routing_code.get("text"))
                )

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
        try:
            address = match.get("entity", {}).get("addresses", {}).get("address")
        except AttributeError:
            address = match.get("entity", {}).get("addresses", {})

        if isinstance(address, dict):
            return address.get("country")
        elif isinstance(address, list):
            countries = []
            for elem in address:
                countries.append(elem.get("country"))
            return "|".join(list(filter(lambda x: x is not None, countries)))

    def extract_country_name(self, match):
        try:
            address = match.get("entity", {}).get("addresses", {}).get("address")
        except AttributeError:
            address = match.get("entity", {}).get("addresses", {})
        if isinstance(address, dict):
            return address.get("countryName")
        elif isinstance(address, list):
            countries = []
            for elem in address:
                countries.append(elem.get("countryName"))
            return "|".join(list(filter(lambda x: x is not None, countries)))

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

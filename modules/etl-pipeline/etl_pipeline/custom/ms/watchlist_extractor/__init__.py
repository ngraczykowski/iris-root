import json
import logging
import re

from etl_pipeline.config import pipeline_config

cn = pipeline_config.cn

logger = logging.getLogger("main").getChild("extractor")


def safe_field_extractor(func):
    def wrap(*args, **kwargs):
        try:
            result = func(*args, **kwargs)
        except Exception as e:
            logger.error(e)
            result = ""
        return result

    return wrap


class WatchlistExtractor:
    def as_list(self, x):
        if not x:
            return []
        elif isinstance(x, list):
            return x
        else:
            return [x]

    @safe_field_extractor
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
            elif k == "S8_extracted_value" or k == "dob":
                if isinstance(v, str):
                    if " TO " in v.upper():
                        date_range.extend(sorted(re.findall(r"\d\d\d\d", v), key=lambda x: int(x)))
                    else:
                        result.append(v)
                if isinstance(v, dict):
                    result.extend(self.parse_dob_dict(v))
            else:
                result.append(v)
        return result, date_range, dmy

    @safe_field_extractor
    def extract_dob(self, record):
        result = []
        entity = record.get("entity", {})
        dobs = entity.get("dobs", [])
        if isinstance(dobs, str):
            return [dobs]
        if not dobs:
            return []
        if not isinstance(dobs, list):
            dobs = [dobs]
        if isinstance(dobs[0], dict):
            for dob in dobs:
                result, date_range, dmy = self.parse_dob_dict(dob)
                result.append("/".join([str(i) for i in dmy if i]))
                if len(date_range) == 2:
                    result.extend(
                        [str(elem) for elem in range(int(date_range[0]), int(date_range[-1]) + 1)]
                    )
            return result
        return ""

    @safe_field_extractor
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

    @safe_field_extractor
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

    @safe_field_extractor
    def extract_wl_data_by_path(self, record, field1, field2):
        entry = record.get("entity", {}).get(field1, {})
        try:
            destination = entry.get(field2, "")
        except AttributeError:
            if isinstance(entry, list):
                results = [self.extract_single_array_element(dest[field2]) for dest in entry]
                return results
            else:
                return []
        return self.extract_single_array_element(destination)

    @safe_field_extractor
    def extract_single_array_element(self, destination):
        result = []
        entry_list = []
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

    @safe_field_extractor
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

        return result

    @safe_field_extractor
    def extract_wl_routing_codes(self, record):
        routing_codes_dict = {}
        if (
            "entity" in record
            and "routingCodes" in record["entity"]
            and "routingCode" in record["entity"]["routingCodes"]
        ):
            routing_codes = self.as_list(record["entity"]["routingCodes"]["routingCode"])
            if routing_codes:
                if isinstance(routing_codes[0], dict):
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

                if isinstance(routing_codes[0], list):
                    routing_codes_dict = []

        return routing_codes_dict

    @safe_field_extractor
    def extract_wl_matched_tokens(self, payload):
        input_tokens = []
        for descriptor in payload.get("stopDescriptors", []):
            details = descriptor.get("stopDescriptorDetails", [])
            for detail in details:
                input_tokens.append(detail.get("inputToken", ""))
        return {cn.WL_MATCHED_TOKENS: json.dumps(input_tokens)}

    @safe_field_extractor
    def extract_country(self, match):
        try:
            address = match.get("entity", {}).get("addresses", {}).get("address")
        except AttributeError:
            address = match.get("entity", {}).get("addresses", {})

        if isinstance(address, dict):
            return address.get("country") or address.get("address2")
        elif isinstance(address, list):
            countries = []
            for elem in address:
                countries.append(elem.get("country"))
                countries.append(elem.get("address2"))
            return "|".join(list(filter(lambda x: x is not None, countries)))

    @safe_field_extractor
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

    @safe_field_extractor
    def extract_wlp_type(self, wl_entitytype):
        entity_type_ind = ["03"]
        entity_type_pep = ["07"]
        entity_type_ind = entity_type_ind + entity_type_pep
        value = "C"
        if wl_entitytype in entity_type_ind:
            value = "I"
        return {"WLP_TYPE": value}

    @safe_field_extractor
    def update_match_with_wl_values(self, match):
        wl_record_data = {
            "SRC_REF_KEY": match.get("uniqueCustomerId", ""),
            "VERSION_ID": match.get(cn.MATCH_RECORD_VERSION_ID, ""),
            "ENTITY_ID": match.get("entityId", ""),
            "ENTITY_VERSION": match.get("entityVersion", ""),
            "WL_NAME": match.get("entity", {}).get("name", ""),
            "WL_DOB": self.extract_dob(match),
            "WL_ENTITYTYPE": match.get("entityType", ""),
            "WL_COUNTRY": self.extract_country(match),
            "WL_COUNTRYNAME": self.extract_country_name(match),
            "WL_NATIONALITY": self.extract_nationality(match),
            "WL_CITIZENSHIP": self.extract_citizenships(match),
            "WL_POB": self.extract_wl_data_by_path(match, "pobs", "pob"),
            "WL_ALIASES": self.extract_wl_data_by_path(match, "aliases", "alias"),
        }
        wl_record_data.update(self.extract_wl_addresses(match))
        wl_record_data.update(self.extract_wl_matched_tokens(match))
        wl_record_data.update(self.extract_wl_routing_codes(match))
        wl_record_data.update(self.extract_wlp_type(wl_record_data["WL_ENTITYTYPE"]))

        try:
            wl_record_data["WL_DOCUMENT_NUMBER"] = self.extract_wl_data_by_path(match, "ids", "id")
        except (KeyError, TypeError):
            wl_record_data["WL_DOCUMENT_NUMBER"] = ""

        match.update(wl_record_data)

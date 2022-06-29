from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.logger import get_logger
from pipelines.ms.collection import Collections

logger = get_logger("main").getChild("Functions")


cn = pipeline_config.cn


def set_scope(func):
    def wrap(*args, **kwargs):
        try:

            scope = kwargs.pop("source", {})
            function_object: Functions = args[0]
            payload = args[1]
            scoped_payload = {}

            for container in scope:
                if container == "alertedParty":
                    collection = payload.get("alertedParty")
                    scoped_payload[str(container)] = scoped_payload.get(str(container), {})
                    for field in scope[container]:
                        scoped_payload[str(container)][field] = collection[field]
                elif container == "metadata":
                    collection = payload
                    scoped_payload[str(container)] = scoped_payload.get(str(container), {})
                    for field in scope[container]:
                        scoped_payload[str(container)][field] = collection[field]
                elif container == "xml_fields":
                    collection = function_object.collections.get_xml_fields(payload)
                    scoped_payload[str(container)] = scoped_payload.get(str(container), {})
                    for field in scope[container]:
                        scoped_payload[str(container)][field] = collection.get(field, None)
                elif container == "alertSupplementalInfo":
                    collection = (
                        payload.get("alertedParty", {})
                        .get("alertSupplementalInfo", {})
                        .get("supplementalInfo", [])
                    )
                    scoped_payload[str(container)] = {"supplementalInfo": collection}
            args = [function_object, scoped_payload]

            func(*args, **kwargs)

            target_collection = kwargs.pop("target_collection", None)
            target_field = kwargs.pop("target_field", None)
            payload[target_collection][target_field] = scoped_payload[target_collection][
                target_field
            ]
        except Exception as e:
            logger.error(f"{str(e)} for {func}")

    return wrap


class Functions:
    collections = Collections()

    def __init__(self, func_maps):
        self.func_maps = func_maps

    def __getattr__(self, name):
        try:
            return self.func_maps.get(name)
        except AttributeError:
            return object.__getattribute__(self, name)

    @classmethod
    def parse_key(cls, value, match, payload, new_config):
        temp_dict = dict(value)
        for new_key in temp_dict:
            for element in temp_dict[new_key]:
                elements = element.split(".")
                if cn.MATCH_RECORDS in element:
                    value = match
                    elements = elements[2:]

                elif "xml_fields" in element:
                    input_records = payload[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][
                        cn.INPUT_RECORDS
                    ]
                    value = input_records[cn.INPUT_FIELD]
                    try:
                        value = value[elements[-1]].value
                    except (AttributeError, KeyError):
                        value = None

                    new_elements = [elements[-1]]
                    elements = []
                else:
                    value = payload
                for field_name in elements:
                    try:
                        value = value.get(field_name, None)
                    except (TypeError, AttributeError):
                        key = PayloadLoader.LIST_TYPE_REGEX.sub("", field_name)
                        ix = int(PayloadLoader.LIST_TYPE_REGEX.match(field_name).groups(0))
                        value = value[key][ix]
                new_config[elements[-1] if elements else new_elements[-1]] = value

    @classmethod
    def _collect_party_dobs(cls, *args, **kwargs):
        cls.pattern_aggregate(*args, **kwargs)

    @classmethod
    def pattern_aggregate(
        self,
        payload,
        target_field="",
        unique=False,
        source=[],
        target_collection=None,
    ):
        aggregated = []
        for collection in source:
            if collection == "parties":
                parties = self.collections.get_parties(payload)
                for field_name in source["parties"]:
                    aggregated.extend(
                        [i.get(field_name, "") for i in parties if i.get(field_name, "")]
                    )
            elif collection == "accounts":
                accounts = self.collections.get_accounts(payload)
                for field_name in source["accounts"]:
                    aggregated.extend(
                        [i.get(field_name, "") for i in accounts if i.get(field_name, "")]
                    )
            elif collection == "xml_fields":
                fields = self.collections.get_xml_fields(payload)
                for field in source[collection]:
                    aggregated.append(fields.get(field).value if fields.get(field) else "")
        aggregated = [i for i in aggregated if i]
        if unique:
            aggregated = list(set([i for i in aggregated if i]))
        if target_collection:
            payload[target_collection][target_field] = aggregated
        else:
            payload[target_field] = aggregated

    @classmethod
    def set_up_for_wm_party(cls, payload):
        ap_type = None
        field = payload["xml_fields"]["PARTY1_ORGANIZATION_NAME"]
        if field:
            ap_type = "I"
            if field.value:
                ap_type = "C"
        return ap_type

    @classmethod
    def set_up_for_isg_account(cls, payload):
        ap_type = None
        supplemental_info = payload["alertSupplementalInfo"]["supplementalInfo"]
        logger.debug(f"supplementalInfo: {supplemental_info}")
        if supplemental_info:
            for info in supplemental_info:
                ap_type = info.get("legalFormName", None)
                break
            logger.debug(f"ap_type: {ap_type}")
            if ap_type == "Individual":
                ap_type = "I"
            elif ap_type:
                ap_type = "C"
        return ap_type

    @classmethod
    def set_up_for_isg_party(cls, payload):
        ap_type = None
        field = payload["xml_fields"]["ORGANIZATIONPERSONIND"]
        if field:
            ap_type = field.value
            if ap_type == "O":
                ap_type = "C"
            elif ap_type == "P":
                ap_type = "I"
        return ap_type

    @classmethod
    def select_ap_for_dataset_type(cls, payload, dataset_type):
        ap_type = None
        if dataset_type == "WM_PARTY":
            ap_type = cls.set_up_for_wm_party(payload)
        elif dataset_type == "ISG_ACCOUNT":
            ap_type = cls.set_up_for_isg_account(payload)
        elif dataset_type == "ISG_PARTY":
            ap_type = cls.set_up_for_isg_party(payload)
        return ap_type if ap_type else "UNKNOWN"

    @classmethod
    @set_scope
    def _set_up_party_type(cls, source_payload, target_field="", target_collection=""):
        party_types = [
            party_type
            for party_type in source_payload["alertedParty"]["AP_PARTY_TYPES"]
            if party_type
        ]
        dataset_type = source_payload["metadata"]["datasetType"]
        value = "UNKNOWN"
        if dataset_type and not party_types:
            value = cls.select_ap_for_dataset_type(source_payload, dataset_type)
        elif party_types:
            if "Individual" in party_types:
                value = "I"
            else:
                value = "C"
        try:
            target = source_payload[target_collection]
        except KeyError:
            source_payload[target_collection] = {}
            target = source_payload[target_collection]
        target[target_field] = value

    @classmethod
    def pattern_set_beneficiary_hits(cls, payload, target_field="", **kwargs):
        payload[target_field] = cn.AD_BNFL_NM in payload[cn.TRIGGERED_BY]

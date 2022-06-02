from etl_pipeline.config import pipeline_config
from etl_pipeline.logger import get_logger
from pipelines.ms.collection import Collections

logger = get_logger("main").getChild("Functions")


cn = pipeline_config.cn


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
                        [i.get(field_name) for i in parties if i.get(field_name, "")]
                    )
            elif collection == "field":
                for field in collection:
                    aggregated.append(collection.get(field))
        if unique:
            aggregated = list(set([i for i in aggregated if i]))
        if target_collection:
            payload[target_collection][target_field] = aggregated
        else:
            payload[target_field] = aggregated

    @classmethod
    def set_up_for_wm_party(cls, payload):
        ap_type = "I"
        field = cls.collections.get_xml_field(payload, "PARTY1_ORGANIZATION_NAME")
        if field:
            ap_type = "C"
        return ap_type

    @classmethod
    def set_up_for_isg_account(cls, payload):
        ap_type = None
        supplemental_info = cls.collections.get_alert_supplemental_info(
            payload, "supplementalInfo"
        )
        logger.debug(f"supplemental_info: {supplemental_info}")
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
        field = cls.collections.get_xml_field(payload, "ORGANIZATIONPERSONIND")
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
        logger.debug(f"ap_type: {ap_type}")
        if dataset_type == "WM_PARTY":
            ap_type = cls.set_up_for_wm_party(payload)
        elif dataset_type == "ISG_ACCOUNT":
            ap_type = cls.set_up_for_isg_account(payload)
        elif dataset_type == "ISG_PARTY":
            ap_type = cls.set_up_for_isg_party(payload)
        logger.debug(f"ap_type: {ap_type}")
        return ap_type if ap_type else "UNKNOWN"

    @classmethod
    def pattern_set_up_party_type(cls, payload, target_field="", source="", target_collection=""):
        field = payload["alertedParty"]["ALL_PARTY_TYPES"]
        types = [i for i in field if i]
        try:
            match_place = payload[target_collection]
        except KeyError:
            payload[target_collection] = {}
            match_place = payload[target_collection]
        dataset_type = payload.get("datasetType")
        match_place[target_field] = "UNKNOWN"
        logger.debug(f"dataset_type: {dataset_type}, types {types}")
        if dataset_type and not types:
            match_place[target_field] = cls.select_ap_for_dataset_type(payload, dataset_type)
        else:
            if "Individual" in types:
                match_place[target_field] = "I"
            else:
                match_place[target_field] = "C"

    @classmethod
    def pattern_set_beneficiary_hits(cls, payload, target_field="", **kwargs):
        payload[target_field] = cn.AD_BNFL_NM in payload[cn.TRIGGERED_BY]

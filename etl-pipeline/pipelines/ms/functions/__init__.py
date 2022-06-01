from etl_pipeline.config import pipeline_config
from pipelines.ms.collection import Collections

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
    def pattern_set_up_party_type(cls, payload, target_field="", source="", target_collection=""):
        for collection in source:
            field_name = source[collection][0]
        field = payload[str(collection)][str(field_name)]
        types = [i for i in field if i]
        try:
            match_place = payload[target_collection]
        except KeyError:
            payload[target_collection] = {}
            match_place = payload[target_collection]
        if not types:
            match_place[target_field] = "UNKNOWN"
        if "Individual" in types:
            match_place[target_field] = "I"
        else:
            match_place[target_field] = "C"

    @classmethod
    def pattern_set_beneficiary_hits(cls, payload, target_field="", **kwargs):
        payload[target_field] = cn.AD_BNFL_NM in payload[cn.TRIGGERED_BY]

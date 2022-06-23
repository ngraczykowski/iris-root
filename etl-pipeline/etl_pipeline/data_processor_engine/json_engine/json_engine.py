import json
import logging
import re
from typing import Dict

from fuzzywuzzy import fuzz

from etl_pipeline.config import Pipeline, pipeline_config
from etl_pipeline.custom.ms.trigger_discovery.discoverer import TriggeredTokensDiscoverer
from etl_pipeline.data_processor_engine.engine.engine import ProcessingEngine

logger = logging.getLogger("main").getChild("engine")
cn = pipeline_config.cn


def safe_field_extractor(func):
    def wrap(*args, **kwargs):
        try:
            result = func(*args, **kwargs)
        except Exception as e:
            logger.error(f"{str(e)} for {func}")
            result = ""
        return result

    return wrap


COLLECTIVE_REPRESENTATION_MAP_FOR_ACCOUNTS = {
    cn.ALL_CONNECTED_ACCOUNT_NAMES: cn.CONNECTED_FULL_NAME,
    cn.ALL_CONNECTED_ACCOUNT_BRANCH_ACCOUNT_NUMBERS: cn.BRANCH_ACCOUNT_NUMBER,
    cn.ALL_CONNECTED_ACCOUNT_BENEFICIARY_NAMES: cn.BENEFICIARY_NAME,
}

COLLECTIVE_REPRESENTATION_MAP_FOR_FIELD = {
    cn.ALL_PARTY1_EMPLOYERS: cn.EMPLOYER,
    cn.ALL_PARTY1_COUNTRY: cn.COUNTRY,
    cn.ALL_PARTY1_COUNTRY1: cn.COUNTRY1,
    cn.ALL_PARTY1_COUNTRY_OF_INCORPORATION: cn.COUNTRY_OF_INCORPORATION,
    cn.ALL_PARTY1_ADDRESS1_COUNTRY: cn.ADDRESS1_COUNTRY,
    cn.ALL_PARTY1_COUNTRY1_CITIZENSHIP: cn.COUNTRY1_CITIZENSHIP,
    cn.ALL_PARTY1_COUNTRY2_CITIZENSHIP: cn.COUNTRY2_CITIZENSHIP,
    cn.ALL_PARTY1_COUNTRY_FORMATION1: cn.COUNTRY_FORMATION1,
    cn.ALL_PARTY1_COUNTRY_DOMICILE1: cn.COUNTRY_DOMICILE1,
    cn.ALL_PARTY1_COUNTRY_OF_INCORPORATION: cn.COUNTRY_OF_INCORPORATION,
    cn.ALL_PRTY_PRIM_CTZNSH_CNTRY: cn.PRTY_PRIM_CTZNSH_CNTRY,
    cn.ALL_PRTY_RSDNC_CNTRY_CD: cn.PRTY_RSDNC_CNTRY_CD,
    cn.ALL_PARTY1_COUNTRY_PEP: cn.PARTY1_COUNTRY_PEP,
    cn.ALL_CONCAT_NAMES: cn.CONCAT_NAME,
    cn.ALL_PARTY1_GOVTID1_NUMBER: cn.PARTY1_GOVTID1_NUMBER,
    cn.ALL_PARTY1_GOVTID2_NUMBER: cn.PARTY1_GOVTID2_NUMBER,
}


class JsonProcessingEngine(ProcessingEngine):
    REF_KEY_REGEX = r"(\d{4}-\d{2}-\d{2}-\d{2}.\d{2}.\d{2}.\d{6})"
    REF_KEY_REGEX_PYTHON = re.compile(REF_KEY_REGEX)
    discoverer = TriggeredTokensDiscoverer(fuzzy_threshold=71)

    def __init__(self, pipeline_config: Pipeline = None):
        self.pipeline_config = pipeline_config.config
        JsonProcessingEngine.discoverer.fuzzy_threshold = self.pipeline_config.FUZZINESS_LEVEL

    def extract_wl_matched_tokens(self, record):
        threshold = 0.66
        result = []

        alerted_tokens = re.findall(
            r'(?<="inputToken": ")[\w\-]+',
            json.dumps(record["stopDescriptors"]["stopDescriptor"]),
            flags=re.I,
        )
        tokens_scores = re.findall(
            r'(?<="matchScore": ")\d\.\d{1,3}',
            json.dumps(record["stopDescriptors"]["stopDescriptor"]),
        )

        result = [
            alerted_tokens[i].upper()
            for i in range(len(alerted_tokens))
            if float(tokens_scores[i]) >= threshold
        ]
        return {cn.WL_MATCHED_TOKENS: json.dumps(result)}

    @staticmethod
    @safe_field_extractor
    def set_trigger_reasons(match, fuzziness_level):

        skip_columns = ["USER_NOTE_TEXT"]
        result = []
        for whole_token in json.loads(match[cn.WL_MATCHED_TOKENS]):
            for token in whole_token.split():
                for key, value in match.items():
                    if not value or key.startswith("WL_") or key in skip_columns:
                        continue

                    value = str(value)

                    ratio = fuzz.partial_ratio(value.lower(), token.lower())
                    if ratio >= fuzziness_level:
                        result.append(key)
        match[cn.TRIGGERED_BY] = list(set(result))

    def set_beneficiary_hits(self, payload):
        payload[cn.IS_BENEFICIARY_HIT] = cn.AD_BNFL_NM in payload[cn.TRIGGERED_BY]

    def connect_full_names(self, values, fields=[cn.PRTY_FST_NM, cn.PRTY_MDL_NM, cn.PRTY_LST_NM]):
        for value in values:
            value[cn.CONNECTED_FULL_NAME] = " ".join(
                [
                    value.get(field_name_to_collect, "")
                    for field_name_to_collect in fields
                    if value.get(field_name_to_collect, "")
                ]
            ).strip()

    def collect_party_values_from_accounts(self, accounts, payload):
        for (
            collective_field_name,
            field_name_to_collect,
        ) in COLLECTIVE_REPRESENTATION_MAP_FOR_ACCOUNTS.items():
            payload[collective_field_name] = [
                i.get(field_name_to_collect) for i in accounts if i.get(field_name_to_collect, "")
            ]

    def load_raw_data(self, *args, **kwargs):
        return self.spark_instance.read_csv(*args, **kwargs)

    def save_data(self, *args, **kwargs):
        return self.spark_instance.safe_save_delta(*args, **kwargs)

    def merge_to_target_col_from_source_cols(self, *args, **kwargs):
        return self.spark_instance.merge_to_target_col_from_source_cols_sql_expression(
            *args, **kwargs
        )

    def substitute_nulls_in_array_with_new_values(self, *args, **kwargs):
        return self.spark_instance.substitute_nulls_in_array_with_new_values(*args, **kwargs)

    def load_data(self, *args, **kwargs):
        return self.spark_instance.read_delta(*args, **kwargs)

    @staticmethod
    @safe_field_extractor
    def get_clean_names_from_concat_name(concat_field: str, source_fields: Dict[str, str]):

        distinct_names = []
        names = {}
        concat_field_residue = concat_field

        if concat_field is None:
            names[cn.CONCAT_RESIDUE] = None
        else:
            for key, value in source_fields.items():
                if isinstance(value, list):

                    for v in value:
                        if v and v in concat_field and v not in distinct_names:
                            concat_field_residue = concat_field_residue.replace(v, "")
                            names[key] = v
                            distinct_names.append(v)

                else:
                    if (
                        source_fields[key]
                        and source_fields[key] in concat_field
                        and value not in distinct_names
                    ):
                        concat_field_residue = concat_field_residue.replace(value, "")
                        names[key] = value
                        distinct_names.append(value)

            names[cn.CONCAT_RESIDUE] = concat_field_residue
        return names

    @staticmethod
    def prepare_and_get_clean_names_from_concat_name(concat_field, *columns):
        concat_fields_map = {}
        for key, col in enumerate(columns):
            concat_fields_map[col] = columns[key]
        return JsonProcessingEngine.get_clean_names_from_concat_name(
            concat_field, concat_fields_map
        )

    def set_clean_names(self, payload):
        names_source_cols = [cn.ALL_CONNECTED_PARTY_NAMES, cn.ALL_CONNECTED_PARTIES_NAMES]
        payload[cn.CLEANED_NAMES] = JsonProcessingEngine.get_clean_names_from_concat_name(
            cn.CONCAT_ADDRESS, *names_source_cols
        )
        return payload

    def set_concat_residue(self, payload):
        payload[cn.CONCAT_RESIDUE] = payload[cn.CLEANED_NAMES][cn.CONCAT_RESIDUE]
        return payload

    def set_concat_address_no_change(self, payload):
        payload[cn.CONCAT_ADDRESS_NO_CHANGES] = None
        if payload[cn.CONCAT_RESIDUE] == payload[cn.CONCAT_ADDRESS]:
            payload[cn.CONCAT_ADDRESS_NO_CHANGES] = payload[cn.CONCAT_RESIDUE]
        return payload

    @staticmethod
    def discover(matched_tokens, dict_values):
        return JsonProcessingEngine.discover(matched_tokens, dict_values)

    def set_triggered_tokens_discovery(self, match, fields):
        TRIGGERS_MAP = {field: fields[field].value for field in fields if fields[field]}
        return self.discoverer.discover(json.loads(match[cn.WL_MATCHED_TOKENS]), TRIGGERS_MAP)

    def merge_to_target_col_from_source_cols_sql_expression(
        self, target_col, source_cols, mapper, return_array
    ):
        if len(source_cols) >= 2:
            value = []
            for valid_source_col in source_cols:
                try:
                    value.append(mapper[valid_source_col])
                except KeyError:
                    if "hit_type" not in valid_source_col:
                        logger.warning(f"No field in payload named: {valid_source_col}.")
        elif len(source_cols) == 1:
            try:
                value = mapper[source_cols[0]]
                if return_array:
                    value = [value]
            except KeyError:
                if "hit_type" not in source_cols:
                    logger.warning(f"No field in payload named: {source_cols}")
        else:
            value = None
            if return_array:
                value = [value]

        return {target_col: value}

    def sql_to_merge_specific_columns_to_standardized(
        self,
        agent_config,
        match_record,
        mapper,
        aggregated: bool = False,
    ):
        for _, config in agent_config.items():
            for target_col, source_cols in config.items():
                sql_expr = self.merge_to_target_col_from_source_cols_sql_expression(
                    target_col, source_cols, mapper, aggregated
                )
                match_record.update(sql_expr)

    def collect_party_values_from_parties_from_fields(self, fields, payload):
        for connected_key, value in COLLECTIVE_REPRESENTATION_MAP_FOR_FIELD.items():
            # logger.debug(f"{connected_key}, {value}, {self.get_field_value_name(fields, value)}")
            payload[connected_key] = self.get_field_value_name(fields, value)

    def get_field_value_name(self, fields, name):
        try:
            return fields.get(name, name).value
        except AttributeError:
            return None

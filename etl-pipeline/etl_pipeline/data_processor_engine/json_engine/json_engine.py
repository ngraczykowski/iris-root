import json
import re
from typing import Dict

from fuzzywuzzy import fuzz

from config import columns_namespace
from etl_pipeline.custom.ms.trigger_discovery.discoverer import TriggeredTokensDiscoverer
from etl_pipeline.data_processor_engine.engine.engine import ProcessingEngine
from etl_pipeline.pattern_json import (
    ACCT_NUM,
    ADDRESS_ID,
    ALERT_ID,
    ALERTED_PARTY_NAME,
    ALL_CONNECTED_PARTIES_NAMES,
    ALL_PARTS_NAMES,
    ALL_PARTY_DETAILS,
    CONCAT_RESIDUE,
    CONNECTED_FULL_NAME,
    DOB_DT,
    MERGING_PARTIES,
    PARTIES,
    PRTY_CNTRY_OF_BIRTH,
    PRTY_FST_NM,
    PRTY_LST_NM,
    PRTY_MDL_NM,
    PRTY_NM,
    PRTY_PRIM_CTZNSH_CNTRY,
    PRTY_RSDNC_CNTRY_CD,
    PRTY_TYP,
    SRC_REF_KEY,
    SRC_SYS_ACCT_KEY,
)

COLLECTIVE_REPRESENTATION_MAP = {
    columns_namespace.ALL_CONNECTED_PARTIES_NAMES: columns_namespace.CONNECTED_FULL_NAME,
    columns_namespace.ALL_PARTY_TYPES: "partyType",
    columns_namespace.ALL_PARTY_NAMES: "partyName",
    columns_namespace.ALL_PARTY_DOBS: "dobDate",
    columns_namespace.ALL_PARTY_BIRTH_COUNTRIES: "partyCountryOfBirth",
    columns_namespace.ALL_PARTY_CITIZENSHIP_COUNTRIES: "partyPrimaryCitizenshipCountry",
    columns_namespace.ALL_PARTY_RESIDENCY_COUNTRIES: "partyResidenceCountryCode",
}


class JsonProcessingEngine(ProcessingEngine):
    REF_KEY_REGEX = r"(\d{4}-\d{2}-\d{2}-\d{2}.\d{2}.\d{2}.\d{6})"
    REF_KEY_REGEX_PYTHON = re.compile(REF_KEY_REGEX)
    discoverer = TriggeredTokensDiscoverer(fuzzy_threshold=1.0)

    def __init__(self, pipeline_config=None):
        self.pipeline_config = pipeline_config
        JsonProcessingEngine.discoverer.fuzzy_threshold = pipeline_config.FUZZINESS_LEVEL

    def set_ref_key(self, row):
        match = self.REF_KEY_REGEX_PYTHON.match(row[SRC_REF_KEY])
        row[SRC_SYS_ACCT_KEY] = match.group(1) if match else ""

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
        return {"WL_MATCHED_TOKENS": json.dumps(result)}

    @staticmethod
    def set_trigger_reasons(match, fuzziness_level):
        skip_columns = ["USER_NOTE_TEXT"]
        result = []
        for whole_token in json.loads(match["WL_MATCHED_TOKENS"]):
            for token in whole_token.split():
                for key, value in match.items():
                    if not value or key.startswith("WL_") or key in skip_columns:
                        continue

                    value = str(value)

                    ratio = fuzz.partial_ratio(value.lower(), token.lower())
                    if ratio >= fuzziness_level:
                        result.append(key)
        return list(set(result))

    def set_beneficiary_hits(self, payload):
        payload[columns_namespace.IS_BENEFICIARY_HIT] = (
            columns_namespace.AD_BNFL_NM in payload[columns_namespace.TRIGGERED_BY]
        )

    def connect_full_names(self, parties):
        for party in parties:
            party[columns_namespace.CONNECTED_FULL_NAME] = " ".join(
                [
                    party.get(key, "")
                    for key in [
                        columns_namespace.PRTY_FST_NM,
                        columns_namespace.PRTY_MDL_NM,
                        columns_namespace.PRTY_LST_NM,
                    ]
                ]
            ).strip()

    def collect_party_values(self, parties, payload):
        for collective_field_name, field_name_to_collect in COLLECTIVE_REPRESENTATION_MAP.items():
            payload[collective_field_name] = [i.get(field_name_to_collect, "") for i in parties]

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

    def merge_with_party_and_address_relationships(self, payload):
        selected_parties_payload = [
            party_item[field_name]
            for party_item in payload[PARTIES]
            for field_name in payload[MERGING_PARTIES]
        ]
        for party in selected_parties_payload:
            party[CONNECTED_FULL_NAME] = " ".join(
                [party[PRTY_FST_NM], party[PRTY_MDL_NM]], party[PRTY_LST_NM]
            )

        enhanced_df360_payload = {
            ALERT_ID: payload[ALERT_ID],
            ALERTED_PARTY_NAME: payload[ALERTED_PARTY_NAME],
            ACCT_NUM: payload[ACCT_NUM],
            columns_namespace.TRIGGERED_BY: payload[columns_namespace.TRIGGERED_BY],
            SRC_SYS_ACCT_KEY: payload[SRC_SYS_ACCT_KEY],
            ADDRESS_ID: payload[ADDRESS_ID],
            ALL_PARTY_DETAILS: selected_parties_payload,
            ALL_PARTS_NAMES: [party[PRTY_NM] for party in selected_parties_payload],
            columns_namespace.ALL_PARTY_TYPES: [
                party[PRTY_TYP] for party in selected_parties_payload
            ],
            columns_namespace.ALL_PARTY_DOBS: [
                party[DOB_DT] for party in selected_parties_payload
            ],
            columns_namespace.ALL_PARTY_BIRTH_COUNTRIES: [
                party[PRTY_CNTRY_OF_BIRTH] for party in selected_parties_payload
            ],
            columns_namespace.ALL_PARTY_CITIZENSHIP_COUNTRIES: [
                party[PRTY_PRIM_CTZNSH_CNTRY] for party in selected_parties_payload
            ],
            columns_namespace.ALL_PARTY_RESIDENCY_COUNTRIES: [
                party[PRTY_RSDNC_CNTRY_CD] for party in selected_parties_payload
            ],
            ALL_CONNECTED_PARTIES_NAMES: [
                party[CONNECTED_FULL_NAME] for party in selected_parties_payload
            ],
        }

        return enhanced_df360_payload

    @staticmethod
    def get_clean_names_from_concat_name(concat_field: str, source_fields: Dict[str, str]):

        distinct_names = []
        names = {}
        concat_field_residue = concat_field

        if concat_field is None:
            names[CONCAT_RESIDUE] = None
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

            names[CONCAT_RESIDUE] = concat_field_residue

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
        names_source_cols = [columns_namespace.ALL_PARTY_NAMES, ALL_CONNECTED_PARTIES_NAMES]
        payload[
            columns_namespace.CLEANED_NAMES
        ] = JsonProcessingEngine.get_clean_names_from_concat_name(
            columns_namespace.CONCAT_ADDRESS, *names_source_cols
        )
        return payload

    def set_concat_residue(self, payload):
        payload[CONCAT_RESIDUE] = payload[columns_namespace.CLEANED_NAMES][CONCAT_RESIDUE]
        return payload

    def set_concat_address_no_change(self, payload):
        payload[columns_namespace.CONCAT_ADDRESS_NO_CHANGES] = None
        if payload[CONCAT_RESIDUE] == payload[columns_namespace.CONCAT_ADDRESS]:
            payload[columns_namespace.CONCAT_ADDRESS_NO_CHANGES] = payload[CONCAT_RESIDUE]
        return payload

    @staticmethod
    def discover(matched_tokens, dict_values):
        return JsonProcessingEngine.discover(matched_tokens, dict_values)

    def set_discovery_tokens(self, payload):
        ap_columns = self.pipeline_config.AP_COLUMNS
        dict_values = {}
        for i in range(len(ap_columns)):
            if ap_columns[i] != "":
                dict_values[ap_columns[i]] = ap_columns[i]
        matched_tokens = payload[columns_namespace.WL_MATCHED_TOKENS]
        return self.discoverer.discover(matched_tokens, dict_values)

    def set_triggered_tokens_discovery(self, payload, match, fields):
        TRIGGERS_MAPPING = {
            columns_namespace.ALL_PARTY_NAMES: payload[columns_namespace.ALL_PARTY_NAMES],
            columns_namespace.ADDRESS1_COUNTRY: fields.get(columns_namespace.ADDRESS1_COUNTRY),
            columns_namespace.ADDRESS1_LINE1: fields.get(columns_namespace.ADDRESS1_LINE1),
            columns_namespace.ADDRESS1_LINE2: fields.get(columns_namespace.ADDRESS1_LINE2),
            columns_namespace.ADDRESS1_LINE3: fields.get(columns_namespace.ADDRESS1_LINE3),
            columns_namespace.ADDRESS1_LINE4: fields.get(columns_namespace.ADDRESS1_LINE4),
            columns_namespace.ADDRESS1_LINE5: fields.get(columns_namespace.ADDRESS1_LINE5),
            columns_namespace.CONCAT_ADDRESS: fields.get(columns_namespace.CONCAT_ADDRESS),
        }
        return self.discoverer.discover(json.loads(match["WL_MATCHED_TOKENS"]), TRIGGERS_MAPPING)

    def prepare_agent_mapper(self, payload, match):
        agent_mapper = {
            "all_party_dobs": payload[columns_namespace.ALL_PARTY_DOBS],
            "WL_DOB": match["WL_DOB"],
        }

        return agent_mapper

    def prepare_aggregation_mapper(self, payload, match):
        aggregation_mapper = {
            "all_party_dobs": payload[columns_namespace.ALL_PARTY_DOBS],
            "WL_DOB": match["WL_DOB"],
            "dob_agent_ap": match["dob_agent_ap"],
            "dob_agent_wl": match["dob_agent_wl"],
        }
        return aggregation_mapper

    def merge_to_target_col_from_source_cols_sql_expression(
        self, target_col, source_cols, mapper, return_array
    ):
        valid_source_cols = []
        for source_col in source_cols:
            valid_source_cols.append(source_col)
        if len(valid_source_cols) >= 2:
            value = [mapper[valid_source_col] for valid_source_col in valid_source_cols]
        elif len(valid_source_cols) == 1:
            value = mapper[valid_source_cols[0]]
            if return_array:
                value = [value]
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

import json
import re
from typing import Dict

from fuzzywuzzy import fuzz

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.trigger_discovery.discoverer import TriggeredTokensDiscoverer
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

FUZZINESS = 81
cn = pipeline_config.cn


class ProcessingEngine:
    REF_KEY_REGEX = r"(\d{4}-\d{2}-\d{2}-\d{2}.\d{2}.\d{2}.\d{6})"
    REF_KEY_REGEX_PYTHON = re.compile(REF_KEY_REGEX)

    def __init__(self):
        self.pipeline_config = None

    def set_ref_key(self, row):
        match = self.REF_KEY_REGEX_PYTHON.match(row[SRC_REF_KEY])
        row[SRC_SYS_ACCT_KEY] = match.group(1) if match else ""

    @staticmethod
    def set_trigger_reasons(values):
        skip_columns = ["USER_NOTE_TEXT"]
        result = []
        for whole_token in json.loads(values[cn.WL_MATCHED_TOKENS]):
            for token in whole_token.split():
                for key, value in values.items():
                    if not value or key.startswith("WL_") or key in skip_columns:
                        continue

                    value = str(value)

                    ratio = fuzz.partial_ratio(value.lower(), token.lower())
                    if ratio >= FUZZINESS:
                        result.append(key)
        return list(set(result))

    def set_beneficiary_hits(self, payload):
        payload[cn.IS_BENEFICIARY_HIT] = payload[cn.AD_BNFL_NM] in payload[cn.TRIGGERED_BY]

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
            cn.TRIGGERED_BY: payload[cn.TRIGGERED_BY],
            SRC_SYS_ACCT_KEY: payload[SRC_SYS_ACCT_KEY],
            ADDRESS_ID: payload[ADDRESS_ID],
            ALL_PARTY_DETAILS: selected_parties_payload,
            ALL_PARTS_NAMES: [party[PRTY_NM] for party in selected_parties_payload],
            cn.ALL_PARTY_TYPES: [party[PRTY_TYP] for party in selected_parties_payload],
            cn.ALL_PARTY_DOBS: [party[DOB_DT] for party in selected_parties_payload],
            cn.ALL_PARTY_BIRTH_COUNTRIES: [
                party[PRTY_CNTRY_OF_BIRTH] for party in selected_parties_payload
            ],
            cn.ALL_PARTY_CITIZENSHIP_COUNTRIES: [
                party[PRTY_PRIM_CTZNSH_CNTRY] for party in selected_parties_payload
            ],
            cn.ALL_PARTY_RESIDENCY_COUNTRIES: [
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
        return ProcessingEngine.get_clean_names_from_concat_name(concat_field, concat_fields_map)

    def set_clean_names(self, payload):
        names_source_cols = [cn.ALL_PARTY_NAMES, ALL_CONNECTED_PARTIES_NAMES]
        payload[cn.CLEANED_NAMES] = ProcessingEngine.get_clean_names_from_concat_name(
            cn.CONCAT_ADDRESS, *names_source_cols
        )
        return payload

    def set_concat_residue(self, payload):
        payload[CONCAT_RESIDUE] = payload[cn.CLEANED_NAMES][CONCAT_RESIDUE]
        return payload

    def set_concat_address_no_change(self, payload):
        payload[cn.CONCAT_ADDRESS_NO_CHANGES] = None
        if payload[CONCAT_RESIDUE] == payload[cn.CONCAT_ADDRESS]:
            payload[cn.CONCAT_ADDRESS_NO_CHANGES] = payload[CONCAT_RESIDUE]
        return payload

    @staticmethod
    def discover(matched_tokens, dict_values):
        return discoverer.discover(matched_tokens, dict_values)

    def set_discovery_tokens(self, payload):
        ap_columns = self.pipeline_config.AP_COLUMNS
        dict_values = {}
        for i in range(len(ap_columns)):
            if ap_columns[i] != "":
                dict_values[ap_columns[i]] = ap_columns[i]
        matched_tokens = payload[cn.WL_MATCHED_TOKENS]
        return discoverer.discover(matched_tokens, dict_values)


discoverer = TriggeredTokensDiscoverer(fuzzy_threshold=71)


def discover(dict_values, matched_tokens):
    return discoverer.discover(matched_tokens, dict_values)

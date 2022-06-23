import json
import logging
import os
import re
from copy import deepcopy
from typing import Dict

from fuzzywuzzy import fuzz

from etl_pipeline import application
from etl_pipeline.config import load_agent_configs, pipeline_config
from etl_pipeline.custom.ms.trigger_discovery.discoverer import TriggeredTokensDiscoverer
from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from etl_pipeline.pipeline import ETLPipeline
from pipelines.ms.collection import Collections

logger = logging.getLogger("main").getChild("etl_pipeline")

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
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


class PipelineError:
    pass


class MSPipeline(ETLPipeline):
    REF_KEY_REGEX = r"(\d{4}-\d{2}-\d{2}-\d{2}.\d{2}.\d{2}.\d{6})"
    REF_KEY_REGEX_PYTHON = re.compile(REF_KEY_REGEX)
    discoverer = TriggeredTokensDiscoverer(fuzzy_threshold=71)

    def __init__(self, config, functions):
        super().__init__(config.config)
        self.reload_config()
        self.dataset_config = config.dataset_config
        self.watchlist_extractor = WatchlistExtractor()
        self.collections = Collections()
        self.functions = functions

    def transform_standardized_to_cleansed(self, payloads):
        payloads = self.collections.prepare_collections(payloads)
        payloads = self.connect_input_record_with_match_record(payloads)
        for payload in payloads:
            match = payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
            self.collections.prepare_wl_values(match)
            self.set_up_dataset_type_match(payload, match)
            self.set_trigger_reasons(match, self.pipeline_config.FUZZINESS_LEVEL)
            self.functions.set_beneficiary_hits(match)
            self.connect_full_names(payload)
            self.collect_party_values(payload)
            self.collect_concat_values(payload, match)
            self.functions.set_up_party_type(payload)
            self.set_token_risk_carrier(match)
        return payloads

    def transform_cleansed_to_application(self, payloads):
        for payload in payloads:
            self.parse_agent_config(payload)
            self.sanitize_logic(payload)
            self.remove_nulls_from_aggregated(payload)
        return payloads

    def sanitize_logic(self, payload):
        self.sanitize_for_hit_type_agent(payload)
        self.sanitize_for_geo_sanction_agent(payload)
        self.sanitize_employer_party_type(payload)

    def sanitize_for_hit_type_agent(self, match):
        match = match["watchlistParty"]["matchRecords"]
        match["all_hit_type_aggregated"] = {
            key.split("_")[-1]: [i for i in match[key] if i]
            for key in match
            if key.startswith("hit")
        }

    def sanitize_for_geo_sanction_agent(self, match):
        match = match["watchlistParty"]["matchRecords"]
        if match["WL_ENTITYTYPE"] != "01":
            match["wl_all_sanctioned_countries_aggregated"] = []

    def parse_agent_config(self, payload):
        match = payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
        (
            agent_config,
            agent_input_prepended_agent_name_config,
            yaml_conf,
        ) = self.alert_agents_config["alert_type"]
        agent_input_agg_col_config = application.create_agent_input_agg_col_config(
            agent_input_prepended_agent_name_config
        )

        config = self.get_key(payload, match, yaml_conf)
        self.sql_to_merge_specific_columns_to_standardized(
            agent_input_prepended_agent_name_config,
            match,
            config,
            False,
        )

        config.update(
            {
                key: self.flatten(match.get(key))
                for key in match
                if key.endswith("_ap")
                or key.endswith("_wl")
                or key.endswith("_name")
                or key.endswith("_aliases")
            }
        )
        input_records = payload[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][cn.INPUT_RECORDS]
        fields = input_records[cn.INPUT_FIELD]
        self.handle_hit_type_agent(match, agent_config, fields)
        self.select_ap_for_ap_id_tp_marked_agent(payload)
        self.set_up_entity_type_match(payload, match)

        self.sql_to_merge_specific_columns_to_standardized(
            agent_input_agg_col_config, match, config, False
        )
        match.update(
            {
                key: self.produce_unique_flatten_list(match.get(key, []))
                for key in match
                if key.endswith("_aggregated") or key.startswith("hit_type_agent")
            }
        )

    def reload_config(self):
        self.alert_agents_config = load_agent_configs()

    def convert_raw_to_standardized(self, df):
        return df

    def connect_input_record_with_match_record(self, payload):
        new_payloads = []
        input_records = (
            payload.get(cn.ALERTED_PARTY_FIELD, {})
            .get(cn.INPUT_RECORD_HIST, {})
            .get(cn.INPUT_RECORDS, [])
        )
        match_ids = {match.match_id: match for match in payload[cn.MATCH_IDS]}

        match_records = payload.get(cn.WATCHLIST_PARTY, {}).get(cn.MATCH_RECORDS, [])
        for input_record in input_records:
            for num, match_record in enumerate(match_records):
                if (
                    input_record[cn.INPUT_RECORD_VERSION_ID]
                    == match_record[cn.MATCH_RECORD_VERSION_ID]
                ):
                    pair_payload = deepcopy(payload)
                    pair_payload[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][
                        cn.INPUT_RECORDS
                    ] = input_record
                    pair_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS] = match_record
                    pair_payload[cn.MATCH_IDS] = match_ids[match_record["matchId"]]
                    new_payloads.append(pair_payload)
        if not new_payloads:
            logger.warning("No input vs match pairs")
        return new_payloads

    def collect_party_values(self, payload):
        self.functions.collect_party_full_names(payload)
        self.functions.collect_party_types(payload)
        self.functions.collect_party_names(payload)
        self.functions.collect_party_tax_ids(payload)
        self.functions.collect_party_dobs(payload)
        self.functions.collect_party_birth_countries(payload)
        self.functions.collect_party_citizenship_countries(payload)
        self.functions.collect_party_residency_countries(payload)
        self.functions.collect_party_country_of_incorporation(payload)
        self.functions.collect_party_government_id(payload)

        self.functions.collect_full_name_accounts(payload)
        self.functions.collect_branch_account_numbers(payload)
        self.functions.collect_beneficiary_names(payload)

    def collect_concat_values(self, payload, match):
        """temporary for demo"""

        fields = self.collections.get_xml_fields(payload)

        names_source_cols = [
            cn.AP_PARTY_NAMES,
            cn.AP_PARTIES_NAMES,
            cn.AP_ACCOUNT_NAMES,
        ]

        payload.update(
            {
                cn.CLEANED_NAMES: self.get_clean_names_from_concat_name(
                    self.get_field_value_name(fields, cn.CONCAT_ADDRESS),
                    {key: payload[key] for key in names_source_cols},
                )
            }
        )

        payload.update({cn.CONCAT_RESIDUE: payload[cn.CLEANED_NAMES][cn.CONCAT_RESIDUE]})
        concat_residue = payload[cn.CONCAT_RESIDUE]
        concat_address = self.get_field_value_name(fields, cn.CONCAT_ADDRESS)
        payload.update({cn.CONCAT_ADDRESS_NO_CHANGES: concat_residue == concat_address})
        match[cn.AP_TRIGGERS] = self.set_triggered_tokens_discovery(match, fields)

    def connect_full_names(self, payload):
        alerted_parties = self.collections.get_parties(payload)
        self._connect_full_names(alerted_parties, [cn.PRTY_FST_NM, cn.PRTY_MDL_NM, cn.PRTY_LST_NM])
        accounts = self.collections.get_accounts(payload)
        self._connect_full_names(accounts, [cn.ACCOUNT_FIRST_NAME, cn.ACCOUNT_LAST_NAME])

    def get_key(self, payload, match, conf):
        new_config = {}
        for _, value in dict(conf).items():
            try:
                self.functions.parse_key(value, match, payload, new_config)
            except:
                logger.warning(f"Field {value} does not exist in payload")
        return new_config

    def produce_unique_flatten_list(self, record):
        record = self.flatten(record)
        if record is None:
            record = []
        if not isinstance(record, list):
            record = [record]
        return list(set([i for i in record]))

    def flatten(self, value):
        if value == []:
            return value
        if isinstance(value, list):
            if isinstance(value[0], list):
                return self.flatten(value[0]) + self.flatten(value[1:])
            return value[:1] + self.flatten(value[1:])
        return value

    def remove_nulls_from_aggregated(self, payload):
        match = payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
        for key in match:
            if key.endswith("_aggregated") or key.startswith("hit_type_agent"):
                value = match[key]
                if isinstance(value, list):
                    match.update({key: [i for i in match.get(key) if i]})
                else:
                    match.update({key: [value] if value else []})

    def handle_hit_type_agent(self, match, config, fields):
        for key in config:
            if key.startswith("hit"):
                for feature in config[key]:
                    match[key + "_" + feature] = self.collect_existing_fields(
                        fields, config[key][feature]
                    )

    def collect_existing_fields(self, fields, requested_json_keys):
        existing_fields = [field for field in requested_json_keys if fields.get(field, None)]
        return existing_fields

    def select_ap_for_ap_id_tp_marked_agent(self, payload):
        dataset_name = payload[cn.DATASET_TYPE]
        match = payload["watchlistParty"]["matchRecords"]
        if dataset_name == "ISG_PARTY":
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][0]
        elif dataset_name in ["WM_ADDRESS", "WM_PARTY"]:
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][1]
        elif dataset_name == "ISG_ACCOUNT":
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][2]
        else:
            logger.warning("No input for ap_id_tp_marked_agent. Setting up default")
            match["ap_id_tp_marked_agent_input"] = ""

    def select_discriminator(self, payload):
        dataset_name = payload[cn.DATASET_TYPE]
        if dataset_name == "ISG_PARTY":
            payload["ap_id_tp_marked_agent_input"] = payload["ap_id_tp_marked_agent_input"][0]
        elif dataset_name in ["WM_ADDRESS", "WM_PARTY"]:
            payload["ap_id_tp_marked_agent_input"] = payload["ap_id_tp_marked_agent_input"][1]
        elif dataset_name == "ISG_ACCOUNT":
            payload["ap_id_tp_marked_agent_input"] = payload["ap_id_tp_marked_agent_input"][2]
        else:
            logger.warning("No input for ap_id_tp_marked_agent. Setting up default")
            payload["ap_id_tp_marked_agent_input"] = ""

    def set_up_entity_type_match(self, payload, match):
        if payload["alertedParty"]["AP_PARTY_TYPE"] == match["WLP_TYPE"]:
            payload["alertedParty"]["ENTITY_TYPE_MATCH"] = "Y"
        else:
            payload["alertedParty"]["ENTITY_TYPE_MATCH"] = "N"
        if payload["alertedParty"]["AP_PARTY_TYPE"] == "UNKNOWN":
            payload["alertedParty"]["ENTITY_TYPE_MATCH"] = "INCONCLUSIVE"

    def sanitize_employer_party_type(self, payload):
        if payload["alertedParty"]["AP_PARTY_TYPE"] == "I":
            payload["alertedParty"]["employer_party_type"] = "C"
        else:
            payload["watchlistParty"]["matchRecords"]["wl_all_employer_names_aggregated"] = []
            payload["watchlistParty"]["matchRecords"]["ap_all_employer_names_aggregated"] = []
            payload["alertedParty"]["employer_party_type"] = "CLEAR"

    def set_up_dataset_type_match(self, payload, match):
        match[cn.DATASET_TYPE] = self.dataset_config.get(
            payload[cn.ALERTED_PARTY_FIELD][cn.HEADER_INFO][cn.DATASET_NAME]
        )
        payload[cn.DATASET_TYPE] = self.dataset_config.get(
            payload[cn.ALERTED_PARTY_FIELD][cn.HEADER_INFO][cn.DATASET_NAME]
        )

    def set_token_risk_carrier(self, match):
        filtered_tokens = [
            token
            for tokens in json.loads(match["WL_MATCHED_TOKENS"])
            for token in tokens.split()
            if len(token) > 2
        ]
        match["TOKENS_RISK_CARRIER"] = ",".join(sorted(list(set(filtered_tokens))))

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

    def _connect_full_names(self, values, fields=[cn.PRTY_FST_NM, cn.PRTY_MDL_NM, cn.PRTY_LST_NM]):
        for value in values:
            value[cn.CONNECTED_FULL_NAME] = " ".join(
                [
                    value[field_name_to_collect]
                    for field_name_to_collect in fields
                    if value.get(field_name_to_collect, "")
                ]
            ).strip()

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
        return MSPipeline.get_clean_names_from_concat_name(concat_field, concat_fields_map)

    def set_clean_names(self, payload):
        names_source_cols = [cn.AP_PARTY_NAMES, cn.AP_PARTIES_NAMES]
        payload[cn.CLEANED_NAMES] = MSPipeline.get_clean_names_from_concat_name(
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

    def get_field_value_name(self, fields, name):
        try:
            return fields.get(name, None).value
        except AttributeError:
            return None

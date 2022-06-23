import json
import logging
import os
from copy import deepcopy

from etl_pipeline import application
from etl_pipeline.config import load_agent_configs, pipeline_config
from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from etl_pipeline.pipeline import ETLPipeline
from pipelines.ms.collection import Collections

logger = logging.getLogger("main").getChild("etl_pipeline")

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
cn = pipeline_config.cn


class PipelineError:
    pass


class MSPipeline(ETLPipeline):
    def __init__(self, engine, config, functions):
        super().__init__(engine, config.config)
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
            self.engine.set_trigger_reasons(match, self.pipeline_config.FUZZINESS_LEVEL)
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
        self.engine.sql_to_merge_specific_columns_to_standardized(
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
        self.select_ap_for_ap_id_tp_marked_agent(match)
        self.set_up_entity_type_match(payload, match)

        self.engine.sql_to_merge_specific_columns_to_standardized(
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

        accounts = self.collections.get_accounts(payload)
        self.engine.collect_party_values_from_accounts(accounts, payload)

        fields = self.collections.get_xml_fields(payload)
        self.engine.collect_party_values_from_parties_from_fields(fields, payload)

    def collect_concat_values(self, payload, match):
        """temporary for demo"""

        fields = self.collections.get_xml_fields(payload)

        names_source_cols = [
            cn.ALL_CONNECTED_PARTY_NAMES,
            cn.ALL_CONNECTED_PARTIES_NAMES,
            cn.ALL_CONNECTED_ACCOUNT_NAMES,
        ]

        payload.update(
            {
                cn.CLEANED_NAMES: self.engine.get_clean_names_from_concat_name(
                    self.engine.get_field_value_name(fields, cn.CONCAT_ADDRESS),
                    {key: payload[key] for key in names_source_cols},
                )
            }
        )

        payload.update({cn.CONCAT_RESIDUE: payload[cn.CLEANED_NAMES][cn.CONCAT_RESIDUE]})
        concat_residue = payload[cn.CONCAT_RESIDUE]
        concat_address = self.engine.get_field_value_name(fields, cn.CONCAT_ADDRESS)
        payload.update({cn.CONCAT_ADDRESS_NO_CHANGES: concat_residue == concat_address})
        match[cn.AP_TRIGGERS] = self.engine.set_triggered_tokens_discovery(match, fields)

    def connect_full_names(self, payload):
        alerted_parties = self.collections.get_parties(payload)
        self.engine.connect_full_names(
            alerted_parties, [cn.PRTY_FST_NM, cn.PRTY_MDL_NM, cn.PRTY_LST_NM]
        )
        accounts = self.collections.get_accounts(payload)
        self.engine.connect_full_names(accounts, [cn.ACCOUNT_FIRST_NAME, cn.ACCOUNT_LAST_NAME])

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

    def select_ap_for_ap_id_tp_marked_agent(self, match):
        dataset_name = match[cn.DATASET_TYPE]
        if dataset_name == "ISG_PARTY":
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][0]
        elif dataset_name in ["WM_ADDRESS", "WM_PARTY"]:
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][1]
        elif dataset_name == "ISG_ACCOUNT":
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][2]
        else:
            logger.warning("No input for ap_id_tp_marked_agent. Setting up default")
            match["ap_id_tp_marked_agent_input"] = ""

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

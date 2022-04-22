import logging
from copy import deepcopy

from etl_pipeline.config import load_agent_configs, pipeline_config
from etl_pipeline.custom.ms.datatypes.field import InputRecordField
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from etl_pipeline.logger import get_logger
from etl_pipeline.pipeline import ETLPipeline

logger = get_logger("main", "ms_pipeline.log")
logger = logging.getLogger("main").getChild("etl_pipeline")


cn = pipeline_config.cn


CUSTOMER_S8_MAP = {
    "R_Global_MultiParty_Wkly": "ISG_PARTY",
    "R_Global_MultiParty_Daily": "ISG_PARTY",
    "R_US_Active_Address": "WM_ADDRESS",
    "R_US_Active_Party": "WM_PARTY",
    "R_Global_MultiAccounts_Daily": "ISG_ACCOUNT",
    "R_Global_MultiAccounts_Wkly": "ISG_ACCOUNT",
}


class PipelineError:
    pass


def create_agent_input_agg_col_config(agent_input_prepended_agent_name_config):
    """Create the source and target columns based on the standardized agent input config.

    Input:
    { 'name_agent': {'name_agent_ap': ['record_name', 'whatever_other_name'],
                     'name_agent_ap_aliases': [],
                     'name_agent_wl': ['name_hit'],
                     'name_agent_wl_aliases': []
                    }
    }

    Output:
    {'name_agent': {'ap_all_names_aggregated': ['name_agent_ap', 'name_agent_ap_aliases'],
                    'wl_all_names_aggregated': ['name_agent_wl', 'name_agent_wl_aliases']
                   }
    }
    """

    def _generate_simple_plural(word):
        if word.lower().endswith("s"):
            return word.lower() + "es"
        elif word.lower().endswith("y") and word.lower()[-2:] not in [
            "ay",
            "ey",
            "iy",
            "oy",
            "uy",
        ]:
            return word.lower()[:-1] + "ies"
        else:
            return word.lower() + "s"

    def _get_ap_or_wl_agg_source_cols(level_1_value, party):
        source_cols = []
        for col in level_1_value.keys():
            if (
                col.endswith(f"_{party}")
                or col.endswith(f"_{party}_aliases")
                or col.startswith("hit")
            ):
                source_cols.append(col)

        return source_cols

    agent_input_agg_col_config = {}

    for agent_name, config in agent_input_prepended_agent_name_config.items():
        agent_type = agent_name.split("_agent", 1)[0]
        agent_ap_agg_col = f"""ap_all_{_generate_simple_plural(agent_type)}_aggregated"""
        agent_wl_agg_col = f"""wl_all_{_generate_simple_plural(agent_type)}_aggregated"""
        agent_ap_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "ap")
        agent_wl_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "wl")

        agent_input_agg_col_config[agent_name] = {}
        agent_input_agg_col_config[agent_name][agent_ap_agg_col] = agent_ap_agg_source_cols
        agent_input_agg_col_config[agent_name][agent_wl_agg_col] = agent_wl_agg_source_cols

    return agent_input_agg_col_config


def prepend_agent_name_to_ap_or_wl_or_aliases_key(agent_input_config):
    """Prepend the agent name (level 1 key) to level 2 key. So the new level 2 key will be

    Input:
    { 'name_agent': {'ap': ['record_name'],
                    'ap_aliases': [],
                    'wl': ['name_hit'],
                    'wl_aliases': []
                    }
    }

    Output:
    { 'name_agent': {'name_agent_ap': ['record_name'],
                    'name_agent_ap_aliases': [],
                    'name_agent_wl': ['name_hit'],
                    'name_agent_wl_aliases': []
                    }
    }
    """
    result = {}
    for agent_name, config in agent_input_config.items():
        result[agent_name] = {}

        for ap_or_wl_or_aliases, source_cols in config.items():
            prepended_key_name = "_".join([agent_name, ap_or_wl_or_aliases])
            result[agent_name][prepended_key_name] = source_cols

    return result


class MSPipeline(ETLPipeline):
    def __init__(self, engine, config=None):
        super().__init__(engine, config.config)
        self.alert_agents_config = load_agent_configs()

    def reload_config(self):
        self.alert_agents_config = load_agent_configs()

    def convert_raw_to_standardized(self, df):
        return df

    def flatten_fields(self, fields):
        for num, party in enumerate(fields):
            fields[num] = party[cn.FIELDS]

    def parse_input_records(self, input_records):
        for input_record in input_records:
            input_record[cn.INPUT_FIELD] = {
                i["name"]: InputRecordField(**i) for i in input_record.get(cn.FIELDS, [])
            }

    def connect_input_record_with_match_record(self, payload):
        new_payloads = []
        input_records = (
            payload.get(cn.ALERTED_PARTY_FIELD, {})
            .get(cn.INPUT_RECORD_HIST, {})
            .get(cn.INPUT_RECORDS, [])
        )
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
                    ] = [input_record]
                    pair_payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS] = [match_record]
                    pair_payload[cn.MATCH_IDS] = [pair_payload[cn.MATCH_IDS][num]]
                    new_payloads.append(pair_payload)
        if not new_payloads:
            logger.warning("No input vs match pairs")
        return new_payloads

    def get_parties(self, payload):
        try:
            alerted_parties = payload[cn.ALERTED_PARTY_FIELD][cn.SUPPLEMENTAL_INFO][
                cn.RELATED_PARTIES
            ][cn.PARTIES]
        except (KeyError, IndexError, TypeError):
            logger.warning("No parties")
            alerted_parties = []
        return alerted_parties

    def get_accounts(self, payload):
        try:
            accounts = payload[cn.ALERTED_PARTY_FIELD][cn.SUPPLEMENTAL_INFO][cn.RELATED_ACCOUNTS][
                cn.ACCOUNTS
            ]
        except (KeyError, IndexError, TypeError):
            logger.warning("No accounts")
            accounts = []
        return accounts

    def transform_standardized_to_cleansed(self, payloads):
        alerted_parties = self.get_parties(payloads)
        accounts = self.get_accounts(payloads)

        if alerted_parties:
            self.flatten_fields(alerted_parties)
        if accounts:
            self.flatten_fields(accounts)

        try:
            input_records = payloads[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][
                cn.INPUT_RECORDS
            ]
        except (KeyError, IndexError):
            logger.warning("No input_records")
            return {}

        self.parse_input_records(input_records)
        payloads = self.connect_input_record_with_match_record(payloads)

        for payload in payloads:
            matches = payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
            input_records = payload[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][cn.INPUT_RECORDS]
            alerted_parties = self.get_parties(payload)
            accounts = self.get_accounts(payload)
            fields = input_records[0][cn.INPUT_FIELD]
            for match in matches:
                WatchlistExtractor().update_match_with_wl_values(match)
                match[cn.TRIGGERED_BY] = self.engine.set_trigger_reasons(
                    match, self.pipeline_config.FUZZINESS_LEVEL
                )
                self.engine.set_beneficiary_hits(match)

            self.engine.connect_full_names(
                alerted_parties, [cn.PRTY_FST_NM, cn.PRTY_MDL_NM, cn.PRTY_LST_NM]
            )
            self.engine.connect_full_names(accounts, [cn.ACCOUNT_FIRST_NAME, cn.ACCOUNT_LAST_NAME])
            self.engine.collect_party_values_from_parties(alerted_parties, payload)
            self.engine.collect_party_values_from_accounts(accounts, payload)
            self.engine.collect_party_values_from_parties_from_fields(fields, payload)
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
            for match in matches:
                match[cn.AP_TRIGGERS] = self.engine.set_triggered_tokens_discovery(match, fields)
                self.set_up_party_type(payload, match)
            self.set_up_party_type(payload, payload)
            self.set_up_dataset_type_match(payload, match)
        return payloads

    def parse_key(self, value, match, payload, new_config):
        temp_dict = dict(value)
        for new_key in temp_dict:
            for element in temp_dict[new_key]:
                elements = element.split(".")
                if cn.MATCH_RECORDS in element:
                    value = match
                    elements = elements[2:]
                else:

                    value = payload
                for field_name in elements:
                    if field_name == cn.INPUT_FIELD:
                        try:
                            value = value[0][field_name][elements[-1]].value
                        except (AttributeError, KeyError):
                            value = None
                        break
                    try:
                        value = value.get(field_name, None)
                    except TypeError:
                        key = PayloadLoader.LIST_ELEMENT_REGEX.sub("", field_name)
                        ix = int(PayloadLoader.LIST_ELEMENT_REGEX.match(field_name).groups(0))
                        value = value[key][ix]
                new_config[elements[-1]] = value

    def get_key(self, payload, match, conf):
        new_config = {}
        for _, value in dict(conf).items():
            try:
                self.parse_key(value, match, payload, new_config)
            except:
                logger.warning(f"Field {value} does not exist in payload")
        return new_config

    def transform_cleansed_to_application(self, payloads):
        for payload in payloads:
            matches = payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
            agent_config, yaml_conf = self.alert_agents_config["alert_type"]
            agent_input_prepended_agent_name_config = (
                prepend_agent_name_to_ap_or_wl_or_aliases_key(agent_config)
            )

            agent_input_agg_col_config = create_agent_input_agg_col_config(
                agent_input_prepended_agent_name_config
            )

            for match in matches:
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
                input_records = payload[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][
                    cn.INPUT_RECORDS
                ]
                fields = input_records[0][cn.INPUT_FIELD]
                self.handle_hit_type_agent(match, agent_config, fields)
                self.select_ap_for_ap_id_tp_marked_agent(payload, match)
                self.set_up_entity_type_match(match)

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

                match["all_hit_type_aggregated"] = {
                    key.split("_")[-1]: [i for i in match[key] if i]
                    for key in match
                    if key.startswith("hit")
                }
                self.remove_nulls_from_aggegated(match)

        return payloads

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

    def remove_nulls_from_aggegated(self, match):
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

    def select_ap_for_ap_id_tp_marked_agent(self, payload, match):
        dataset_name = payload[cn.ALERTED_PARTY_FIELD][cn.HEADER_INFO][cn.DATASET_NAME]
        if dataset_name in ["R_Global_MultiParty_Daily", "R_Global_MultiParty_Wkly"]:
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][0]
        elif dataset_name in ["R_US_Active_Address", "R_US_Active_Party"]:
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][1]
        elif dataset_name in ["R_Global_MultiAccounts_Wkly", "R_Global_MultiAccounts_Daily"]:
            match["ap_id_tp_marked_agent_input"] = match["ap_id_tp_marked_agent_input"][2]
        if not match["ap_id_tp_marked_agent_input"]:
            logger.warning("No input for ap_id_tp_marked_agent")
            match["ap_id_tp_marked_agent_input"] = ""

    def set_up_party_type(self, payload, match):
        types = [i for i in payload[cn.ALL_CONNECTED_PARTY_TYPES] if i]
        if not types:
            match["AP_PARTY_TYPE"] = "UNKNOWN"
        if "Individual" in types:
            match["AP_PARTY_TYPE"] = "I"
        else:
            match["AP_PARTY_TYPE"] = "C"

    def set_up_entity_type_match(self, match):
        if match["AP_PARTY_TYPE"] == match["WLP_TYPE"]:
            match["ENTITY_TYPE_MATCH"] = "Y"
        else:
            match["ENTITY_TYPE_MATCH"] = "N"

    def set_up_dataset_type_match(self, payload, match):
        match[cn.DATASET_TYPE] = CUSTOMER_S8_MAP.get(
            payload[cn.ALERTED_PARTY_FIELD][cn.HEADER_INFO][cn.DATASET_NAME], ""
        )

from copy import deepcopy

from etl_pipeline.config import alert_agents_config
from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.custom.ms.datatypes.field import InputRecordField
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.custom.ms.transformations import (
    create_agent_input_agg_col_config,
    prepend_agent_name_to_ap_or_wl_or_aliases_key,
)
from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from etl_pipeline.pipeline import ETLPipeline


class MSPipeline(ETLPipeline):
    def convert_raw_to_standardized(self, df):
        return df

    def flatten_parties(self, parties):
        for num, party in enumerate(parties):
            parties[num] = party["fields"]

    def parse_input_records(self, payload):
        for input_record in payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST]:
            input_record["INPUT_FIELD"] = {
                i["name"]: InputRecordField(**i) for i in input_record["field"]
            }

    def connect_input_record_with_match_record(self, payload):
        new_payloads = []
        for input_record in payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST]:
            for match_record in payload[cn.ALERT_FIELD][cn.MATCH_RECORDS]:
                if input_record["versionId"] == match_record["inputVersionId"]:
                    pair_payload = deepcopy(payload)
                    for num, input_record_to_remove in enumerate(
                        payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST]
                    ):
                        if input_record["versionId"] != input_record_to_remove["versionId"]:
                            pair_payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST][num] = None

                    for num, match_record_to_remove in enumerate(
                        payload[cn.ALERT_FIELD][cn.MATCH_RECORDS]
                    ):
                        if (
                            match_record["inputVersionId"]
                            != match_record_to_remove["inputVersionId"]
                        ):
                            pair_payload[cn.ALERT_FIELD][cn.MATCH_RECORDS][num] = None

                    pair_payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST] = [
                        elem for elem in pair_payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST] if elem
                    ]
                    pair_payload[cn.ALERT_FIELD][cn.MATCH_RECORDS] = [
                        elem for elem in pair_payload[cn.ALERT_FIELD][cn.MATCH_RECORDS] if elem
                    ]

                    new_payloads.append(pair_payload)

        return new_payloads

    def transform_standardized_to_cleansed(self, payloads):
        parties = payloads[cn.SUPPLEMENTAL_INFO][cn.RELATED_PARTIES][cn.PARTIES]
        self.flatten_parties(parties)
        self.parse_input_records(payloads)

        payloads = self.connect_input_record_with_match_record(payloads)

        for payload in payloads:
            matches = payload[cn.ALERT_FIELD][cn.MATCH_RECORDS]
            fields = payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST][0]["INPUT_FIELD"]
            for match in matches:
                WatchlistExtractor().update_match_with_wl_values(match)
                match[cn.TRIGGERED_BY] = self.engine.set_trigger_reasons(
                    match, self.pipeline_config.FUZZINESS_LEVEL
                )
                self.engine.set_beneficiary_hits(match)

            self.engine.connect_full_names(parties)

            self.engine.collect_party_values_from_parties(parties, payload)
            self.engine.collect_party_values_from_parties_from_fields(fields, payload)
            payload[cn.ALL_CONNECTED_PARTY_TYPES] = payload[cn.ALL_PARTY_TYPES]
            names_source_cols = [
                cn.ALL_PARTY_NAMES,
                cn.ALL_CONNECTED_PARTIES_NAMES,
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
                match[cn.AP_TRIGGERS] = self.engine.set_triggered_tokens_discovery(
                    payload, match, fields
                )
        return payloads

    def get_key(self, payload, match, conf):
        new_config = {}
        for key, value in dict(conf).items():
            temp_dict = dict(value)
            for new_key in temp_dict:
                for element in temp_dict[new_key]:
                    elements = element.split(".")
                    if cn.MATCH_RECORDS in element:
                        value = match
                        elements = elements[1:]
                    else:
                        value = payload

                    for field_name in elements:
                        if field_name == "INPUT_FIELD":
                            try:
                                value = value[0][field_name][elements[-1]].value
                            except AttributeError:
                                value = None
                            break
                        try:
                            value = value.get(field_name, None)
                        except TypeError:
                            key = PayloadLoader.LIST_ELEMENT_REGEX.sub("", field_name)
                            ix = int(PayloadLoader.LIST_ELEMENT_REGEX.match(field_name).groups(0))
                            value = value[key][ix]
                    new_config[elements[-1]] = value
        return new_config

    def load_agent_config(self, alert_type="WM_ADDRESS"):
        alert_config = alert_agents_config[alert_type]
        parsed_agent_config = {}
        for agent_name, agent_config in dict(alert_config).items():
            particular_agent_config = dict(agent_config)
            parsed_agent_config[agent_name] = {}
            for new_key in particular_agent_config:
                parsed_agent_config[agent_name][new_key] = []
                for element in particular_agent_config[new_key]:
                    elements = element.split(".")
                    parsed_agent_config[agent_name][new_key].append(elements[-1])
        return parsed_agent_config, alert_config

    def transform_cleansed_to_application(self, payloads):

        for payload in payloads:
            matches = payload[cn.ALERT_FIELD][cn.MATCH_RECORDS]
            agent_config, yaml_conf = self.load_agent_config(
                payload[cn.ALERT_FIELD]["headerInfo"]["datasetName"]
            )
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
                match.update(
                    {
                        key: self.flatten(match.get(key))
                        for key in match
                        if key.endswith("_ap") or key.endswith("_wl")
                    }
                )

                config.update(
                    {
                        key: self.flatten(match.get(key))
                        for key in match
                        if key.endswith("_ap") or key.endswith("_wl")
                    }
                )
                self.engine.sql_to_merge_specific_columns_to_standardized(
                    agent_input_agg_col_config, match, config, False
                )

                self.remove_nulls_from_aggegated(match)

        return payloads

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
            if key.endswith("_aggregated"):
                value = match[key]
                if isinstance(value, list):
                    match.update({key: [i for i in match.get(key) if i]})
                else:
                    match.update({key: [value] if value else []})

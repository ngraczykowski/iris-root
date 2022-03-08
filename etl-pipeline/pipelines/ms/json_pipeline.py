import os

from omegaconf import OmegaConf

from config import CONFIG_APP_DIR, columns_namespace
from custom.ms.datatypes.field import InputRecordField
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

    def transform_standardized_to_cleansed(self, payload):
        payload = {key: payload[key] for key in sorted(payload)}
        payload = PayloadLoader().load_payload_from_json(payload)

        matches = payload["matchesPayloads"]
        parties = payload["alertPayload"]["supplementalInfo"]["parties"]
        payload["alertPayload"]["inputRecord"]["fields"] = {
            i["name"]: InputRecordField(**i)
            for i in payload["alertPayload"]["inputRecord"]["fields"]
        }
        fields = payload["alertPayload"]["inputRecord"]["fields"]

        for match in matches:
            WatchlistExtractor().update_match_with_wl_values(match)
            match[columns_namespace.TRIGGERED_BY] = self.engine.set_trigger_reasons(
                match, self.pipeline_config.FUZZINESS_LEVEL
            )
            self.engine.set_beneficiary_hits(match)
        self.engine.connect_full_names(parties)
        self.engine.collect_party_values(parties, payload)
        payload[columns_namespace.ALL_CONNECTED_PARTIES_NAMES] = payload[
            columns_namespace.ALL_PARTY_TYPES
        ]
        names_source_cols = [
            columns_namespace.ALL_PARTY_NAMES,
            columns_namespace.ALL_CONNECTED_PARTIES_NAMES,
        ]

        payload.update(
            {
                columns_namespace.CLEANED_NAMES: self.engine.get_clean_names_from_concat_name(
                    fields.get(fields.get(columns_namespace.CONCAT_ADDRESS, "")),
                    {key: payload[key] for key in names_source_cols},
                )
            }
        )
        payload.update(
            {
                columns_namespace.CONCAT_RESIDUE: payload[columns_namespace.CLEANED_NAMES][
                    columns_namespace.CONCAT_RESIDUE
                ]
            }
        )
        concat_residue = payload[columns_namespace.CONCAT_RESIDUE]
        concat_address = fields.get(
            fields.get(columns_namespace.CONCAT_ADDRESS, ""),
            "",
        )

        payload.update(
            {columns_namespace.CONCAT_ADDRESS_NO_CHANGES: concat_residue == concat_address}
        )
        for record in matches:
            record[columns_namespace.AP_TRIGGERS] = self.engine.set_triggered_tokens_discovery(
                payload, record, fields
            )

        return payload

    def get_key(self, payload, match_id, conf):
        new_config = {}
        for key, value in dict(conf).items():
            temp_dict = dict(value)
            for new_key in temp_dict:
                for element in temp_dict[new_key]:
                    elements = element.split(".")
                    if "matchesPayloads" in element:
                        value = payload["matchesPayloads"][match_id]
                        elements = elements[1:]
                    else:
                        value = payload
                    for field_name in elements:
                        try:
                            value = value.get(field_name, None)
                        except TypeError:
                            key = PayloadLoader.LIST_ELEMENT_REGEX.sub("", field_name)
                            ix = int(PayloadLoader.LIST_ELEMENT_REGEX.match(field_name).groups(0))
                            value = value[key][ix]
                    new_config[elements[-1]] = value
        return new_config

    def load_config(self, alert_type="WM_ADDRESS"):
        filenames = {"WM_ADDRESS": os.path.join(CONFIG_APP_DIR, "agents_input_WM_ADDRESS.yaml")}
        yaml_conf = OmegaConf.load(filenames[alert_type])
        agent_config = {}
        for key, value in dict(yaml_conf).items():
            temp_dict = dict(value)
            party_config = {}
            agent_config[key] = party_config
            for new_key in temp_dict:
                party_config[new_key] = []

                for element in temp_dict[new_key]:
                    elements = element.split(".")
                    party_config[new_key].append(elements[-1])
        return agent_config, yaml_conf

    def transform_cleansed_to_application(self, payload):
        matches = payload["matchesPayloads"]
        agent_config, yaml_conf = self.load_config()
        agent_input_prepended_agent_name_config = prepend_agent_name_to_ap_or_wl_or_aliases_key(
            agent_config
        )

        agent_input_agg_col_config = create_agent_input_agg_col_config(
            agent_input_prepended_agent_name_config
        )

        for num, match in enumerate(matches):
            config = self.get_key(payload, num, yaml_conf)
            self.engine.sql_to_merge_specific_columns_to_standardized(
                agent_input_prepended_agent_name_config,
                match,
                config,
                False,
            )
            config.update(
                {
                    key: match.get(key)
                    for key in match
                    if key.endswith("_ap") or key.endswith("_wl")
                }
            )
            self.engine.sql_to_merge_specific_columns_to_standardized(
                agent_input_agg_col_config, match, config, True
            )
        return payload

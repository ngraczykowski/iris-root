from etl_pipeline.config import agents_config
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

    def transform_standardized_to_cleansed(self, payload):
        match_ids = payload[cn.MATCH_IDS]
        matches = payload[cn.ALERT_FIELD][cn.MATCH_RECORDS]

        parties = payload[cn.SUPPLEMENTAL_INFO][cn.RELATED_PARTIES][cn.PARTIES]

        for num, party in enumerate(parties):
            parties[num] = party["fields"]

        payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST][0]["INPUT_RECORD_FIELD"] = {
            i["name"]: InputRecordField(**i)
            for i in payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST][0]["field"]
        }

        fields = payload[cn.ALERT_FIELD][cn.INPUT_RECORD_HIST][0]["INPUT_RECORD_FIELD"]

        for match_id in match_ids:
            match = matches[match_id]
            WatchlistExtractor().update_match_with_wl_values(match)
            match[cn.TRIGGERED_BY] = self.engine.set_trigger_reasons(
                match, self.pipeline_config.FUZZINESS_LEVEL
            )
            self.engine.set_beneficiary_hits(match)

        self.engine.connect_full_names(parties)

        self.engine.collect_party_values(parties, payload)
        payload[cn.ALL_CONNECTED_PARTY_TYPES] = payload[cn.ALL_PARTY_TYPES]
        names_source_cols = [
            cn.ALL_PARTY_NAMES,
            cn.ALL_CONNECTED_PARTIES_NAMES,
        ]

        payload.update(
            {
                cn.CLEANED_NAMES: self.engine.get_clean_names_from_concat_name(
                    fields.get(cn.CONCAT_ADDRESS, None).value,
                    {key: payload[key] for key in names_source_cols},
                )
            }
        )

        payload.update({cn.CONCAT_RESIDUE: payload[cn.CLEANED_NAMES][cn.CONCAT_RESIDUE]})

        concat_residue = payload[cn.CONCAT_RESIDUE]
        concat_address = fields.get(cn.CONCAT_ADDRESS, None).value

        payload.update({cn.CONCAT_ADDRESS_NO_CHANGES: concat_residue == concat_address})
        for match_id in match_ids:
            match = matches[match_id]
            match[cn.AP_TRIGGERS] = self.engine.set_triggered_tokens_discovery(
                payload, match, fields
            )

        return payload

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
                        if field_name == "INPUT_RECORD_FIELD":
                            value = value[0][field_name][elements[-1]].value
                            break
                        try:
                            value = value.get(field_name, None)
                        except TypeError:
                            key = PayloadLoader.LIST_ELEMENT_REGEX.sub("", field_name)
                            ix = int(PayloadLoader.LIST_ELEMENT_REGEX.match(field_name).groups(0))
                            value = value[key][ix]
                    new_config[elements[-1]] = value
        return new_config

    def load_config(self, alert_type="WM_ADDRESS"):
        yaml_conf = agents_config[alert_type]
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
        match_ids = payload[cn.MATCH_IDS]
        matches = payload[cn.ALERT_FIELD][cn.MATCH_RECORDS]
        agent_config, yaml_conf = self.load_config()
        agent_input_prepended_agent_name_config = prepend_agent_name_to_ap_or_wl_or_aliases_key(
            agent_config
        )

        agent_input_agg_col_config = create_agent_input_agg_col_config(
            agent_input_prepended_agent_name_config
        )

        for match_id in match_ids:
            match = matches[match_id]
            config = self.get_key(payload, match, yaml_conf)
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

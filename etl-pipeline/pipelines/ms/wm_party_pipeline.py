from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.custom.ms.transformations import (
    create_agent_input_agg_col_config,
    prepend_agent_name_to_ap_or_wl_or_aliases_key,
)
from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor
from pipelines.ms.wm_address_pipeline import MSPipeline as WMPipeline


class MSPipeline(WMPipeline):
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

                match.update(
                    {
                        key: self.flatten(match.get(key))
                        for key in match
                        if key.endswith("_aggregated")
                    }
                )
                self.remove_nulls_from_aggegated(match)
        return payloads

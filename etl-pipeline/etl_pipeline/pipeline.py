from abc import ABC

from etl_pipeline.data_processor_engine.engine import Engine


class ETLPipeline(ABC):
    def __init__(self, engine: Engine):
        self.engine = engine

    def convert_raw_to_standardized(self):
        pass

    def transform_standardized_to_cleansed(self):
        pass

    def transform_cleansed_to_application(self):
        pass

    def merge_and_clean_empty_hits(self, alert_ap_wl_hit_names_df):
        wl_hit_names_sql = self.engine.merge_to_target_col_from_source_cols(
            alert_ap_wl_hit_names_df,
            "wl_hit_names",
            ["wl_hit_matched_name", "wl_hit_aliases_matched_name"],
        )
        alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.select("*", wl_hit_names_sql)
        merge_hit_and_aliases_displayName_sql = self.engine.merge_to_target_col_from_source_cols(
            alert_ap_wl_hit_names_df,
            "wl_hit_names",
            ["hit_displayName", "hit_aliases_displayName"],
        )
        alert_ap_wl_hit_names_df = self.engine.substitute_nulls_in_array_with_new_values(
            alert_ap_wl_hit_names_df, "wl_hit_names", merge_hit_and_aliases_displayName_sql
        )
        merge_hit_and_aliases_displayName_sql = self.engine.merge_to_target_col_from_source_cols(
            alert_ap_wl_hit_names_df,
            "wl_hit_names",
            ["hit_displayName", "hit_aliases_displayName"],
        )
        alert_ap_wl_hit_names_df = self.engine.substitute_nulls_in_array_with_new_values(
            alert_ap_wl_hit_names_df, "wl_hit_names", merge_hit_and_aliases_displayName_sql
        )
        merge_ap_names_sql = self.engine.merge_to_target_col_from_source_cols(
            alert_ap_wl_hit_names_df, "ap_hit_names", ["alert_ahData_partyName"], return_array=True
        )
        alert_ap_wl_hit_names_df = self.engine.substitute_nulls_in_array_with_new_values(
            alert_ap_wl_hit_names_df, "ap_hit_names", merge_ap_names_sql
        )
        return alert_ap_wl_hit_names_df

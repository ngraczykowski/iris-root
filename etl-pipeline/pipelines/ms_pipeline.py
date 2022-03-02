from etl_pipeline.pipeline import ETLPipeline


class MSPipeline(ETLPipeline):
    def convert_raw_to_standardized(self, df):
        return df

    def transform_standardized_to_cleansed(self, data):
        data = self.engine.set_ref_key(data)
        data.registerTempTable("df360")
        data = self.ms_customized_merge_df360_and_WM_Account_In_Scope()
        data = self.engine.set_trigger_reasons(data)
        data = self.engine.merge_with_party_and_address_relationships(data)
        data = self.engine.set_beneficiary_hits(data)
        data = self.engine.set_clean_names(data)
        data = self.engine.set_concat_residue(data)
        data = self.engine.set_concat_address_no_change(data)
        data = self.engine.set_discovery_tokens(data)
        return data

    def transform_cleansed_to_application(self, cleansed_alert_df):
        agent_input_agg_df = self.engine.prepare_agent_inputs(cleansed_alert_df)
        return agent_input_agg_df

    def ms_customized_merge_df360_and_WM_Account_In_Scope(self):
        path = self.pipeline_config.WM_ACCOUNTS_IN_SCOPE_INPUT_PATH
        WM_Account_In_Scope = self.engine.spark_instance.read_csv(path)

        for col in WM_Account_In_Scope.columns:
            WM_Account_In_Scope = WM_Account_In_Scope.withColumnRenamed(col, col.replace(".", "_"))

        WM_Account_In_Scope.registerTempTable("WM_Account_In_Scope")

        df360_v2 = self.engine.spark_instance.spark_instance.sql(
            "select * from df360 left join WM_Account_In_Scope WM_Account "
            "on df360.SRC_SYS_ACCT_KEY = WM_Account.AD_SRC_SYS_ACCT_KEY"
        )
        return df360_v2

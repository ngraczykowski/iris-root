import json

import pyspark
import pyspark.sql.functions as F
from pyspark.sql.types import ArrayType, MapType, NullType, StringType
from spark_manager.spark_client import SparkClient
from spark_manager.spark_config import SPARK_CONF

from etl_pipeline.agent_input_creator.sql import sql_to_merge_specific_columns_to_standardized
from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.custom.ms.transformations import (
    create_agent_input,
    create_agent_input_agg_col_config,
    custom_determine_wl_type,
    custom_udf_derive_party_type_from_acct_class_descr,
    custom_udf_to_analyst_solution,
    prepend_agent_name_to_ap_or_wl_or_aliases_key,
    udf_select_ap_names,
    udf_select_wl_names,
    udf_split_ap_names_by_ms_delimiter,
)
from etl_pipeline.data_processor_engine.engine.engine import ProcessingEngine
from etl_pipeline.pattern import AccountType


def extract_party_fields(field_name):
    return F.expr(f'FILTER(TRANSFORM(ALL_PARTY_DETAILS, x -> x.{field_name}), x -> x != "")')


class SparkProcessingEngine(ProcessingEngine):
    REF_KEY_REGEX = r"(\d{4}-\d{2}-\d{2}-\d{2}.\d{2}.\d{2}.\d{6})"

    def __init__(self):
        self.spark_instance = SparkClient(SPARK_CONF)

    def set_ref_key(self, data):
        data = data.withColumn(
            cn.SRC_SYS_ACCT_KEY,
            F.regexp_extract(F.col(cn.SRC_REF_KEY), self.REF_KEY_REGEX, 1),
        )
        return data

    @staticmethod
    @F.udf(returnType=ArrayType(StringType()))
    def _set_trigger_reasons(values):
        values = values.asDict()
        return ProcessingEngine.set_trigger_reasons(values)

    @staticmethod
    def set_trigger_reasons(data):
        data = data.withColumn(
            cn.TRIGGERED_BY,
            SparkProcessingEngine._set_trigger_reasons(F.struct(data.columns)),
        )
        return data

    def set_beneficiary_hits(self, data):
        check_beneficiary_hit = F.when(
            F.array_contains(F.col(cn.TRIGGERED_BY), cn.AD_BNFL_NM),
            F.lit(True),
        ).otherwise(F.lit(False))
        data = data.withColumn(cn.IS_BENEFICIARY_HIT, check_beneficiary_hit)
        return data

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

    def load_ap_data(self):
        path = self.pipeline_config.PARTY_AND_ADDRESS_RELATIONSHIP_INPUT_PATH
        ap_df = self.spark_instance.read_csv(path)

        ap_df = ap_df.withColumnRenamed(cn.ACCT_PMH_ADDR_ID, cn.ADDRESS_ID).withColumnRenamed(
            cn.PRTY_ID, cn.PARTY_ID
        )
        ap_df.registerTempTable("ap")
        return ap_df

    def load_parties_in_scope_data(self):
        path = self.pipeline_config.WM_PARTIES_IN_SCOPE_INPUT_PATH
        parties_in_scope_df = self.spark_instance.read_csv(path)
        parties_in_scope_df = parties_in_scope_df.withColumnRenamed(cn.PRTY_ID, cn.PARTY_ID)
        return parties_in_scope_df

    def connect_data_via_account_num(self, data, ap_df, parties_in_scope_df):
        df_connected_via_account_num = (
            data.groupBy(
                cn.ALERT_ID,
                cn.SRC_SYS_ACCT_KEY,
                cn.ACCT_NUM,
            )
            .count()
            .withColumnRenamed(cn.PARTY_ID, cn.ALERTED_PARTY_NAME)
            .join(
                ap_df,
                (ap_df.SRC_SYS_ACCT_KEY == data.SRC_SYS_ACCT_KEY)
                & (F.trim(ap_df.ACCT_NUM) == F.trim(data.ACCT_NUM)),
            )
            .join(parties_in_scope_df, [cn.PARTY_ID], "leftouter")
            .select(
                cn.ALERT_ID,
                cn.PARTY_ID,
                cn.PRTY_NM,
                F.concat_ws(
                    " ",
                    F.col(cn.PRTY_FST_NM),
                    F.col(cn.PRTY_MDL_NM),
                    F.col(cn.PRTY_LST_NM),
                ).alias(cn.CONNECTED_FULL_NAME),
                cn.PRTY_TYP,
                cn.DOB_DT,
                cn.PRTY_CNTRY_OF_BIRTH,
                cn.PRTY_PRIM_CTZNSH_CNTRY,
                cn.PRTY_RSDNC_CNTRY_CD,
            )
        )
        return df_connected_via_account_num

    def connect_data_via_account_id(self, data, ap_df, parties_in_scope_df):
        df_connected_via_address_id = (
            data.groupBy(
                cn.ALERT_ID,
                cn.SRC_SYS_ACCT_KEY,
                cn.ADDRESS_ID,
            )
            .count()
            .withColumnRenamed(cn.PARTY_ID, cn.ALERTED_PARTY_NAME)
            .join(
                ap_df,
                (ap_df.SRC_SYS_ACCT_KEY == data.SRC_SYS_ACCT_KEY)
                & (F.trim(ap_df.ADDRESS_ID) == F.trim(data.ADDRESS_ID)),
            )
            .join(parties_in_scope_df, [cn.PARTY_ID], "leftouter")
            .select(
                cn.ALERT_ID,
                cn.PARTY_ID,
                cn.PRTY_NM,
                F.concat_ws(
                    " ",
                    F.col(cn.PRTY_FST_NM),
                    F.col(cn.PRTY_MDL_NM),
                    F.col(cn.PRTY_LST_NM),
                ).alias(cn.CONNECTED_FULL_NAME),
                cn.PRTY_TYP,
                cn.DOB_DT,
                cn.PRTY_CNTRY_OF_BIRTH,
                cn.PRTY_PRIM_CTZNSH_CNTRY,
                cn.PRTY_RSDNC_CNTRY_CD,
            )
        )
        return df_connected_via_address_id

    def connect_parties_name(self, df_connected):
        connected_details_df = df_connected.groupBy(cn.ALERT_ID).agg(
            F.collect_list(cn.CONNECTED_FULL_NAME).alias(cn.ALL_CONNECTED_PARTIES_NAMES)
        )
        return connected_details_df

    def merge_with_party_and_address_relationships(self, data):
        ap_df = self.load_ap_data()
        parties_in_scope_df = self.load_parties_in_scope_data()
        df_connected_via_account_num = self.connect_data_via_account_num(
            data, ap_df, parties_in_scope_df
        )
        df_connected_via_address_id = self.connect_data_via_account_id(
            data, ap_df, parties_in_scope_df
        )
        df_connected = df_connected_via_account_num.unionAll(df_connected_via_address_id)
        connected_details_df = self.connect_parties_name(df_connected)

        enhanced_df360 = (
            data.groupBy(
                cn.ALERT_ID,
                cn.SRC_SYS_ACCT_KEY,
                cn.ADDRESS_ID,
            )
            .count()
            .join(
                ap_df,
                [cn.SRC_SYS_ACCT_KEY, cn.ADDRESS_ID],
                "leftouter",
            )
            .join(parties_in_scope_df, [cn.PARTY_ID], "leftouter")
            .select(
                cn.ALERT_ID,
                cn.SRC_SYS_ACCT_KEY,
                cn.ADDRESS_ID,
                cn.PRTY_NM,
                cn.PRTY_TYP,
                cn.DOB_DT,
                cn.PRTY_CNTRY_OF_BIRTH,
                cn.PRTY_PRIM_CTZNSH_CNTRY,
                cn.PRTY_RSDNC_CNTRY_CD,
            )
        )

        references_details_cols = self.pipeline_config.REFERENCES_COLUMNS

        enhancement_details_df = enhanced_df360.groupBy(
            cn.ALERT_ID,
            cn.SRC_SYS_ACCT_KEY,
            cn.ADDRESS_ID,
        ).agg(
            F.collect_list(
                F.struct(
                    [F.coalesce(F.col(x), F.lit("")).alias(x) for x in references_details_cols]
                )
            ).alias(cn.ALL_PARTY_DETAILS)
        )

        to_aggregate = data.join(
            enhancement_details_df,
            [
                cn.ALERT_ID,
                cn.SRC_SYS_ACCT_KEY,
                cn.ADDRESS_ID,
            ],
        )
        final_df360 = (
            to_aggregate.withColumn(cn.ALL_PARTY_TYPES, extract_party_fields(cn.PRTY_TYP))
            .withColumn(cn.ALL_PARTY_NAMES, extract_party_fields(cn.PRTY_NM))
            .withColumn(cn.ALL_PARTY_DOBS, extract_party_fields(cn.DOB_DT))
            .withColumn(
                cn.ALL_PARTY_BIRTH_COUNTRIES,
                extract_party_fields(cn.PRTY_CNTRY_OF_BIRTH),
            )
            .withColumn(
                cn.ALL_PARTY_CITIZENSHIP_COUNTRIES,
                extract_party_fields(cn.PRTY_PRIM_CTZNSH_CNTRY),
            )
            .withColumn(
                cn.ALL_PARTY_RESIDENCY_COUNTRIES,
                extract_party_fields(cn.PRTY_RSDNC_CNTRY_CD),
            )
        )

        final_df360 = final_df360.join(connected_details_df, [cn.ALERT_ID], "leftouter")

        return final_df360

    def set_clean_names(self, data):
        names_source_cols = [
            cn.ALL_PARTY_NAMES,
            cn.ALL_CONNECTED_PARTIES_NAMES,
        ]
        return data.withColumn(
            cn.CLEANED_NAMES,
            SparkProcessingEngine.prepare_and_get_clean_names_from_concat_name(
                cn.CONCAT_ADDRESS, *[F.lit(i) for i in names_source_cols]
            ),
        )

    @staticmethod
    @F.udf(MapType(StringType(), StringType()))
    def prepare_and_get_clean_names_from_concat_name(concat_field, *columns):
        return ProcessingEngine.prepare_and_get_clean_names_from_concat_name(
            concat_field, *columns
        )

    def set_concat_residue(self, data):
        data = data.withColumn(
            cn.CONCAT_RESIDUE,
            F.col(cn.CLEANED_NAMES).getItem(cn.CONCAT_RESIDUE),
        )
        return data

    def set_concat_address_no_change(self, data):
        data = data.withColumn(
            cn.CONCAT_ADDRESS_NO_CHANGES,
            F.when(
                F.col(cn.CONCAT_RESIDUE) == F.col(cn.CONCAT_ADDRESS),
                F.col(cn.CONCAT_RESIDUE),
            ).otherwise(F.lit(None)),
        )
        return data

    def set_discovery_tokens(self, data):
        return data.withColumn(
            cn.AP_TRIGGERS,
            SparkProcessingEngine.custom_create_triggered_tokens_discovery_udf(
                self.pipeline_config.AP_COLUMNS
            ),
        )

    @staticmethod
    def custom_create_triggered_tokens_discovery_udf(columns):
        @F.udf(MapType(StringType(), MapType(StringType(), ArrayType(StringType()))))
        def udf_discover(matched_tokens, *values):
            dict_values = {}
            for i in range(len(columns)):
                if values[i] != "":
                    dict_values[columns[i]] = values[i]
            return ProcessingEngine.discover(eval(matched_tokens), dict_values)

        return udf_discover(cn.WL_MATCHED_TOKENS, *columns)

    def select_columns_for_convert(self, alert_type):
        cols_to_convert = []
        if alert_type == AccountType.WM_ADDRESS:
            cols_to_convert = self.pipeline_config.WM_ADDRESS_COLS_TO_CONVERT_FROM_JSON
        elif alert_type == AccountType.ISG_ACCOUNT:
            cols_to_convert = self.pipeline_config.ISG_ACCOUNT_COLS_TO_CONVERT_FROM_JSON
        return cols_to_convert

    def prepare_agent_inputs(self, cleansed_alert_df):
        ALERT_TYPE = AccountType.WM_ADDRESS
        cols_to_convert = self.select_columns_for_convert(ALERT_TYPE)

        for c in cols_to_convert:
            cleansed_alert_df = cleansed_alert_df.withColumn(
                c, custom_udf_parse_array_from_string(c)
            )

        if ALERT_TYPE == AccountType.ISG_ACCOUNT:
            cleansed_alert_df = cleansed_alert_df.withColumn(
                cn.DELIVERED_ALERTED_PARTY_TYPE,
                custom_udf_derive_party_type_from_acct_class_descr(cn.ACCT_CLAS_DESCR),
            )
            cleansed_alert_df = cleansed_alert_df.withColumn(
                cn.WP_TYPE,
                custom_determine_wl_type(cn.WL_ENTITYTYPE),
            )

        cleansed_alert_df = cleansed_alert_df.withColumn(
            cn.ANALYST_SOLUTION,
            custom_udf_to_analyst_solution(cn.STATUS_DESC),
        )

        if cn.NUMBER_OF_HITS not in cleansed_alert_df.columns:
            cleansed_alert_df = cleansed_alert_df.join(
                cleansed_alert_df.groupBy(cn.ALERT_ID).count(),
                cn.ALERT_ID,
            ).withColumnRenamed("count", cn.NUMBER_OF_HITS)

        cleansed_alert_df = cleansed_alert_df.withColumn(
            cn.MATCH_ID,
            F.concat(
                cn.ALERT_INTERNAL_ID,
                F.lit("_"),
                cn.ENTITY_ID,
                F.lit("_"),
                cn.ENTITY_VERSION,
            ),
        )
        cleansed_alert_df = cleansed_alert_df.dropDuplicates([cn.MATCH_ID])

        agent_input_config = create_agent_input(ALERT_TYPE)
        agent_input_prepended_agent_name_config = prepend_agent_name_to_ap_or_wl_or_aliases_key(
            agent_input_config
        )

        agent_input_agg_col_config = create_agent_input_agg_col_config(
            agent_input_prepended_agent_name_config
        )

        agent_input_agg_df = create_input_for_agents(
            cleansed_alert_df,
            agent_input_prepended_agent_name_config,
            agent_input_agg_col_config,
            spark_instance=self.spark_instance,
            destination="temp_test",
        )
        agent_input_agg_df = (
            agent_input_agg_df.withColumn(
                cn.WL_ALL_NAMES_AGGREGATED,
                udf_select_wl_names(
                    cn.AP_TRIGGERS,
                    cn.WL_ALL_NAMES_AGGREGATED,
                    cn.WL_NAME,
                ),
            )
            .withColumn(
                cn.AP_ALL_NAMES_AGGREGATED,
                udf_select_ap_names(cn.AP_TRIGGERS),
            )
            .withColumn(
                cn.AP_ALL_NAMES_AGGREGATED,
                udf_split_ap_names_by_ms_delimiter(cn.AP_ALL_NAMES_AGGREGATED),
            )
        )
        return agent_input_agg_df


def create_input_for_agents(
    cleansed_alert_df: pyspark.sql.DataFrame, config, agg_config, spark_instance, destination: str
):
    sql_expression = sql_to_merge_specific_columns_to_standardized(
        cleansed_alert_df, config, spark_instance=spark_instance
    )
    agent_input_refined_df = cleansed_alert_df.select("*", *sql_expression)

    sql_expression = sql_to_merge_specific_columns_to_standardized(
        agent_input_refined_df, agg_config, spark_instance=spark_instance, aggregated=True
    )
    agent_input_agg_df = agent_input_refined_df.select("*", *sql_expression).withColumn(
        "_index", F.col(cn.MATCH_ID)
    )
    agent_input_agg_df = agent_input_agg_df.select(
        [
            F.lit(None).cast("string").alias(i.name)
            if isinstance(i.dataType, NullType)
            else i.name
            for i in agent_input_agg_df.schema
        ]
    )
    agent_input_agg_df = (
        agent_input_agg_df.withColumn(
            "wl_all_names_aggregated",
            udf_select_wl_names(cn.AP_TRIGGERS, "wl_all_names_aggregated", "wl_name"),
        )
        .withColumn("ap_all_names_aggregated", udf_select_ap_names(cn.AP_TRIGGERS))
        .withColumn(
            "ap_all_names_aggregated",
            udf_split_ap_names_by_ms_delimiter("ap_all_names_aggregated"),
        )
    )
    return agent_input_agg_df


def custom_parse_array_from_string(x):
    if isinstance(x, str):
        return json.loads(x) if x else []
    else:
        return x


@F.udf(returnType=ArrayType(StringType()))
def custom_udf_parse_array_from_string(x):
    return custom_parse_array_from_string(x)

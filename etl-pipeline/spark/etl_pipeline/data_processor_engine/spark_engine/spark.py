import json

import pyspark.sql.functions as F
from pyspark.sql.types import ArrayType, MapType, StringType
from spark_manager.spark_client import SparkClient
from spark_manager.spark_config import SPARK_CONF

from etl_pipeline.config import pipeline_config
from etl_pipeline.data_processor_engine.engine.engine import ProcessingEngine
from etl_pipeline.pattern import AccountType


def extract_party_fields(field_name):
    return F.expr(f'FILTER(TRANSFORM(ALL_PARTY_DETAILS, x -> x.{field_name}), x -> x != "")')


class SparkProcessingEngine(ProcessingEngine):
    REF_KEY_REGEX = r"(\d{4}-\d{2}-\d{2}-\d{2}.\d{2}.\d{2}.\d{6})"

    def __init__(self, pipeline_config=pipeline_config):
        self.spark_instance = SparkClient(SPARK_CONF)
        self.cn = pipeline_config.cn

    def set_ref_key(self, data):
        data = data.withColumn(
            self.cn.SRC_SYS_ACCT_KEY,
            F.regexp_extract(F.col(self.cn.SRC_REF_KEY), self.REF_KEY_REGEX, 1),
        )
        return data

    @staticmethod
    @F.udf(returnType=ArrayType(StringType()))
    def _set_trigger_reasons(values):
        values = values.asDict()
        return ProcessingEngine.set_trigger_reasons(values)

    @staticmethod
    def set_trigger_reasons(data, cn):
        data = data.withColumn(
            cn.TRIGGERED_BY,
            SparkProcessingEngine._set_trigger_reasons(F.struct(data.columns)),
        )
        return data

    def set_beneficiary_hits(self, data):
        check_beneficiary_hit = F.when(
            F.array_contains(F.col(self.cn.TRIGGERED_BY), self.cn.AD_BNFL_NM),
            F.lit(True),
        ).otherwise(F.lit(False))
        data = data.withColumn(self.cn.IS_BENEFICIARY_HIT, check_beneficiary_hit)
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
        path = self.pipeline_config.config.PARTY_AND_ADDRESS_RELATIONSHIP_INPUT_PATH
        ap_df = self.spark_instance.read_csv(path)

        ap_df = ap_df.withColumnRenamed(
            self.cn.ACCT_PMH_ADDR_ID, self.cn.ADDRESS_ID
        ).withColumnRenamed(self.cn.PRTY_ID, self.cn.PARTY_ID)
        ap_df.registerTempTable("ap")
        return ap_df

    def load_parties_in_scope_data(self):
        path = self.pipeline_config.config.WM_PARTIES_IN_SCOPE_INPUT_PATH
        parties_in_scope_df = self.spark_instance.read_csv(path)
        parties_in_scope_df = parties_in_scope_df.withColumnRenamed(
            self.cn.PRTY_ID, self.cn.PARTY_ID
        )
        return parties_in_scope_df

    def connect_data_via_account_num(self, data, ap_df, parties_in_scope_df):
        df_connected_via_account_num = (
            data.groupBy(
                self.cn.ALERT_ID,
                self.cn.SRC_SYS_ACCT_KEY,
                self.cn.ACCT_NUM,
            )
            .count()
            .withColumnRenamed(self.cn.PARTY_ID, self.cn.ALERTED_PARTY_NAME)
            .join(
                ap_df,
                (ap_df.SRC_SYS_ACCT_KEY == data.SRC_SYS_ACCT_KEY)
                & (F.trim(ap_df.ACCT_NUM) == F.trim(data.ACCT_NUM)),
            )
            .join(parties_in_scope_df, [self.cn.PARTY_ID], "leftouter")
            .select(
                self.cn.ALERT_ID,
                self.cn.PARTY_ID,
                self.cn.PRTY_NM,
                F.concat_ws(
                    " ",
                    F.col(self.cn.PRTY_FST_NM),
                    F.col(self.cn.PRTY_MDL_NM),
                    F.col(self.cn.PRTY_LST_NM),
                ).alias(self.cn.CONNECTED_FULL_NAME),
                self.cn.PRTY_TYP,
                self.cn.DOB_DT,
                self.cn.PRTY_CNTRY_OF_BIRTH,
                self.cn.PRTY_PRIM_CTZNSH_CNTRY,
                self.cn.PRTY_RSDNC_CNTRY_CD,
            )
        )
        return df_connected_via_account_num

    def connect_data_via_account_id(self, data, ap_df, parties_in_scope_df):
        df_connected_via_address_id = (
            data.groupBy(
                self.cn.ALERT_ID,
                self.cn.SRC_SYS_ACCT_KEY,
                self.cn.ADDRESS_ID,
            )
            .count()
            .withColumnRenamed(self.cn.PARTY_ID, self.cn.ALERTED_PARTY_NAME)
            .join(
                ap_df,
                (ap_df.SRC_SYS_ACCT_KEY == data.SRC_SYS_ACCT_KEY)
                & (F.trim(ap_df.ADDRESS_ID) == F.trim(data.ADDRESS_ID)),
            )
            .join(parties_in_scope_df, [self.cn.PARTY_ID], "leftouter")
            .select(
                self.cn.ALERT_ID,
                self.cn.PARTY_ID,
                self.cn.PRTY_NM,
                F.concat_ws(
                    " ",
                    F.col(self.cn.PRTY_FST_NM),
                    F.col(self.cn.PRTY_MDL_NM),
                    F.col(self.cn.PRTY_LST_NM),
                ).alias(self.cn.CONNECTED_FULL_NAME),
                self.cn.PRTY_TYP,
                self.cn.DOB_DT,
                self.cn.PRTY_CNTRY_OF_BIRTH,
                self.cn.PRTY_PRIM_CTZNSH_CNTRY,
                self.cn.PRTY_RSDNC_CNTRY_CD,
            )
        )
        return df_connected_via_address_id

    def connect_parties_name(self, df_connected):
        connected_details_df = df_connected.groupBy(self.cn.ALERT_ID).agg(
            F.collect_list(self.cn.CONNECTED_FULL_NAME).alias(self.cn.ALL_CONNECTED_PARTIES_NAMES)
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
                self.cn.ALERT_ID,
                self.cn.SRC_SYS_ACCT_KEY,
                self.cn.ADDRESS_ID,
            )
            .count()
            .join(
                ap_df,
                [self.cn.SRC_SYS_ACCT_KEY, self.cn.ADDRESS_ID],
                "leftouter",
            )
            .join(parties_in_scope_df, [self.cn.PARTY_ID], "leftouter")
            .select(
                self.cn.ALERT_ID,
                self.cn.SRC_SYS_ACCT_KEY,
                self.cn.ADDRESS_ID,
                self.cn.PRTY_NM,
                self.cn.PRTY_TYP,
                self.cn.DOB_DT,
                self.cn.PRTY_CNTRY_OF_BIRTH,
                self.cn.PRTY_PRIM_CTZNSH_CNTRY,
                self.cn.PRTY_RSDNC_CNTRY_CD,
            )
        )
        references_details_cols = self.cn.REFERENCES_COLUMNS

        enhancement_details_df = enhanced_df360.groupBy(
            self.cn.ALERT_ID,
            self.cn.SRC_SYS_ACCT_KEY,
            self.cn.ADDRESS_ID,
        ).agg(
            F.collect_list(
                F.struct(
                    [F.coalesce(F.col(x), F.lit("")).alias(x) for x in references_details_cols]
                )
            ).alias(self.cn.ALL_PARTY_DETAILS)
        )

        to_aggregate = data.join(
            enhancement_details_df,
            [
                self.cn.ALERT_ID,
                self.cn.SRC_SYS_ACCT_KEY,
                self.cn.ADDRESS_ID,
            ],
        )
        final_df360 = (
            to_aggregate.withColumn(
                self.cn.ALL_PARTY_TYPES, extract_party_fields(self.cn.PRTY_TYP)
            )
            .withColumn(self.cn.ALL_PARTY_NAMES, extract_party_fields(self.cn.PRTY_NM))
            .withColumn(self.cn.ALL_PARTY_DOBS, extract_party_fields(self.cn.DOB_DT))
            .withColumn(
                self.cn.ALL_PARTY_BIRTH_COUNTRIES,
                extract_party_fields(self.cn.PRTY_CNTRY_OF_BIRTH),
            )
            .withColumn(
                self.cn.ALL_PARTY_CITIZENSHIP_COUNTRIES,
                extract_party_fields(self.cn.PRTY_PRIM_CTZNSH_CNTRY),
            )
            .withColumn(
                self.cn.ALL_PARTY_RESIDENCY_COUNTRIES,
                extract_party_fields(self.cn.PRTY_RSDNC_CNTRY_CD),
            )
        )

        final_df360 = final_df360.join(connected_details_df, [self.cn.ALERT_ID], "leftouter")

        return final_df360

    def set_clean_names(self, data):
        names_source_cols = [
            self.cn.ALL_PARTY_NAMES,
            self.cn.ALL_CONNECTED_PARTIES_NAMES,
        ]
        return data.withColumn(
            self.cn.CLEANED_NAMES,
            SparkProcessingEngine.prepare_and_get_clean_names_from_concat_name(
                self.cn.CONCAT_ADDRESS, *[F.lit(i) for i in names_source_cols]
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
            self.cn.CONCAT_RESIDUE,
            F.col(self.cn.CLEANED_NAMES).getItem(self.cn.CONCAT_RESIDUE),
        )
        return data

    def set_concat_address_no_change(self, data):
        data = data.withColumn(
            self.cn.CONCAT_ADDRESS_NO_CHANGES,
            F.when(
                F.col(self.cn.CONCAT_RESIDUE) == F.col(self.cn.CONCAT_ADDRESS),
                F.col(self.cn.CONCAT_RESIDUE),
            ).otherwise(F.lit(None)),
        )
        return data

    def set_discovery_tokens(self, data):
        return data.withColumn(
            self.cn.AP_TRIGGERS,
            SparkProcessingEngine.custom_create_triggered_tokens_discovery_udf(
                self.cn.AP_COLUMNS, self.cn
            ),
        )

    @staticmethod
    def custom_create_triggered_tokens_discovery_udf(columns, cn):
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
            cols_to_convert = self.pipeline_config.config.WM_ADDRESS_COLS_TO_CONVERT_FROM_JSON
        elif alert_type == AccountType.ISG_ACCOUNT:
            cols_to_convert = self.pipeline_config.config.ISG_ACCOUNT_COLS_TO_CONVERT_FROM_JSON
        return cols_to_convert


def custom_parse_array_from_string(x):
    if isinstance(x, str):
        return json.loads(x) if x else []
    else:
        return x


@F.udf(returnType=ArrayType(StringType()))
def custom_udf_parse_array_from_string(x):
    return custom_parse_array_from_string(x)

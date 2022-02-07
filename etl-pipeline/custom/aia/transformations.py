import os

import pyspark.sql.functions as F
from pyspark.sql.types import ArrayType, MapType, StringType

from cleaners.identity.nric import extract_ap_nric, extract_wl_nric_dob
from custom.aia.config import (
    ALERT_NOTES_FILE_NAME,
    ALERT_STATUSES_DF_FILE_NAME,
    ALERTS_FILE_NAME,
    CLEANSED_DATA_DIR,
    ITEM_STATUS_FILE_NAME,
    STANDARDIZED_DATA_DIR,
)
from custom.aia.preprocessors import add_note_stage, add_status_stage
from etl_pipeline.data_processor_engine.spark import spark_instance


def custom_transform(df):
    alert_statuses_df = spark_instance.read_delta(
        os.path.join(STANDARDIZED_DATA_DIR, ALERT_STATUSES_DF_FILE_NAME)
    ).select("STATUS_INTERNAL_ID", "STATUS_NAME")
    alert_ap_wl_hit_names_df = df.join(alert_statuses_df, "STATUS_INTERNAL_ID")
    reordered_columns = spark_instance.reorder_columns(
        alert_ap_wl_hit_names_df, "STATUS_INTERNAL_ID", ["STATUS_NAME"]
    )

    alert_ap_wl_hit_names_df = spark_instance.cast_array_null_to_array_string(
        alert_ap_wl_hit_names_df.select(reordered_columns)
    )
    custom_nric_preprocess(alert_ap_wl_hit_names_df, ALERTS_FILE_NAME)
    custom_notes_status_preprocess()


def custom_remove_status_identifiers(item_status_history_df, alert_statuses_df):
    return (
        item_status_history_df.join(
            alert_statuses_df.selectExpr("STATUS_IDENTIFIER", "STATUS_NAME as FROM_STATUS_NAME"),
            F.col("FROM_STATUS_IDENTIFIER") == F.col("STATUS_IDENTIFIER"),
            how="left",
        )
        .drop("STATUS_IDENTIFIER")
        .join(
            alert_statuses_df.selectExpr("STATUS_IDENTIFIER", "STATUS_NAME as TO_STATUS_NAME"),
            F.col("TO_STATUS_IDENTIFIER") == F.col("STATUS_IDENTIFIER"),
            how="left",
        )
        .drop("STATUS_IDENTIFIER")
    )


def custom_process_notes():
    alert_notes_df = spark_instance.read_delta(
        os.path.join(STANDARDIZED_DATA_DIR, ALERT_NOTES_FILE_NAME)
    )
    alert_notes_stage_df = add_note_stage(alert_notes_df)
    spark_instance.safe_save_delta(
        alert_notes_stage_df,
        delta_target_path=os.path.join(CLEANSED_DATA_DIR, ALERT_NOTES_FILE_NAME),
        return_df=False,
    )


def custom_process_statuses():
    item_status_history_df = spark_instance.read_delta(
        os.path.join(STANDARDIZED_DATA_DIR, ITEM_STATUS_FILE_NAME)
    )
    alert_statuses_df = spark_instance.read_delta(
        os.path.join(STANDARDIZED_DATA_DIR, ALERT_STATUSES_DF_FILE_NAME)
    )

    item_status_history_df = custom_remove_status_identifiers(
        item_status_history_df, alert_statuses_df
    )
    reordered_columns = spark_instance.reorder_columns(
        item_status_history_df, "FROM_STATUS_IDENTIFIER", ["FROM_STATUS_NAME"]
    )
    reordered_columns = spark_instance.reorder_columns(
        item_status_history_df.select(reordered_columns),
        "TO_STATUS_IDENTIFIER",
        ["TO_STATUS_NAME"],
    )
    item_status_history_df = item_status_history_df.select(reordered_columns)

    item_status_history_stage_df = add_status_stage(item_status_history_df)
    spark_instance.safe_save_delta(
        item_status_history_stage_df,
        delta_target_path=os.path.join(CLEANSED_DATA_DIR, ITEM_STATUS_FILE_NAME),
    )


def custom_notes_status_preprocess():
    custom_process_notes()
    custom_process_statuses()


def custom_nric_preprocess(df, file_name):
    alert_nric_df = df.withColumn(
        "hit_cs_1_data_points",
        F.udf(extract_wl_nric_dob, MapType(StringType(), ArrayType(StringType())))("hit_cs_1"),
    )

    alert_nric_df = alert_nric_df.withColumn(
        "ap_nric", F.udf(extract_ap_nric, ArrayType(StringType()))("alert_partyIds_idNumber")
    )

    alert_nric_df.selectExpr("size(ap_nric) as s", "ap_nric").where("s = 2").limit(2).toPandas()
    spark_instance.safe_save_delta(
        alert_nric_df,
        os.path.join(CLEANSED_DATA_DIR, file_name),
        user_metadata="Extracted AP and WL NRIC",
    )

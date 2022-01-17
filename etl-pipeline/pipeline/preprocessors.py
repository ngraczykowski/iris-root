import json
import logging
import os
import re
import time
from glob import glob

import pyspark.sql.functions as F

# from delta.tables import *  # noqa: E402,F403,F401
from pyspark.sql.types import ArrayType, MapType, StringType
from spark_manager.config.config import RAW_DATA_DIR, STANDARDIZED_DATA_DIR

from customized.xml_pipeline import AIAXMLPipeline
from etl_pipeline.agent_input_creator.input_creator import create_input
from etl_pipeline.preprocessors import add_note_stage, add_status_stage
from pipeline.agent_input_config import agent_input_prepended_agent_name_config
from pipeline.spark import spark_instance

in_application_data_dir = lambda x: os.path.join("data/4.application", x)
in_cleansed_data_dir = lambda x: os.path.join("data/3.cleansed", x)
in_raw_data_dir = lambda x: os.path.join("data/1.raw", x)
in_standardized_data_dir = lambda x: os.path.join("data/2.standardized", x)


# IMPLEMENTATION: DeltaConverter
def convert_raw_to_standardized(
    raw_data_path: str = RAW_DATA_DIR, target_path: str = STANDARDIZED_DATA_DIR
) -> None:
    # """Converts raw data to delta format.

    # Args:
    #     raw_data_path (str, optional): Path to source directory. Defaults to RAW_DATA_DIR.
    #     target_path (str, optional): Path to target directory. Defaults to STANDARDIZED_DATA_DIR.

    # Returns:
    #     None
    # """

    for file_name in os.listdir(raw_data_path):
        start = time.time()
        raw_file_path = in_raw_data_dir(file_name)
        logging.info(f"Start to process {raw_file_path}")

        standardized_file_name = re.sub(".csv$", ".delta", file_name)
        standardized_file_path = in_standardized_data_dir(standardized_file_name)
        df = spark_instance.read_csv(raw_file_path)
        df = spark_instance.safe_save_delta(df, standardized_file_path)

        logging.info(f"Data saved to {standardized_file_path}")
        logging.info(f"Time lapsed {time.time() - start:.2f} s")
        time.sleep(1)


def transform_standardized_to_cleansed():

    # file_name = 'RCMDB.ALERTS_SAMPLE.delta'
    # Implementation: Spark manager

    pipeline = AIAXMLPipeline(spark_instance=spark_instance)
    file_name = "ALERTS.delta"

    alert_ap_wl_hit_names_df = pipeline.pipeline("data/2.standardized/" + (file_name))
    # Merge wl hits columns
    wl_hit_names_sql = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
        alert_ap_wl_hit_names_df,
        "wl_hit_names",
        ["wl_hit_matched_name", "wl_hit_aliases_matched_name"],
    )
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.select("*", wl_hit_names_sql)

    # Implementation: Match/Hit Handler, Spark manager

    # Merge wl hits columns
    merge_hit_and_aliases_displayName_sql = (
        spark_instance.merge_to_target_col_from_source_cols_sql_expression(
            alert_ap_wl_hit_names_df,
            "wl_hit_names",
            ["hit_displayName", "hit_aliases_displayName"],
        )
    )
    merge_ap_names_sql = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
        alert_ap_wl_hit_names_df, "ap_hit_names", ["alert_ahData_partyName"], return_array=True
    )

    # Clear wl and ap hits columns
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.withColumn(
        "wl_hit_names",
        F.when(F.expr("size(wl_hit_names) > 0"), F.col("wl_hit_names")).otherwise(
            merge_hit_and_aliases_displayName_sql
        ),
    ).withColumn(
        "ap_hit_names",
        F.when(F.expr("size(ap_hit_names) > 0"), F.col("ap_hit_names")).otherwise(
            merge_ap_names_sql
        ),
    )

    alert_ap_wl_hit_names_df.toPandas()["ap_hit_names"]

    # Implementation: Match/Hit Handler, Spark manager
    alert_statuses_df = spark_instance.read_delta(
        in_standardized_data_dir("ACM_MD_ALERT_STATUSES.delta")
    ).select("STATUS_INTERNAL_ID", "STATUS_NAME")
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.join(
        alert_statuses_df, "STATUS_INTERNAL_ID"
    )
    reordered_columns = spark_instance.reorder_columns(
        alert_ap_wl_hit_names_df, "STATUS_INTERNAL_ID", ["STATUS_NAME"]
    )
    alert_ap_wl_hit_names_df = spark_instance.safe_save_delta(
        alert_ap_wl_hit_names_df.select(reordered_columns),
        in_cleansed_data_dir(file_name),
        user_metadata="More processing on AP and WL names, pinpoint to the exact names from AP and WL which caused the hits",
    )
    # Implementation: NRIC handler (Customer specifics)
    def extract_wl_nric_dob(custom_field):
        def _extract_yob_from_st_nrics(nrics):
            # Assuemd there is no data quality issue, e.g, 2 S or T NRICs
            for nric in nrics:
                nric_type = nric[0]
                if nric_type.lower() in ["s", "t"]:
                    two_digit_year = nric[1:3]

                    if nric_type.lower() == "s":
                        if int(two_digit_year) >= 68:
                            yob = "19" + two_digit_year
                        else:
                            yob = None
                    else:
                        yob = "20" + two_digit_year

                    return yob

        nric_match = re.match("^NRIC:.*?([STGF]\d{7}[A-Z])", custom_field)
        dob_match = re.match(".*DOB: (.+?\d{4})[,.]", custom_field)
        possible_nric_match = re.findall("([STGF]\d{7}[A-Z])", custom_field)

        if nric_match:
            nrics = nric_match.groups()
        else:
            nrics = None

        if dob_match:
            dobs = dob_match.groups()
        else:
            # Try to use NRIC to extract YOB only when there is no DOB from the text data
            if nrics:
                dobs = _extract_yob_from_st_nrics(nrics)
            else:
                dobs = None

        if possible_nric_match:
            possible_nrics = possible_nric_match
        else:
            possible_nrics = None

        return {"nric": nrics, "dob": dobs, "possible_nric": possible_nrics}

    def extract_ap_nric(ap_id_numbers):
        ap_nrics = []
        for id_number in set(ap_id_numbers):
            if id_number and re.match("^[STGF]\d{7}[A-Z]$", id_number.upper()):
                ap_nrics.append(id_number)

        return ap_nrics

    alert_nric_df = alert_ap_wl_hit_names_df.withColumn(
        "hit_cs_1_data_points",
        F.udf(extract_wl_nric_dob, MapType(StringType(), ArrayType(StringType())))("hit_cs_1"),
    )
    extract_ap_nric(["S7364776B", "S7335736B"])

    alert_nric_df = alert_nric_df.withColumn(
        "ap_nric", F.udf(extract_ap_nric, ArrayType(StringType()))("alert_partyIds_idNumber")
    )

    alert_nric_df.selectExpr("size(ap_nric) as s", "ap_nric").where("s = 2").limit(2).toPandas()
    alert_nric_df = spark_instance.safe_save_delta(
        alert_nric_df, in_cleansed_data_dir(file_name), user_metadata="Extracted AP and WL NRIC"
    )

    # Implementation: Spark manager
    alert_notes_file_name = "ACM_ALERT_NOTES.delta"
    item_status_file_name = "ACM_ITEM_STATUS_HISTORY.delta"

    alert_notes_df = spark_instance.read_delta(in_standardized_data_dir(alert_notes_file_name))
    item_status_history_df = spark_instance.read_delta(
        in_standardized_data_dir(item_status_file_name)
    )
    alert_statuses_df = spark_instance.read_delta(
        in_standardized_data_dir("ACM_MD_ALERT_STATUSES.delta")
    )

    # Implementation: Spark manager, Delta Converter
    # Join statuses with alerts
    item_status_history_df = (
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
        delta_target_path=in_cleansed_data_dir(item_status_file_name),
        user_metadata="Tagged the status stage",
    )
    alert_notes_stage_df = add_note_stage(alert_notes_df)
    spark_instance.safe_save_delta(
        alert_notes_stage_df,
        delta_target_path=in_cleansed_data_dir(alert_notes_file_name),
        user_metadata="Tagged the note stage",
    )


def transform_cleansed_to_application():
    alert_file_name = "ALERTS.delta"
    cleansed_alert_df = spark_instance.read_delta(in_cleansed_data_dir(alert_file_name))

    note_file_name = "ACM_ALERT_NOTES.delta"
    cleansed_note_df = spark_instance.read_delta(in_cleansed_data_dir(note_file_name))
    cleansed_note_df = cleansed_note_df.where('analyst_note_stage like "%last%"').selectExpr(
        "ALERT_ID", "note as last_note"
    )
    cleansed_alert_df = cleansed_alert_df.join(cleansed_note_df, "ALERT_ID", how="left")
    create_input(cleansed_alert_df, "data/4.application")


def get_pandas_dataframe(filename):
    return spark_instance.read_delta(filename).toPandas()


def show_files_in_directory(directory):
    for i in glob(os.path.join(directory, "*")):
        print(i)

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
from pipeline.agent_input_config import agent_input_prepended_agent_name_config
from pipeline.spark import spark_instance
from etl_pipeline.preprocessors import add_note_stage, add_status_stage
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
    merge_hit_and_aliases_displayName_sql = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
        alert_ap_wl_hit_names_df, "wl_hit_names", ["hit_displayName", "hit_aliases_displayName"],
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

    extract_wl_nric_dob("NRIC: S6959726J, DOB: 1955, Freque")

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
    # file_name = 'RCMDB.ALERTS_SAMPLE.delta'
    # Spark manager

    alert_file_name = "ALERTS.delta"
    cleansed_alert_df = spark_instance.read_delta(in_cleansed_data_dir(alert_file_name))

    note_file_name = "ACM_ALERT_NOTES.delta"
    cleansed_note_df = spark_instance.read_delta(in_cleansed_data_dir(note_file_name))

    # Note Preprocessor, Status Preprocessor
    # Join alerts with notes and stages of last analysis
    cleansed_note_df = cleansed_note_df.where('analyst_note_stage like "%last%"').selectExpr(
        "ALERT_ID", "note as last_note"
    )
    cleansed_alert_df = cleansed_alert_df.join(cleansed_note_df, "ALERT_ID", how="left")

    # Agent input creator

    # Spark manager / Agent input creator

    def spark_sql_create_agent_primary_alias_input_cols(
        df, agent_input_prepended_agent_name_config
    ):
        """Merge the customer specific columns into standardized agent primary and alias input columns.

        Input:
        { 'name_agent': {'name_agent_ap': ['record_name', 'whatever_other_name'],
                        'name_agent_ap_aliases': [],
                        'name_agent_wl': ['name_hit'],
                        'name_agent_wl_aliases': []
                        }
        }

        Output:
        Take {'name_agent_ap': ['record_name', 'whatever_other_name']} as an example. The 2 columns
        'record_name' and 'whatever_other_name' will be merged to create a new column named 'name_agent_ap'.
        """
        sql_expr_list = []

        for agent_name, config in agent_input_prepended_agent_name_config.items():
            for target_col, source_cols in config.items():

                sql_expr = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
                    df, target_col, source_cols, return_array=False
                )
                if sql_expr is not None:
                    sql_expr_list.append(sql_expr)

        return sql_expr_list

    # Agent input creator
    agent_input_raw_df = cleansed_alert_df.select(
        "*",
        *spark_sql_create_agent_primary_alias_input_cols(
            cleansed_alert_df, agent_input_prepended_agent_name_config
        ),
    )

    # x = agent_input_raw_df.toPandas()

    agent_input_refined_df = agent_input_raw_df

    # Agent input creator

    def create_agent_input_agg_col_config(agent_input_prepended_agent_name_config):
        """Create the source and target columns based on the standardized agent input config.

        Input:
        { 'name_agent': {'name_agent_ap': ['record_name', 'whatever_other_name'],
                        'name_agent_ap_aliases': [],
                        'name_agent_wl': ['name_hit'],
                        'name_agent_wl_aliases': []
                        }
        }

        Output:
        {'name_agent': {'ap_all_names_aggregated': ['name_agent_ap', 'name_agent_ap_aliases'],
                        'wl_all_names_aggregated': ['name_agent_wl', 'name_agent_wl_aliases']
                    }
        }
        """

        def _generate_simple_plural(word):
            if word.lower().endswith("s"):
                return word.lower() + "es"
            elif word.lower().endswith("y") and word.lower()[-2:] not in [
                "ay",
                "ey",
                "iy",
                "oy",
                "uy",
            ]:
                return word.lower()[:-1] + "ies"
            else:
                return word.lower() + "s"

        def _get_ap_or_wl_agg_source_cols(level_1_value, party):
            source_cols = []
            for col in level_1_value.keys():
                if col.endswith(f"_{party}") or col.endswith(f"_{party}_aliases"):
                    source_cols.append(col)

            return source_cols

        agent_input_agg_col_config = dict()

        for agent_name, config in agent_input_prepended_agent_name_config.items():
            agent_type = agent_name.split("_agent", 1)[0]

            agent_ap_agg_col = f"""ap_all_{_generate_simple_plural(agent_type)}_aggregated"""
            agent_wl_agg_col = f"""wl_all_{_generate_simple_plural(agent_type)}_aggregated"""

            agent_ap_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "ap")
            agent_wl_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "wl")

            agent_input_agg_col_config[agent_name] = dict()
            agent_input_agg_col_config[agent_name][agent_ap_agg_col] = agent_ap_agg_source_cols
            agent_input_agg_col_config[agent_name][agent_wl_agg_col] = agent_wl_agg_source_cols

        return agent_input_agg_col_config

    # DO NOTE the input to the function is the config object after prepending agent name.
    agent_input_agg_col_config = create_agent_input_agg_col_config(
        agent_input_prepended_agent_name_config
    )

    # The agent_agg_cols_config will be needed later
    with open("agent_input_agg_col_config.json", "w") as outfile:
        json.dump(agent_input_agg_col_config, outfile)

    # Spark manager, Agent input creator

    def spark_sql_create_agg_cols(df, agent_input_agg_col_config):
        sql_expr_list = []
        for agent, config in agent_input_agg_col_config.items():
            for party_agg_col, party_agg_source_cols in config.items():
                target_col = party_agg_col
                source_cols = party_agg_source_cols

                sql_expr = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
                    df, target_col, source_cols, return_array=True
                )
                if sql_expr is not None:
                    sql_expr_list.append(sql_expr)

        return sql_expr_list

    # Agent input creator
    agent_input_agg_df = agent_input_refined_df.select(
        "*", *spark_sql_create_agg_cols(agent_input_refined_df, agent_input_agg_col_config)
    ).withColumn("_index", F.monotonically_increasing_id())

    agent_input_agg_df = spark_instance.safe_save_delta(
        agent_input_agg_df, in_application_data_dir("agent_input_agg_df.delta")
    )

    agent_input_agg_df.toPandas().head()

    spark_instance.read_delta(in_application_data_dir("agent_input_agg_df.delta")).count()
    # Agent input creator
    key_cols = ["_index", "ALERT_INTERNAL_ID", "ALERT_ID", "hit_listId", "hit_entryId"]
    for agent_name, input_agg_col_config in agent_input_agg_col_config.items():
        start = time.time()

        if agent_name in ["name_agent", "dob_agent"]:
            agent_input_df = agent_input_agg_df.select(
                *key_cols,
                *input_agg_col_config.keys(),
                "party_type_agent_ap",
                "party_type_agent_wl",
            )
        elif "pary_type" in agent_name:
            continue
        else:
            agent_input_df = agent_input_agg_df.select(*key_cols, *input_agg_col_config.keys())

        # Our agent support the input list has None, hence, filter out None from all the agg columns (they will be the agent inputs)
        for agg_col_name in input_agg_col_config.keys():
            agent_input_df = agent_input_df.withColumn(
                agg_col_name, F.expr(f"array_except({agg_col_name}, array(null))")
            )

        agent_input_df_path = in_application_data_dir(f"agent-input/{agent_name}_input.delta")
        agent_input_df = spark_instance.safe_save_delta(agent_input_df, agent_input_df_path)
        logging.info(
            f"Agent: {agent_name}, Input written to {agent_input_df_path}, elapsed time: {time.time() - start:.2f}s"
        )


def get_pandas_dataframe(filename):
    return spark_instance.read_delta(filename).toPandas()


def show_files_in_directory(directory):
    for i in glob(os.path.join(directory, "*")):
        print(i)

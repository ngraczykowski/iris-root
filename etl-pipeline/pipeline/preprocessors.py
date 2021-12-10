import json
import logging
import os
import re
import copy
import time
from glob import glob
import pyspark.sql.functions as F

from pyspark.sql.types import ArrayType, MapType, StringType, StructField, StructType
from silenteight.aia.alerts import AlertHitDictFactory, AlertHitExtractor
from silenteight.data.spark import preprocess

import helper.dbhelper as dbhelper
from pipeline.config import (
    CLEANSED_DATA_DIR,
    RAW_DATA_DIR,
    STANDARDIZED_DATA_DIR,
    in_application_data_dir,
    APPLICATION_DATA_DIR,
    in_cleansed_data_dir,
    in_raw_data_dir,
    in_standardized_data_dir,
)
from pipeline.spark import spark_instance
from delta.tables import *

# IMPLEMENTATION: DeltaConverter
def convert_to_standardized(raw_data_path=RAW_DATA_DIR, target_path=STANDARDIZED_DATA_DIR):
    for file_name in os.listdir(raw_data_path):
        start = time.time()
        raw_file_path = in_raw_data_dir(file_name)
        logging.info(f"Start to process {raw_file_path}")

        standardized_file_name = re.sub(".csv$", ".delta", file_name)
        standardized_file_path = in_standardized_data_dir(standardized_file_name)
        df = spark_instance.spark_read_csv(raw_file_path)
        df = preprocess.write_read_delta(spark_instance.spark_instance, df, standardized_file_path)

        logging.info(f"Data saved to {standardized_file_path}")
        logging.info(f"Time lapsed {time.time() - start:.2f} s")

        print()
        time.sleep(1)


def convert_standardized_to_cleansed():

    # Implementation: XML Parser
    alert_hit_dict_factory = AlertHitDictFactory()
    alert_hit_extractor = AlertHitExtractor()

    # Implementation: XML Parser
    alert_schema = alert_hit_dict_factory.get_alert_spark_schema()
    hit_schema = alert_hit_dict_factory.get_hit_spark_schema()

    # Implementation: Spark manager
    schema = StructType([StructField("alert_header", alert_schema), StructField("hits", ArrayType(hit_schema))])

    # file_name = 'RCMDB.ALERTS_SAMPLE.delta'
    # Implementation: Spark manager
    file_name = "ALERTS.delta"
    std_alert_df = spark_instance.read_delta("data/2.standardized/" + (file_name))

    # Implementation: Spark manager
    spark_instance.show_dim(std_alert_df)

    # Implementation: Spark manager, XML Parser

    ## Unwrap xmls
    alert_df = std_alert_df.withColumn(
        "alert_hits",
        F.udf(alert_hit_extractor.extract_alert_hits_from_xml, schema)("html_file_key"),
    )

    # Implementation: Delta Converter

    alert_df = spark_instance.write_and_get_delta_data(alert_df, in_cleansed_data_dir(file_name))

    # Create columns from hits
    # Implementation: Spark manager, XML Parser, Match/Hit Handler
    alert_hits_df = (
        alert_df.selectExpr("*", "alert_hits.*")
        .selectExpr("*", "explode(hits) as hit")
        .selectExpr("*", "alert_header.*")
        .selectExpr("*", "hit.*")
        .drop("alert_hits", "alert_header", "hits", "hit")
    )

    # Implementation: Delta Exporter
    alert_hits_df = spark_instance.write_and_get_delta_data(
        alert_hits_df, in_cleansed_data_dir(file_name), user_metadata="At hit level"
    )

    # Implementation: XML Parser, Match/Hit Handler
    def get_wl_hit_aliases_matched_name(hit_aliases_displayName, hit_aliases_matchedName, hit_inputExplanations):
        if hit_inputExplanations is None or len(hit_inputExplanations) == 0:
            return []
        else:
            result = []
            hit_inputExplanations = list(set(hit_inputExplanations))
            for hit_inputExplanation in hit_inputExplanations:
                if hit_inputExplanation in hit_aliases_matchedName:
                    index_in_matchedName = hit_aliases_matchedName.index(hit_inputExplanation)
                    result.append(hit_aliases_displayName[index_in_matchedName])
                elif hit_inputExplanation in hit_aliases_displayName:
                    result.append(hit_inputExplanation)
                else:
                    result.append(hit_inputExplanation)

            return result

    # Implementation: Match/Hit Handler, Spark manager

    # Merge columns
    ap_hit_names_sql = spark_instance.sql_merge_to_target_col_from_source_cols(
        alert_hits_df,
        "ap_hit_names",
        "hit_inputExplanations_matchedName_inputExplanation",
        "hit_inputExplanations_aliases_matchedName_inputExplanation",
    )
    alert_ap_hit_names_df = alert_hits_df.select("*", ap_hit_names_sql)

    # Clear columns from empty hits
    alert_ap_wl_hit_names_df = alert_ap_hit_names_df.withColumn(
        "wl_hit_matched_name",
        F.when(F.expr("size(hit_explanations_matchedName_Explanation) > 0"), F.col("hit_displayName")).otherwise(
            F.lit(None)
        ),
    ).withColumn(
        "wl_hit_aliases_matched_name",
        F.udf(get_wl_hit_aliases_matched_name, ArrayType(StringType()))(
            "hit_aliases_displayName",
            "hit_aliases_matchedName",
            "hit_explanations_aliases_matchedName_Explanation",
        ),
    )
    # Merge wl hits columns
    wl_hit_names_sql = spark_instance.sql_merge_to_target_col_from_source_cols(
        alert_ap_wl_hit_names_df,
        "wl_hit_names",
        "wl_hit_matched_name",
        "wl_hit_aliases_matched_name",
    )
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.select("*", wl_hit_names_sql)

    # Implementation: Match/Hit Handler, Spark manager

    # Merge wl hits columns
    merge_hit_and_aliases_displayName_sql = spark_instance.sql_merge_to_target_col_from_source_cols(
        alert_ap_wl_hit_names_df, "wl_hit_names", "hit_displayName", "hit_aliases_displayName"
    )
    merge_ap_names_sql = spark_instance.sql_merge_to_target_col_from_source_cols(
        alert_ap_wl_hit_names_df, "ap_hit_names", "alert_ahData_partyName", return_array=True
    )

    # Clear wl and ap hits columns
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.withColumn(
        "wl_hit_names",
        F.when(F.expr("size(wl_hit_names) > 0"), F.col("wl_hit_names")).otherwise(
            merge_hit_and_aliases_displayName_sql
        ),
    ).withColumn(
        "ap_hit_names",
        F.when(F.expr("size(ap_hit_names) > 0"), F.col("ap_hit_names")).otherwise(merge_ap_names_sql),
    )

    alert_ap_wl_hit_names_df.toPandas()["ap_hit_names"]

    # Implementation: Match/Hit Handler, Spark manager
    alert_statuses_df = spark_instance.read_delta(in_standardized_data_dir("ACM_MD_ALERT_STATUSES.delta")).select(
        "STATUS_INTERNAL_ID", "STATUS_NAME"
    )
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.join(alert_statuses_df, "STATUS_INTERNAL_ID")
    alert_ap_wl_hit_names_df = spark_instance.reorder_cols(
        alert_ap_wl_hit_names_df, "STATUS_INTERNAL_ID", "STATUS_NAME"
    )
    alert_ap_wl_hit_names_df = spark_instance.write_and_get_delta_data(
        alert_ap_wl_hit_names_df,
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

    spark_instance.group_count(alert_nric_df.selectExpr("size(ap_nric) as s", "ap_nric"), "s")

    alert_nric_df.selectExpr("size(ap_nric) as s", "ap_nric").where("s = 2").limit(2).toPandas()
    alert_nric_df = spark_instance.write_and_get_delta_data(
        alert_nric_df, in_cleansed_data_dir(file_name), user_metadata="Extracted AP and WL NRIC"
    )

    # Implementation: Spark manager
    alert_notes_file_name = "ACM_ALERT_NOTES.delta"
    item_status_file_name = "ACM_ITEM_STATUS_HISTORY.delta"

    alert_notes_df = spark_instance.read_delta(in_standardized_data_dir(alert_notes_file_name))
    item_status_history_df = spark_instance.read_delta(in_standardized_data_dir(item_status_file_name))
    alert_statuses_df = spark_instance.read_delta(in_standardized_data_dir("ACM_MD_ALERT_STATUSES.delta"))

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

    item_status_history_df = spark_instance.reorder_cols(
        item_status_history_df, "FROM_STATUS_IDENTIFIER", "FROM_STATUS_NAME"
    )
    item_status_history_df = spark_instance.reorder_cols(
        item_status_history_df, "TO_STATUS_IDENTIFIER", "TO_STATUS_NAME"
    )
    item_status_history_df.createOrReplaceTempView("status_df")
    # Join statuses with alerts
    system_id = "22601"
    item_status_history_stage_df = spark_instance.spark_instance.sql(
        f"""
    with status_row_num as (
        select *,
            row_number() over (partition by item_id order by create_date asc) as row_num
        from status_df),
    first_last_analyst_row_num as (
        select ITEM_ID,
            min(row_num) as first_analyst_row_num,
            max(row_num) as last_analyst_row_num
        from status_row_num
        where user_join_id != "{system_id}"
        group by ITEM_ID
        )
    select a.*,
        b.first_analyst_row_num,
        b.last_analyst_row_num,
        case 
            when row_num = first_analyst_row_num and row_num = last_analyst_row_num then "first_last_analyst_status"
            when row_num = first_analyst_row_num then "first_analyst_status"
            when row_num = last_analyst_row_num then "last_analyst_status"
            when row_num > first_analyst_row_num then "middle_analyst_status"
            else "system_activity"
        end as analyst_status_stage
    from status_row_num a
    join first_last_analyst_row_num b
    on a.ITEM_ID = b.ITEM_ID
    """
    )
    item_status_history_stage_df = spark_instance.write_and_get_delta_data(
        item_status_history_stage_df,
        delta_path=in_cleansed_data_dir(item_status_file_name),
        user_metadata="Tagged the status stage",
    )
    alert_notes_df.createOrReplaceTempView("notes_df")

    alert_notes_stage_df = spark_instance.spark_instance.sql(
        """
        with notes_row_num as (
            select *,
                row_number() over (partition by alert_id order by create_date asc) as row_num
            from notes_df),
        first_last_analyst_row_num as (
            select *,
                min(row_num) over (partition by alert_id) as first_analyst_row_num,
                max(row_num) over (partition by alert_id) as last_analyst_row_num
            from notes_row_num)
        select *,
            case 
                when row_num = first_analyst_row_num and row_num = last_analyst_row_num then "first_last_analyst_note"
                when row_num = first_analyst_row_num then "first_analyst_note"
                when row_num = last_analyst_row_num then "last_analyst_note"
                else "middle_analyst_note"
            end as analyst_note_stage
        from first_last_analyst_row_num    
    """
    )

    alert_notes_stage_df = spark_instance.write_and_get_delta_data(
        alert_notes_stage_df,
        delta_path=in_cleansed_data_dir(alert_notes_file_name),
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

    # group_count(cleansed_alert_df, 'last_note', 5)

    cleansed_alert_df.toPandas()

    # Agent input creator

    

    input_template = {"ap": [], "ap_aliases": [], "wl": [], "wl_aliases": []}

    agent_list = [
        "party_type_agent",
        "name_agent",
        "dob_agent",
        "pob_agent",
        "gender_agent",
        "national_id_agent",
        #     'passport_agent',
        "document_number_agent",
        "nationality_agent",
        "historical_decision_name_agent",
        "pep_payment_agent",
        "hit_is_san_agent",
        "hit_is_deceased_agent",
        "hit_has_dob_id_address_agent",
        "rba_agent",
    ]

    agent_input_config = {}

    for agent in agent_list:
        new_input = copy.deepcopy(input_template)
        agent_input_config[agent] = new_input

    agent_input_config

    # Agent input creator

    # DO NOTE the fff_format use i-based index While, python list is 0-based index
    agent_input_config["party_type_agent"]["ap"].extend(["alert_partyType"])
    agent_input_config["party_type_agent"]["wl"].extend(["hit_entryType"])

    agent_input_config["name_agent"]["ap"].extend(["ap_hit_names"])
    agent_input_config["name_agent"]["wl"].extend(["wl_hit_names"])

    # agent_input_config['dob_agent']['ap'].extend(['alert_partyDOB', 'alert_partyYOB'])
    # agent_input_config['dob_agent']['wl'].extend(['hit_datesOfBirth_birthDate', 'hit_datesOfBirth_yearOfBirth'])

    # The alert_partyDOB has value of '31/12/99' which is actually '9999-12-31' from P14
    agent_input_config["dob_agent"]["ap"].extend(["P14"])
    agent_input_config["dob_agent"]["wl"].extend(["hit_datesOfBirth_birthDate", "hit_cs_1_data_points.dob"])

    agent_input_config["pob_agent"]["ap"].extend(["alert_partyBirthCountry"])
    # It's weird hit_placesOfBirth_birthPlace has country data instead of hit_placesOfBirth_birthCountry
    agent_input_config["pob_agent"]["wl"].extend(["hit_placesOfBirth_birthPlace"])

    agent_input_config["gender_agent"]["ap"].extend(["alert_partyGender"])
    agent_input_config["gender_agent"]["wl"].extend(["hit_gender"])

    # The national ID is for SG NRIC only
    agent_input_config["national_id_agent"]["ap"].extend(["ap_nric"])
    agent_input_config["national_id_agent"]["wl"].extend(["hit_cs_1_data_points.nric"])

    # agent_input_config['passport_agent']['ap'].extend(get_ap_screenable_attributes('PASSPORT'))
    # agent_input_config['passport_agent']['wl'].extend(['passport'])
    agent_input_config["document_number_agent"]["ap"].extend(["alert_partyIds_idNumber"])
    # The 'hit_cs_1_data_points.nric' is a subset of 'hit_cs_1_data_points.possible_nric'. Hence, use 'hit_cs_1_data_points.possible_nric' only
    agent_input_config["document_number_agent"]["wl"].extend(["hit_ids_idNumber", "hit_cs_1_data_points.possible_nric"])

    # "alert_partyNatCountries_countryCd" is alwasy empty, "alert_partyNatCountries_countryCd" is actually part of address
    # but it's used by screening engine and analyst to match with WL nationality country
    agent_input_config["nationality_agent"]["ap"].extend(
        ["alert_partyNatCountries_countryCd", "alert_partyAddresses_partyCountry"]
    )
    agent_input_config["nationality_agent"]["wl"].append("hit_nationalityCountries_country")

    agent_input_config["historical_decision_name_agent"]["ap"].extend(
        ["alert_ahData_numberOfHits", "STATUS_NAME", "alert_ahData_partyName", "ALERT_DATE"]
    )
    agent_input_config["historical_decision_name_agent"]["wl"].extend(["hit_entryId"])

    # agent_input_config['historical_decision_entity_key_agent']['ap'].extend(['alert_ahData_numberOfHits', 'STATUS_NAME', 'alert_alertEntityKey', 'ALERT_DATE'])
    # agent_input_config['historical_decision_entity_key_agent']['wl'].extend(['hit_entryId'])

    agent_input_config["pep_payment_agent"]["ap"].extend(["P12"])
    agent_input_config["pep_payment_agent"]["wl"].extend(["hit_listID"])

    # The P36 and P38 are at alert level, use `hit_listId` and `hit_categories_category` which is at hit level
    agent_input_config["hit_is_san_agent"]["wl"].extend(["hit_listId", "hit_categories_category"])

    agent_input_config["hit_is_deceased_agent"]["wl"].extend(["hit_isDeceased"])

    agent_input_config["hit_has_dob_id_address_agent"]["wl"].extend(
        [
            "hit_datesOfBirth_birthDate",
            "hit_cs_1_data_points.dob",
            "hit_ids_idNumber",
            "hit_cs_1_data_points.possible_nric",
            "hit_addresses_streetAddress1",
        ]
    )

    agent_input_config["rba_agent"]["ap"].extend(
        [
            "alert_ahData_numberOfHits",
            "STATUS_NAME",
            "alert_ahData_partyName",
            "ALERT_DATE",
            "last_note",
        ]
    )
    agent_input_config["rba_agent"]["wl"].extend(["hit_entryId"])

    # Agent input creator

    def prepend_agent_name_to_ap_or_wl_or_aliases_key(agent_input_config):
        """Prepend the agent name (level 1 key) to level 2 key. So the new level 2 key will be

        Input:
        { 'name_agent': {'ap': ['record_name'],
                        'ap_aliases': [],
                        'wl': ['name_hit'],
                        'wl_aliases': []
                        }
        }

        Output:
        { 'name_agent': {'name_agent_ap': ['record_name'],
                        'name_agent_ap_aliases': [],
                        'name_agent_wl': ['name_hit'],
                        'name_agent_wl_aliases': []
                        }
        }
        """
        result = dict()
        for agent_name, config in agent_input_config.items():
            result[agent_name] = dict()

            for ap_or_wl_or_aliases, source_cols in config.items():
                prepended_key_name = "_".join([agent_name, ap_or_wl_or_aliases])
                result[agent_name][prepended_key_name] = source_cols

        return result

    # Sanity check
    agent_input_prepended_agent_name_config = prepend_agent_name_to_ap_or_wl_or_aliases_key(agent_input_config)
    agent_input_prepended_agent_name_config

    x = cleansed_alert_df.toPandas()

    # Spark manager / Agent input creator

    def spark_sql_create_agent_primary_alias_input_cols(df, agent_input_prepended_agent_name_config):
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

                sql_expr = spark_instance.sql_merge_to_target_col_from_source_cols(df, target_col, *source_cols)
                if sql_expr is not None:
                    sql_expr_list.append(sql_expr)

        return sql_expr_list

    # Agent input creator
    agent_input_raw_df = cleansed_alert_df.select(
        "*",
        *spark_sql_create_agent_primary_alias_input_cols(cleansed_alert_df, agent_input_prepended_agent_name_config),
    )

    x = agent_input_raw_df.toPandas()

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
    agent_input_agg_col_config = create_agent_input_agg_col_config(agent_input_prepended_agent_name_config)
    agent_input_agg_col_config

    # Agent input creator

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

                sql_expr = spark_instance.sql_merge_to_target_col_from_source_cols(
                    df, target_col, *source_cols, return_array=True
                )
                if sql_expr is not None:
                    sql_expr_list.append(sql_expr)

        return sql_expr_list

    # Agent input creator
    agent_input_agg_df = agent_input_refined_df.select(
        "*", *spark_sql_create_agg_cols(agent_input_refined_df, agent_input_agg_col_config)
    ).withColumn("_index", F.monotonically_increasing_id())

    agent_input_agg_df = spark_instance.write_and_get_delta_data(
        agent_input_agg_df, in_application_data_dir("agent_input_agg_df.delta")
    )

    agent_input_agg_df.toPandas().head()

    spark_instance.read_delta(in_application_data_dir("agent_input_agg_df.delta")).count()

    spark_instance.to_pandas(agent_input_agg_df)
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
        agent_input_df = spark_instance.write_and_get_delta_data(agent_input_df, agent_input_df_path)
        logging.info(
            f"Agent: {agent}, Input written to {agent_input_df_path}, elapsed time: {time.time() - start:.2f}s"
        )


def get_pandas_dataframe(filename):
    return spark_instance.read_delta(filename).toPandas()

def show_files_in_directory(directory):
    for i in glob(os.path.join(directory, "*")):
        print(i)
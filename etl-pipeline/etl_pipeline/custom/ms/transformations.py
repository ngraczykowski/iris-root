import copy
import re
from typing import List

import pyspark
import pyspark.sql.functions as F
from pyspark.sql.types import (
    ArrayType,
    MapType,
    StringType,
    StructField,
    StructType,
    TimestampType,
)
from serppythonclient.yml import YamlLoader

from config import columns_namespace
from etl_pipeline.custom.ms.config import CONFIG_PATH
from etl_pipeline.custom.ms.datatypes.status.sanctions import SanctionsStatus
from etl_pipeline.pattern import AccountType

with open(CONFIG_PATH) as conf:
    loaded = YamlLoader.get_instance().load(conf)
    loaded = loaded["PIPELINE"]

    TEMP_DIR_PATH = loaded["temp"]

    INPUT_RECORD_COLUMNS = loaded["input-record-columns"]
    ID_COLUMN_NAME = loaded["id-column-name"]
    DS_TYPE = loaded["ds-type"]
    WM_ACCOUNTS_IN_SCOPE_INPUT_PATH = loaded["wm-accounts-in-scope-input-path"]
    DF360_INPUT_PATH = loaded["df360-input-path"]
    DF360_OUTPUT_PATH = loaded["df360-output-path"]

    FUZZINESS = loaded["fuzziness"]
    PARTY_AND_ADDRESS_RELATIONSHIP_INPUT_PATH = loaded["party-and-address-relationship-input-path"]
    WM_PARTIES_IN_SCOPE_INPUT_PATH = loaded["wm-parties-in-scope-input-path"]

    DECISION_TREE = loaded["decision-tree"]
    ALERTS = loaded["alerts"]
    ALERT_TYPE = loaded["type"]


def detect_type(column: str):
    return TimestampType() if column.endswith("datetime") else StringType()


def columns_to_schema(cols: List[str]) -> StructType:
    fields = []
    for column in cols:
        field_type = detect_type(column)
        fields.append(StructField(column, field_type))

    return StructType(fields)


def enrich_with_date_column(df, datetime_column, date_column):
    from pyspark.sql.types import DateType

    df = df.withColumn(date_column, F.col(datetime_column).cast(DateType()))
    cols = df.columns_namespace.copy()
    columns_namespace.insert(cols.index(datetime_column) + 1, cols.pop(cols.index(date_column)))
    return df.select(cols)


def custom_register_alerts_data_table(alerts_df):
    cols = [
        columns_namespace.ALERT_INTERNAL_ID,
        columns_namespace.ALERT_ID,
        columns_namespace.SRC_REF_KEY,
        ID_COLUMN_NAME,
        "BUNIT_IDENTIFIER",
        "STATUS_DESC",
        "ALERT_STATE",
    ]

    df = alerts_df.select(cols)
    df.registerTempTable("alerts_data")
    return df


def register_alerts_xml_table(df: pyspark.sql.dataframe):
    cols = [
        columns_namespace.ALERT_ID,
        "CURRENT_VERSION_ID",
        "INPUT_RECORD_HIST",
        "MATCH_RECORDS",
    ]

    df = df.select(cols)
    df.registerTempTable("alerts_xml_data")
    return df


@F.udf(returnType=MapType(StringType(), StringType()))
def filter_record(input_record_hist, version_id):
    for el in input_record_hist:
        if el["VERSION_ID"] == version_id:
            return el
    return {}


@F.udf()
def determine_ap_type(entiti_type):
    if entiti_type in ["05", "06", "08", "09"]:
        return "C"
    return "I"


@F.udf()
def determine_analyst_decision(status):
    return SanctionsStatus(status).to_analyst_solution().value


def create_sanctions_360_df(df_alerts_notes, spark_engine):

    df = spark_engine.spark_instance.sql(
        rf"""
        SELECT
            ad.ALERT_INTERNAL_ID,
            ad.ALERT_ID,
            ad.{ID_COLUMN_NAME},
            ad.STATUS_DESC,
            ad.SRC_REF_KEY,
            axd.INPUT_RECORD_HIST,
            axd.MATCH_RECORDS,
            axd.CURRENT_VERSION_ID
        FROM alerts_data ad
        LEFT JOIN alerts_xml_data axd ON axd.ALERT_ID = ad.ALERT_ID
    """
    )

    if df_alerts_notes:
        df_alerts_notes_filtered = df_alerts_notes.select(
            columns_namespace.ALERT_INTERNAL_ID,
            "LAST_USER_NOTE_TEXT",
            "USER_NOTE_TEXT",
            F.col("CREATE_DATE").alias("USER_NOTE_CREATE_DATE"),
        )
        df = df.join(df_alerts_notes_filtered, columns_namespace.ALERT_INTERNAL_ID, "left")
    else:
        df = (
            df.withColumn("LAST_USER_NOTE_TEXT", F.lit(None).cast(StringType()))
            .withColumn("USER_NOTE_TEXT", F.lit(None).cast(StringType()))
            .withColumn("USER_NOTE_CREATE_DATE", F.lit(None).cast(StringType()))
        )

    df = df.withColumn("MATCH_RECORD", F.explode("MATCH_RECORDS")).drop("MATCH_RECORDS")
    df = df.withColumn("DS_TYPE", F.lit(DS_TYPE))
    return df


def join_transformation(df_sanctions_360):
    df360 = df_sanctions_360.withColumn(
        "INPUT_RECORD", filter_record("INPUT_RECORD_HIST", F.col("MATCH_RECORD").VERSION_ID)
    )

    for column in INPUT_RECORD_COLUMNS:
        df360 = df360.withColumn(column, getattr(F.col("INPUT_RECORD"), column))

    # Columns common for all types
    df360 = (
        df360.withColumn(columns_namespace.WL_NAME, F.col("MATCH_RECORD").WL_NAME)
        .withColumn(columns_namespace.WL_STATENAME, F.col("MATCH_RECORD").WL_STATENAME)
        .withColumn(columns_namespace.WL_ADDRESS1, F.col("MATCH_RECORD").WL_ADDRESS1)
        .withColumn(columns_namespace.WL_ENTITYTYPE, F.col("MATCH_RECORD").WL_ENTITYTYPE)
        .withColumn(columns_namespace.WL_STATE, F.col("MATCH_RECORD").WL_STATE)
        .withColumn(columns_namespace.WL_COUNTRY, F.col("MATCH_RECORD").WL_COUNTRY)
        .withColumn(columns_namespace.WL_POSTALCODE, F.col("MATCH_RECORD").WL_POSTALCODE)
        .withColumn(columns_namespace.WL_COUNTRYNAME, F.col("MATCH_RECORD").WL_COUNTRYNAME)
        .withColumn(columns_namespace.WL_CITY, F.col("MATCH_RECORD").WL_CITY)
        .withColumn(columns_namespace.WL_MATCHED_TOKENS, F.col("MATCH_RECORD").WL_MATCHED_TOKENS)
        .withColumn(columns_namespace.WL_DOB, F.col("MATCH_RECORD").WL_DOB)
        .withColumn(columns_namespace.WL_NATIONALITY, F.col("MATCH_RECORD").WL_NATIONALITY)
        .withColumn(columns_namespace.WL_POB, F.col("MATCH_RECORD").WL_POB)
        .withColumn(columns_namespace.WL_CITIZENSHIP, F.col("MATCH_RECORD").WL_CITIZENSHIP)
        .withColumn(columns_namespace.WL_ALIASES, F.col("MATCH_RECORD").WL_ALIASES)
        .withColumn(
            columns_namespace.WL_ROUTING_CODE_BIC, F.col("MATCH_RECORD").WL_ROUTING_CODE_BIC
        )
        .withColumn(
            columns_namespace.WL_ROUTING_CODE_CHIPS_UID,
            F.col("MATCH_RECORD").WL_ROUTING_CODE_CHIPS_UID,
        )
        .withColumn(
            columns_namespace.WL_ROUTING_CODE_NATL_ROUTING_CODE,
            F.col("MATCH_RECORD").WL_ROUTING_CODE_Natl_Routing_Code,
        )
        .withColumn(
            columns_namespace.WL_ROUTING_CODE_SWIFT, F.col("MATCH_RECORD").WL_ROUTING_CODE_SWIFT
        )
        .withColumn(columns_namespace.MATCH_INPUT_VERSION_ID, F.col("MATCH_RECORD").VERSION_ID)
        .withColumn(columns_namespace.ENTITY_ID, F.col("MATCH_RECORD").ENTITY_ID)
        .withColumn(columns_namespace.ENTITY_VERSION, F.col("MATCH_RECORD").ENTITY_VERSION)
        .withColumn(columns_namespace.AP_TYPE, determine_ap_type("WL_ENTITYTYPE"))
        .withColumn(columns_namespace.ANALYST_DECISION, determine_analyst_decision("STATUS_DESC"))
    )
    return df360


candidates_for_conversion = []


@F.udf()
def custom_determine_wl_type(entity_type):
    if entity_type in ["05", "06", "08", "09"]:
        return "C"
    return "I"


def custom_derive_party_type_from_acct_class_descr(descr):
    individual_indicators = ["INDIVIDUAL"]

    if descr is not None:
        if any(d in descr for d in individual_indicators):
            return "I"
        elif descr is not None:
            return "C"
    else:
        return None


@F.udf
def custom_udf_derive_party_type_from_acct_class_descr(descr):
    return custom_derive_party_type_from_acct_class_descr(descr)


@F.udf(returnType=StringType())
def custom_udf_to_analyst_solution(status_desc):
    return custom_to_analyst_solution(status_desc)


def custom_to_analyst_solution(status_desc):
    if any(
        f in status_desc
        for f in ["Risk Accepted", "Case Created", "L3 Closed No Action - False Positive"]
    ):
        return "TP"
    elif any(
        f in status_desc
        for f in [
            "False Positive",
            " Could have been closed L1",
            "L1 Closed",
            "L3 Closed No Action - Could have been closed L2",
        ]
    ):
        return "FP"

    return status_desc


def create_agent_input(ALERT_TYPE):
    input_template = {"ap": [], "wl": []}

    agent_list = [
        "party_type_agent",
        "is_deny_agent",
        "name_agent",
        "dob_agent",
        "national_id_agent",
        "passport_agent",
        "document_agent",
        "gender_agent",
        "nationality_agent",
        "residency_agent",
    ]

    agent_input_config = {}

    for agent in agent_list:
        new_input = copy.deepcopy(input_template)
        agent_input_config[agent] = new_input

    agent_input_config["name_agent"]["ap_aliases"] = []
    agent_input_config["name_agent"]["wl_aliases"] = []

    agent_input_config["party_type_agent"]["ap"].extend(["AP_TYPE"])
    if ALERT_TYPE == AccountType.ISG_ACCOUNT:
        agent_input_config["name_agent"]["ap"].extend(
            ["FRST_NM", "LAST_NM", "PRIN_OWN_NM", "ORD_PLACR_NM", "PARTY1_NAME_ALIAS1"]
        )
        agent_input_config["name_agent"]["wl"].extend(["WL_NAME", columns_namespace.WL_ALIASES])
        agent_input_config["dob_agent"]["wl"].extend([columns_namespace.WL_DOB])

        agent_input_config["national_id_agent"]["ap"].extend(["ap_nric"])
        agent_input_config["national_id_agent"]["wl"].extend(["hit_cs_1_data_points.nric"])

        agent_input_config["document_agent"]["ap"].extend(["alert_partyIds_idNumber"])
        agent_input_config["document_agent"]["wl"].extend(
            ["hit_ids_idNumber", "hit_cs_1_data_points.possible_nric"]
        )

        agent_input_config["nationality_agent"]["ap"].extend(
            [
                "PARTY1_COUNTRY_CITIZENSHIP1",
                "PARTY1_COUNTRY_DOMICILE1",
                "COUNTRYCODE",
                "PARTY1_ADDRESS1_COUNTRY",
            ]
        )
        agent_input_config["nationality_agent"]["wl"].extend(
            [
                columns_namespace.WL_NATIONALITY,
                columns_namespace.WL_POB,
                columns_namespace.WL_CITIZENSHIP,
                columns_namespace.WL_COUNTRY,
                columns_namespace.WL_COUNTRYNAME,
            ]
        )

        agent_input_config["residency_agent"]["ap"].extend(["PARTY1_COUNTRY_DOMICILE1"])
        agent_input_config["residency_agent"]["wl"].extend([columns_namespace.WL_COUNTRY])

    if ALERT_TYPE == AccountType.WM_ADDRESS:
        agent_input_config["name_agent"]["ap"].extend(
            ["FRST_NM", "LAST_NM", columns_namespace.ALL_PARTY_NAMES]
        )
        agent_input_config["name_agent"]["wl"].extend(["WL_NAME", columns_namespace.WL_ALIASES])

        agent_input_config["dob_agent"]["ap"].extend([columns_namespace.ALL_PARTY_DOBS])
        agent_input_config["dob_agent"]["wl"].extend([columns_namespace.WL_DOB])

        agent_input_config["national_id_agent"]["ap"].extend(["ap_nric"])
        agent_input_config["national_id_agent"]["wl"].extend(["hit_cs_1_data_points.nric"])

        agent_input_config["document_agent"]["ap"].extend(["alert_partyIds_idNumber"])
        agent_input_config["document_agent"]["wl"].extend(
            ["hit_ids_idNumber", "hit_cs_1_data_points.possible_nric"]
        )

        agent_input_config["nationality_agent"]["wl"].extend(
            [
                columns_namespace.WL_NATIONALITY,
                columns_namespace.WL_POB,
                columns_namespace.WL_CITIZENSHIP,
                columns_namespace.WL_COUNTRY,
                columns_namespace.WL_COUNTRYNAME,
            ]
        )

        agent_input_config["residency_agent"]["ap"].extend([columns_namespace.ADDRESS1_COUNTRY])
        agent_input_config["residency_agent"]["wl"].extend([columns_namespace.WL_COUNTRY])
    return agent_input_config


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
    result = {}
    for agent_name, config in agent_input_config.items():
        result[agent_name] = {}

        for ap_or_wl_or_aliases, source_cols in config.items():
            prepended_key_name = "_".join([agent_name, ap_or_wl_or_aliases])
            result[agent_name][prepended_key_name] = source_cols

    return result


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

    agent_input_agg_col_config = {}

    for agent_name, config in agent_input_prepended_agent_name_config.items():
        agent_type = agent_name.split("_agent", 1)[0]
        agent_ap_agg_col = f"""ap_all_{_generate_simple_plural(agent_type)}_aggregated"""
        agent_wl_agg_col = f"""wl_all_{_generate_simple_plural(agent_type)}_aggregated"""

        agent_ap_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "ap")
        agent_wl_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "wl")

        agent_input_agg_col_config[agent_name] = {}
        agent_input_agg_col_config[agent_name][agent_ap_agg_col] = agent_ap_agg_source_cols
        agent_input_agg_col_config[agent_name][agent_wl_agg_col] = agent_wl_agg_source_cols

    return agent_input_agg_col_config


def select_wl_names(ap_triggers, wl_all_names_aggregated, wl_name):

    tokens = list(ap_triggers.keys())

    names = []

    for token in tokens:
        selected_names = [
            name.replace('["', "").replace('"]', "")
            for name in wl_all_names_aggregated
            if token in name.replace(" ", "")
        ]

        for name in selected_names:
            names.append(name)

    names.append(wl_name)

    return list(set(names))


@F.udf(returnType=ArrayType(StringType()))
def udf_select_wl_names(ap_triggers, wl_all_names_aggregated, wl_name):
    return select_wl_names(ap_triggers, wl_all_names_aggregated, wl_name)


def split_ap_names_by_ms_delimiter(concatenated_values):
    """
    for name agent alone ease a bit processing of alerted parties (as long as we don't have FNs)
    """
    names = []

    for value in concatenated_values:
        if value:
            # each time it should take at most names avoiding address
            names = names + re.split(" A/C | ATTN | C/O | ON BHLF OF | U/A | - ", value)[:3]

    return names


@F.udf(returnType=ArrayType(StringType()))
def udf_split_ap_names_by_ms_delimiter(concatenated_values):
    if concatenated_values:
        return split_ap_names_by_ms_delimiter(concatenated_values)
    else:
        return []


def select_ap_names(d):
    names = []
    for v in d.values():
        for k in v.keys():
            if k not in [
                "LAST_USER_NOTE_TEXT",
                columns_namespace.CONCAT_ADDRESS,
            ]:  # no need to find hits in analyst comment      #"CONCAT_NAME",
                value = v[k]

                if isinstance(value, list):
                    names = names + value
                else:
                    names.append(value)

    return list(set(names))


@F.udf(returnType=ArrayType(StringType()))
def udf_select_ap_names(ap_triggers):
    return select_ap_names(ap_triggers)

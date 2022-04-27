import logging
import os
from typing import Dict, List

import pyspark.sql
import pyspark.sql.functions as F

from etl_pipeline.config import pipeline_config
from etl_pipeline.xml_spark.agent_input_creator.config import (
    AGENT_INPUT_AGG_COL_CONFIG,
    AGENT_INPUT_CONFIG,
)
from etl_pipeline.xml_spark.agent_input_creator.sql import (
    sql_to_merge_specific_columns_to_standardized,
)

cn = pipeline_config.cn


def create_input_for_agents(
    cleansed_alert_df: pyspark.sql.DataFrame, destination: str, spark_instance
):
    """Parameters
    ----------
    cleansed_alert_df : pyspark.sql.DataFrame
    destination : str
    """
    sql_expression = sql_to_merge_specific_columns_to_standardized(
        cleansed_alert_df, AGENT_INPUT_CONFIG, spark_instance=spark_instance
    )
    agent_input_refined_df = cleansed_alert_df.select("*", *sql_expression)

    sql_expression = sql_to_merge_specific_columns_to_standardized(
        agent_input_refined_df,
        AGENT_INPUT_AGG_COL_CONFIG,
        aggregated=True,
        spark_instance=spark_instance,
    )
    agent_input_agg_df = agent_input_refined_df.select("*", *sql_expression).withColumn(
        "_index", F.monotonically_increasing_id()
    )
    agent_input_agg_df = spark_instance.safe_save_delta(
        agent_input_agg_df, os.path.join(destination, "agent_input_agg_df.delta")
    )

    _extract_and_save_agent_inputs(agent_input_agg_df, destination, spark_instance=spark_instance)


def _extract_and_save_agent_inputs(
    agent_input_agg_df: pyspark.sql.DataFrame, destination: str, spark_instance
):
    """Parameters
    ----------
    agent_input_agg_df : pyspark.sql.DataFrame
    destination : str
    """
    key_cols = [
        "_index",
        cn.ALERT_INTERNAL_ID,
        cn.ALERT_ID,
        "hit_listId",
        "hit_entryId",
    ]
    os.makedirs(os.path.join(destination, "agent-input"), exist_ok=True)
    for agent_name, input_agg_col_config in AGENT_INPUT_AGG_COL_CONFIG.items():

        if agent_name in ["name_agent", "dob_agent"]:
            agent_input_df = agent_input_agg_df.select(
                *key_cols,
                *input_agg_col_config.keys(),
                "party_type_agent_ap",
                "party_type_agent_wl",
            )
        elif "pary_type" in agent_name:  # todo check if it's not a misspelling
            continue
        else:
            agent_input_df = agent_input_agg_df.select(*key_cols, *input_agg_col_config.keys())

        _save_agent_input_to_delta_file(
            agent_input_df,
            agent_name,
            input_agg_col_config,
            destination,
            spark_instance=spark_instance,
        )


def _save_agent_input_to_delta_file(
    agent_input_df: pyspark.sql.DataFrame,
    agent_name: str,
    input_agg_col_config: Dict[str, List[str]],
    destination: str,
    spark_instance,
):
    """Our agent support the input list has None, hence, filter out None from all the agg columns
    (they will be the agent inputs)

    Parameters
    ----------
    agent_input_df : pyspark.sql.DataFrame
    agent_name : str
    input_agg_col_config : Dict[str, List[str]]
    destination : str
    """
    for agg_col_name in input_agg_col_config.keys():
        agent_input_df = agent_input_df.withColumn(
            agg_col_name, F.expr(f"array_except({agg_col_name}, array(null))")
        )
    agent_input_df_path = os.path.join(destination, "agent-input", f"{agent_name}_input.delta")
    spark_instance.safe_save_delta(agent_input_df, agent_input_df_path)
    logging.info(f"Agent: {agent_name}, Input written to {agent_input_df_path}")

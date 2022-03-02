from typing import Dict, List

import pyspark.sql


def sql_to_merge_specific_columns_to_standardized(
    df: pyspark.sql.DataFrame,
    agent_input_prepended_agent_name_config: Dict[str, Dict[str, List[str]]],
    spark_instance,
    aggregated: bool = False,
) -> List[str]:
    """Merge the customer specific columns into
    standardized agent primary and alias input columns_namespace.

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

    Parameters
    ----------
    df : pyspark.sql.DataFrame
    agent_input_prepended_agent_name_config : Dict[str, Dict[str, List[str]]]
    Returns
    aggregated : bool, optional
    -------
    List[str]
    """

    sql_expr_list = []

    for _, config in agent_input_prepended_agent_name_config.items():
        for target_col, source_cols in config.items():
            if aggregated:
                sql_expr = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
                    df, target_col, source_cols, return_array=True
                )
            else:
                sql_expr = spark_instance.merge_to_target_col_from_source_cols_sql_expression(
                    df, target_col, source_cols, return_array=False
                )
            if sql_expr is not None:
                sql_expr_list.append(sql_expr)

    return sql_expr_list

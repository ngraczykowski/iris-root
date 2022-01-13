import pyspark.sql

from pipeline.spark import spark_instance


def add_status_stage(
    item_status_history: pyspark.sql.DataFrame, system_id: str = "22601"
) -> pyspark.sql.DataFrame:
    necessary_columns = ["ITEM_ID", "CREATE_DATE", "USER_JOIN_ID"]
    if not all(
        spark_instance.column_exists(item_status_history, column) for column in necessary_columns
    ):
        raise ValueError(
            f"item_status_history data frame must contain {necessary_columns} columns"
        )
    item_status_history.createOrReplaceTempView("status_df")
    item_status_history_stage = spark_instance.spark_instance.sql(
        f"""
        with status_row_num as (
            select *,
            row_number() over (partition by ITEM_ID order by CREATE_DATE asc) as row_num
            from status_df),
        first_last_analyst_row_num as (
            select ITEM_ID,
            min(row_num) as first_analyst_row_num,
            max(row_num) as last_analyst_row_num
            from status_row_num
            where USER_JOIN_ID != "{system_id}"
            group by ITEM_ID
            )
        select a.*,
            b.first_analyst_row_num,
            b.last_analyst_row_num,
            case
                when row_num = first_analyst_row_num
                    and row_num = last_analyst_row_num
                    then "first_last_analyst_status"
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
    return item_status_history_stage

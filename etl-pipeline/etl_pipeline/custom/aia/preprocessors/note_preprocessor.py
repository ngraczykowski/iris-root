import pyspark.sql

from etl_pipeline.config import pipeline_config

cn = pipeline_config.cn


def add_note_stage(alert_notes: pyspark.sql.DataFrame, spark_instance) -> pyspark.sql.DataFrame:
    necessary_columns = [cn.ALERT_ID, "CREATE_DATE"]
    if not all(spark_instance.column_exists(alert_notes, column) for column in necessary_columns):
        raise ValueError(
            f"item_status_history data frame must contain {necessary_columns} columns"
        )
    alert_notes.createOrReplaceTempView("notes_df")
    alert_notes_stage = spark_instance.spark_instance.sql(
        """
        with notes_row_num as (
            select *,
            row_number() over (partition by ALERT_ID order by CREATE_DATE asc) as row_num
            from notes_df),
        first_last_analyst_row_num as (
            select *,
            min(row_num) over (partition by ALERT_ID) as first_analyst_row_num,
            max(row_num) over (partition by ALERT_ID) as last_analyst_row_num
            from notes_row_num)
        select *,
            case
                when row_num = first_analyst_row_num
                    and row_num = last_analyst_row_num
                    then "first_last_analyst_note"
                when row_num = first_analyst_row_num then "first_analyst_note"
                when row_num = last_analyst_row_num then "last_analyst_note"
                else "middle_analyst_note"
            end as analyst_note_stage
        from first_last_analyst_row_num
    """
    )
    return alert_notes_stage

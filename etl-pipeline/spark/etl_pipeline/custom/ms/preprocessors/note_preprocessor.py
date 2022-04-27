import pyspark.sql.functions as F

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.config import JOINING_SEP

cn = pipeline_config.cn


@F.udf()
def get_last_comment(comments):
    return comments.split(JOINING_SEP)[-1]


def register_alerts_notes_table(df):
    cols = [
        cn.ALERT_INTERNAL_ID,
        "USER_NOTE_TEXT",
        "CREATE_DATE",
        # 'BUS_DT'
    ]

    df = df.select(cols)
    df = df.orderBy([cols.ALERT_INTERNAL_ID, "CREATE_DATE"], ascending=[1, 1])
    df = df.groupby(cols.ALERT_INTERNAL_ID).agg(
        F.concat_ws(JOINING_SEP, F.collect_list(df.USER_NOTE_TEXT)).alias("USER_NOTE_TEXT"),
        F.max(df.CREATE_DATE).alias("CREATE_DATE"),
    )
    df = df.withColumn("LAST_USER_NOTE_TEXT", get_last_comment("USER_NOTE_TEXT"))
    df.registerTempTable("alerts_notes")
    return df

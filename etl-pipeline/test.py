import hashlib
from glob import glob
import os

from pipeline.spark import spark
REFERENCE_DIR = 'tests/data'
from delta.tables import *
import helper.dbhelper as dbhelper

oracle_db = dbhelper.DbHelper(spark, 'ORACLE')
pg_db = dbhelper.DbHelper(spark, 'POSTGRES')
ids_map = {
    "ALERTS.delta": "ALERT_ID",
    "ACM_ALERT_NOTES.delta": "ALERT_ID",
    "ACM_ITEM_STATUS_HISTORY.delta": "ITEM_ID",
    "agent_input_agg_df.delta": "ALERT_ID",
    "ACM_MD_ALERT_STATUSES.delta": "E_ISSUE",
}

for dir in os.listdir(REFERENCE_DIR):
    for reference in glob(f"tests/data/{dir}/*delta"):
        
        rel_path = os.path.relpath(os.path.dirname(reference), 'tests')
        reference_dataframe = spark.read.format('delta').load(reference)
        tested_dataframe = spark.read.format('delta').load(reference)
        print(reference)
        id = ids_map[os.path.basename(reference)]
        reference_rows = reference_dataframe.sort(id, ascending=False).collect()
        tested_rows = tested_dataframe.sort(id, ascending=False).collect()
        for tested_row, reference_row in zip(tested_rows, reference_rows):
            assert tested_row == reference_row

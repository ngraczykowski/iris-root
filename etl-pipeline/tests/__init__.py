import hashlib
import os
from glob import glob

from pipeline.spark import get_spark

spark = get_spark()
from pipeline.delta_initializer import *

REFERENCE_DIR = "tests/data"

ids_map = {
    "ALERTS.delta": "ALERT_ID",
    "ACM_ALERT_NOTES.delta": "ALERT_ID",
    "ACM_ITEM_STATUS_HISTORY.delta": "ITEM_ID",
    "agent_input_agg_df.delta": "ALERT_ID",
    "ACM_MD_ALERT_STATUSES.delta": "E_ISSUE",
}

for dir in os.listdir(REFERENCE_DIR):
    for reference in glob(f"tests/data/{dir}/*delta"):
        from delta.tables import *

        rel_path = os.path.relpath(os.path.dirname(reference), "tests")
        reference_dataframe = spark.read.format("delta").load(reference)
        tested_dataframe = spark.read.format("delta").load(reference)
        print(reference)
        id = ids_map[os.path.basename(reference)]
        reference_rows = reference_dataframe.sort(id, ascending=False).collect()
        tested_rows = tested_dataframe.sort(id, ascending=False).collect()
        for tested_row, reference_row in zip(tested_rows, reference_rows):
            assert tested_row == reference_row

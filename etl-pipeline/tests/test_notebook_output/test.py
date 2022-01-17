import os
import shutil
from glob import glob

from spark_manager.config.config import RAW_DATA_DIR, STANDARDIZED_DATA_DIR

from pipeline.preprocessors import (
    convert_raw_to_standardized,
    transform_cleansed_to_application,
    transform_standardized_to_cleansed,
)
from pipeline.spark import spark_instance as spark
from tests.utils import compare_dataframe

REFERENCE_DIR = "tests/data"


ID_MAP = {
    "ALERTS.delta": "ALERT_ID",
    "ACM_ALERT_NOTES.delta": "ALERT_ID",
    "ACM_ITEM_STATUS_HISTORY.delta": "ITEM_ID",
    "agent_input_agg_df.delta": "ALERT_ID",
    "ACM_MD_ALERT_STATUSES.delta": "E_ISSUE",
}


def clean_up():
    try:
        shutil.rmtree("data/2.standardized/")
        shutil.rmtree("data/3.cleansed/")
        shutil.rmtree("data/4.application/")
    except FileNotFoundError:
        pass


def compare_created_data_with_reference_data(reference, unique_column="ALERT_INTERNAL_ID"):
    rel_path = os.path.relpath(os.path.dirname(reference), "tests")
    reference_dataframe = spark.read_delta(reference)
    if not os.path.exists(os.path.join(rel_path, os.path.basename(reference))):
        raise FileNotFoundError(os.path.join(rel_path, os.path.basename(reference)))
    tested_dataframe = spark.read_delta(os.path.join(rel_path, os.path.basename(reference)))
    compare_dataframe(tested_dataframe, reference_dataframe, unique_column)


def test_integration():
    clean_up()
    convert_raw_to_standardized(RAW_DATA_DIR, STANDARDIZED_DATA_DIR)
    transform_standardized_to_cleansed()
    transform_cleansed_to_application()
    for directory in os.listdir(REFERENCE_DIR):
        if "4.application" in directory:
            continue
        for reference in glob(os.path.join(f"{REFERENCE_DIR}", f"{directory}", "*delta")):
            compare_created_data_with_reference_data(
                reference, unique_column=ID_MAP[os.path.basename(reference)]
            )
    for reference in glob(os.path.join(f"{REFERENCE_DIR}", "4.application/agent-input/*delta")):
        compare_created_data_with_reference_data(reference)
    clean_up()

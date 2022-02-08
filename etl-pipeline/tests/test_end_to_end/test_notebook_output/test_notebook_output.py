import os
import shutil
from glob import glob

from custom.aia.config import (
    APPLICATION_DATA_DIR,
    CLEANSED_DATA_DIR,
    RAW_DATA_DIR,
    STANDARDIZED_DATA_DIR,
)
from etl_pipeline.data_processor_engine.spark import spark_engine
from etl_pipeline.data_processor_engine.spark import spark_instance as spark
from main_pipeline import AIAPipeline
from tests.shared import TEST_SHARED_DATA_REFERENCE_DIR
from tests.utils import compare_dataframe

REFERENCE_DIR = TEST_SHARED_DATA_REFERENCE_DIR


ID_MAP = {
    "ALERTS.delta": "ALERT_ID",
    "ACM_ALERT_NOTES.delta": "ALERT_ID",
    "ACM_ITEM_STATUS_HISTORY.delta": "ITEM_ID",
    "agent_input_agg_df.delta": "ALERT_ID",
    "ACM_MD_ALERT_STATUSES.delta": "E_ISSUE",
}


def clean_up():
    try:
        shutil.rmtree(STANDARDIZED_DATA_DIR)
        shutil.rmtree(CLEANSED_DATA_DIR)
        shutil.rmtree(APPLICATION_DATA_DIR)
        os.makedirs("data", exist_ok=True)
    except FileNotFoundError:
        pass


def compare_created_data_with_reference_data(reference, unique_column="ALERT_INTERNAL_ID"):
    rel_path = os.path.relpath(os.path.dirname(reference), "tests/shared/reference")
    filename = os.path.basename(reference)
    reference_dataframe = spark.read_delta(os.path.join("data", rel_path, filename))
    if not os.path.exists(os.path.join("data", rel_path, filename)):
        raise FileNotFoundError(os.path.join("data", rel_path, filename))
    tested_dataframe = spark.read_delta(reference)
    compare_dataframe(tested_dataframe, reference_dataframe, unique_column)


def test_integration():
    clean_up()
    pipeline = AIAPipeline(spark_engine)
    pipeline.convert_raw_to_standardized(RAW_DATA_DIR, STANDARDIZED_DATA_DIR)
    pipeline.transform_standardized_to_cleansed()
    pipeline.transform_cleansed_to_application()
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

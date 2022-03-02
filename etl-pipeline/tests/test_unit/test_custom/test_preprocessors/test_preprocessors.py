import json
import os
from typing import Dict

from etl_pipeline.custom.aia.preprocessors.note_preprocessor import add_note_stage
from etl_pipeline.custom.aia.preprocessors.status_preprocessor import add_status_stage
from etl_pipeline.data_processor_engine.spark_engine import SparkProcessingEngine
from tests.shared import TEST_SHARED_DATA_REFERENCE_DIR

spark_engine = SparkProcessingEngine()
spark_instance = spark_engine.spark_instance


def test_note_preprocessor():
    note_input = spark_instance.read_delta(
        f"{TEST_SHARED_DATA_REFERENCE_DIR}/2.standardized/ACM_ALERT_NOTES.delta"
    )
    expected_output = _read_json("note_output.json")
    result_frame = add_note_stage(note_input, spark_instance=spark_instance)
    result_frame_pd = result_frame.toPandas()
    for column_name in [
        "analyst_note_stage",
        "row_num",
        "first_analyst_row_num",
        "last_analyst_row_num",
    ]:
        assert list(result_frame_pd[column_name]) == expected_output[column_name]


def test_status_preprocessor():
    status_input = spark_instance.read_delta(
        f"{TEST_SHARED_DATA_REFERENCE_DIR}/2.standardized/ACM_ITEM_STATUS_HISTORY.delta"
    )
    expected_output = _read_json("status_output.json")
    result_frame = add_status_stage(status_input, system_id="22601", spark_instance=spark_instance)
    result_frame_pd = result_frame.toPandas()
    for column_name in [
        "analyst_status_stage",
        "row_num",
        "first_analyst_row_num",
        "last_analyst_row_num",
    ]:
        assert list(result_frame_pd[column_name]) == expected_output[column_name]


def _read_json(file_name: str) -> Dict:
    file_path = os.path.join(
        "tests/test_unit/test_custom/test_preprocessors/resources/", file_name
    )
    with open(file_path, "r") as file:
        data = json.load(file)
    return data

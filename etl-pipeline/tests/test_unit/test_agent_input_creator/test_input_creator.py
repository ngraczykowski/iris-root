import os
from shutil import rmtree
from unittest import TestCase

from etl_pipeline.agent_input_creator.config import AGENT_INPUT_CONFIG
from etl_pipeline.agent_input_creator.input_creator import create_input_for_agents
from etl_pipeline.data_processor_engine.spark_engine import SparkProcessingEngine
from tests.shared import TEST_SHARED_DATA_REFERENCE_DIR
from tests.utils import compare_dataframe

spark_engine = SparkProcessingEngine()
spark_instance = spark_engine.spark_instance


class TestCreateInput(TestCase):
    def setUp(self) -> None:
        rmtree("temporary", ignore_errors=True)
        os.makedirs("temporary", exist_ok=True)

    def test_create_input(self):

        cleansed_alert_df = spark_instance.read_delta(
            "tests/test_unit/test_agent_input_creator/reference"
        )
        cleansed_alert_df = spark_instance.cast_array_null_to_array_string(cleansed_alert_df)

        create_input_for_agents(
            cleansed_alert_df, destination="temporary/", spark_instance=spark_instance
        )

        for agent_name in AGENT_INPUT_CONFIG.keys():
            result_df = spark_instance.read_delta(
                f"temporary/agent-input/{agent_name}_input.delta"
            )
            expected_df = spark_instance.read_delta(
                f"{TEST_SHARED_DATA_REFERENCE_DIR}/4.application/agent-input/{agent_name}_input.delta"
            )
            assert compare_dataframe(result_df, expected_df)

    def tearDown(self) -> None:
        rmtree("temporary", ignore_errors=True)

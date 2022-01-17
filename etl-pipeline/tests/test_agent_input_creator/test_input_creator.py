import os
from shutil import rmtree
from unittest import TestCase

from etl_pipeline.agent_input_creator.config import AGENT_INPUT_CONFIG
from etl_pipeline.agent_input_creator.input_creator import create_input
from pipeline.spark import spark_instance
from tests.utils import compare_dataframe


class TestCreateInput(TestCase):
    def setUp(self) -> None:
        rmtree("temporary", ignore_errors=True)
        os.makedirs("temporary", exist_ok=True)

    def test_create_input(self):

        cleansed_alert_df = spark_instance.read_delta("tests/test_agent_input_creator/reference")
        cleansed_alert_df = spark_instance.cast_array_null_to_array_string(cleansed_alert_df)

        create_input(cleansed_alert_df, destination="temporary/")

        for agent_name in AGENT_INPUT_CONFIG.keys():
            result_df = spark_instance.read_delta(
                f"temporary/agent-input/{agent_name}_input.delta"
            )
            expected_df = spark_instance.read_delta(
                f"tests/data/4.application/agent-input/{agent_name}_input.delta"
            )
            assert compare_dataframe(result_df, expected_df)

    def tearDown(self) -> None:
        rmtree("temporary", ignore_errors=True)

import os
import unittest

from pyspark.sql.types import NullType
from spark_manager.spark_client import SparkClient
from spark_manager.spark_config import SPARK_CONF

from etl_pipeline.config import pipeline_config
from etl_pipeline.data_processor_engine.spark_engine import SparkProcessingEngine
from pipelines.ms.spark_pipeline import MSPipeline
from tests.utils import compare_dataframe

cn = pipeline_config.cn

ALERT_INTERNAL_ID = cn.ALERT_INTERNAL_ID
TEST_PATH = "tests/shared/test_ms_pipeline/"
spark_instance = SparkClient(SPARK_CONF)


def load_input_and_reference_data(func, spark_instance):
    funcname = func.__name__
    input_data, output_data = spark_instance.read_delta(
        os.path.join(TEST_PATH, funcname + "_input.delta")
    ), spark_instance.read_delta(os.path.join(TEST_PATH, funcname + "_output.delta"))

    return input_data, output_data


class TestMSPipeline(unittest.TestCase):
    @classmethod
    def setUpClass(self):
        self.spark_engine = SparkProcessingEngine()
        self.uut = MSPipeline(self.spark_engine, config=pipeline_config)

    @unittest.skip
    def test_cleansed_to_agent_input(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_cleansed_to_agent_input, self.spark_engine.spark_instance
        )
        result = self.uut.transform_cleansed_to_application(input_data)
        df_schema_dict = {f.name.lower(): f.dataType for f in result.schema.fields}

        x = [key for key, value in df_schema_dict.items() if not isinstance(value, NullType)]

        assert compare_dataframe(result.select(x), reference_data)

    @unittest.skip
    def test_ms_customized_merge_df360_and_WM_Account_In_Scope(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_ms_customized_merge_df360_and_WM_Account_In_Scope,
            self.uut.engine.spark_instance,
        )
        input_data.registerTempTable("df360")
        result = self.uut.ms_customized_merge_df360_and_WM_Account_In_Scope()
        assert compare_dataframe(result, reference_data, unique_column=cn.ALERT_ID)

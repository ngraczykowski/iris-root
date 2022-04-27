import os
import unittest

from spark_etl_pipeline.data_processor_engine.spark_engine.spark import SparkProcessingEngine
from test_spark.utils import compare_dataframe

from etl_pipeline.config import Pipeline

pipeline_config = Pipeline("config/pipeline/spark-pipeline.yaml")
cn = pipeline_config.cn
ALERT_ID, ALERT_INTERNAL_ID = cn.ALERT_ID, cn.ALERT_INTERNAL_ID

TEST_PATH = "test_spark/shared/test_ms_pipeline/"


def load_input_and_reference_data(func, spark_instance):
    funcname = func.__name__
    input_data, output_data = spark_instance.read_delta(
        os.path.join(TEST_PATH, funcname + "_input.delta")
    ), spark_instance.read_delta(os.path.join(TEST_PATH, funcname + "_output.delta"))
    return input_data, output_data


class TestEngine(unittest.TestCase):
    @classmethod
    def setUpClass(self):
        self.uut = SparkProcessingEngine(pipeline_config)
        self.uut.pipeline_config = pipeline_config

    def test_set_ref_key(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_ref_key, self.uut.spark_instance
        )
        result = self.uut.set_ref_key(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_set_trigger_reasons(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_trigger_reasons, self.uut.spark_instance
        )
        result = self.uut.set_trigger_reasons(input_data, cn)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_merge_with_party_and_address_relationships(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_merge_with_party_and_address_relationships, self.uut.spark_instance
        )
        result = self.uut.merge_with_party_and_address_relationships(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_set_beneficiary_hits(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_beneficiary_hits, self.uut.spark_instance
        )
        result = self.uut.set_beneficiary_hits(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_set_concat_residue(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_concat_residue, self.uut.spark_instance
        )
        result = self.uut.set_concat_residue(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_set_concat_address_no_change(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_concat_address_no_change, self.uut.spark_instance
        )
        result = self.uut.set_concat_address_no_change(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_set_discovery_tokens(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_discovery_tokens, self.uut.spark_instance
        )
        result = self.uut.set_discovery_tokens(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

    def test_set_clean_names(self):
        input_data, reference_data = load_input_and_reference_data(
            self.test_set_clean_names, self.uut.spark_instance
        )
        result = self.uut.set_clean_names(input_data)
        assert compare_dataframe(result, reference_data, unique_column=ALERT_ID)

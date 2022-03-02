import os
import unittest

import pytest

from custom.aia.config import AIA_ALERT_HEADER_CONFIG, AIA_HIT_CONFIG
from custom.aia.xml_pipeline import AIAWatchlistExtractor, AIAXMLPipeline
from etl_pipeline.data_processor_engine.spark_engine import SparkProcessingEngine
from etl_pipeline.xml_parser.xml_pipeline import AlertHitDictFactory, WatchlistExtractor
from tests.shared import TEST_ETL_PIPELINE_DATA_PATH, TEST_SHARED_DATA_REFERENCE_DIR
from tests.test_unit.test_etl_pipeline.reference.test_get_wl_hit_aliases_matched_name import (
    INPUT_WHITELIST,
    REFERENCE_OUTPUT_FOR_TEST_WL_HIT_ALIASES_MATCHED_NAME,
)
from tests.utils import compare_dataframe, load_json, load_pickle, load_xml

spark_engine = SparkProcessingEngine()
spark_instance = spark_engine.spark_instance


class TestAlertHitDicttFactory(unittest.TestCase):
    def setUp(self):
        self.uut = AlertHitDictFactory(
            hit_config=AIA_HIT_CONFIG, alert_config=AIA_ALERT_HEADER_CONFIG
        )

    def test__invoke_xml_util(self):
        input_xml = load_xml(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH, "test_input_extract_alert_and_hits_from_xml.xml"
            )
        )
        watchlist = AIAWatchlistExtractor(
            hit_config=AIA_HIT_CONFIG, alert_config=AIA_ALERT_HEADER_CONFIG
        )
        _, hits = watchlist.extract_alert_and_hits_string_from_xml(input_xml)
        result = self.uut._invoke_xml_util(watchlist.alert_hit_dict_factory.hit_config, hits[0])
        reference_json = load_json(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH, "test_output_extract_alert_and_hits_from_xml.json"
            )
        )
        self.assertTrue(result, reference_json)

    def test__create_spark_schema(self):
        result = self.uut._create_spark_schema(AIA_HIT_CONFIG)
        reference_output = load_pickle(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_output_get_hit_spark_schema.pkl")
        )
        self.assertEqual(result, reference_output)

    def test_get_alert_spark_schema(self):
        result = self.uut.get_alert_spark_schema()
        reference_output = load_pickle(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_output_get_alert_spark_schema.pkl")
        )
        self.assertEqual(result, reference_output)

    def test_get_hit_spark_schema(self):
        result = self.uut.get_hit_spark_schema()
        reference_output = load_pickle(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_output_get_hit_spark_schema.pkl")
        )
        self.assertEqual(result, reference_output)


class TestWatchlistExtractor(unittest.TestCase):
    def setUp(self):
        self.uut = WatchlistExtractor()

    def test_get_wl_hit_aliases_matched_name(self):
        result = self.uut.get_wl_hit_aliases_matched_name(*INPUT_WHITELIST)
        self.assertTrue(result, REFERENCE_OUTPUT_FOR_TEST_WL_HIT_ALIASES_MATCHED_NAME)

    def test_if_list_is_empty_return_empty_list(self):
        args = [[], [], []]
        result = self.uut.get_wl_hit_aliases_matched_name(*args)
        self.assertEqual(len(result), 0)


@pytest.mark.parametrize(
    ("args", "expected_result"),
    [
        (([("A"), ("b", "c")], [("A")], [("alias")]), (["alias", ("b", "c")])),
        (([("A"), ("b", "c")], [("Aa")], [("A")]), (["A", ("b", "c")])),
    ],
)
def test_get_wl_hit_aliases_matched_name(args, expected_result):
    uut = WatchlistExtractor()
    hit_inputExplanations, hit_aliases_matchedName, hit_aliases_displayName = args
    result = uut.get_wl_hit_aliases_matched_name(
        hit_aliases_displayName, hit_aliases_matchedName, hit_inputExplanations
    )
    assert set(result) == set(expected_result)


class TestAIAWatchlistExtractor(unittest.TestCase):
    def setUp(self):
        self.uut = AIAWatchlistExtractor(
            hit_config=AIA_HIT_CONFIG, alert_config=AIA_ALERT_HEADER_CONFIG
        )

    def test_parse_hit(self):
        input_xml = load_xml(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH, "test_input_extract_alert_and_hits_from_xml.xml"
            )
        )
        _, hits = self.uut.extract_alert_and_hits_string_from_xml(input_xml)

        result = self.uut.parse_hit(hits[0])
        reference_json = load_json(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH, "test_output_extract_alert_and_hits_from_xml.json"
            )
        )
        self.assertTrue(result, reference_json)

    def test_remove_unnecessary_columns(self):
        df = spark_instance.read_delta(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH,
                "test_input_remove_unnecessary_columns.delta",
            )
        )
        result = self.uut.remove_unnecessary_columns(df)
        reference_df = spark_instance.read_delta(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH,
                "test_output_remove_unnecessary_columns.delta",
            )
        )
        self.assertTrue(compare_dataframe(result, reference_df))

    def test_unwrap_alert_hits(self):
        pipeline = AIAXMLPipeline(spark_engine)
        df = spark_instance.read_delta(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_input_unwrap_alert_hits.delta")
        )
        result = self.uut.unwrap_alert_hits(df, pipeline.schema)
        reference_df = spark_instance.read_delta(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_output_unwrap_alert_hits.delta")
        )
        self.assertTrue(compare_dataframe(result, reference_df))

    def test_extract_alert_and_hits_from_xml(self):
        input_xml = load_xml(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH, "test_input_extract_alert_and_hits_from_xml.xml"
            )
        )
        result = self.uut.extract_alert_and_hits_from_xml(input_xml)
        reference_json = load_json(
            os.path.join(
                TEST_ETL_PIPELINE_DATA_PATH, "test_output_extract_alert_and_hits_from_xml.json"
            )
        )
        self.assertTrue(result, reference_json)


class TestXMLPipeline(unittest.TestCase):
    def setUp(self):
        self.uut = AIAXMLPipeline(spark_engine)

    def test_unwrap_xml(self):
        result = self.uut.unwrap_xml(
            os.path.join(TEST_SHARED_DATA_REFERENCE_DIR, "2.standardized/ALERTS.delta")
        )
        df = spark_instance.read_delta(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_output_unwrap_xml.delta")
        )
        self.assertTrue(compare_dataframe(result, df))

    def test_clear_empty_hits(self):
        df = spark_instance.read_delta(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_input_clear_empty_hits.delta")
        )
        result = self.uut.clear_empty_hits(df)
        reference_df = spark_instance.read_delta(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_output_clear_empty_hits.delta")
        )
        self.assertTrue(compare_dataframe(result, reference_df))

    def test_pipeline(self):
        created_df = self.uut.pipeline(
            os.path.join(TEST_SHARED_DATA_REFERENCE_DIR, "2.standardized/ALERTS.delta")
        )
        df = spark_instance.read_delta(
            os.path.join(TEST_ETL_PIPELINE_DATA_PATH, "test_pipeline.delta")
        )
        assert compare_dataframe(created_df, df)

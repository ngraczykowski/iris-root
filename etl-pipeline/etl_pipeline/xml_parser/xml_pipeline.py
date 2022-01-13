"""Main pipeline that accounts for the XML mapping"""

from typing import Iterable, List, Optional, Tuple, Union

import lxml
import pyspark
import pyspark.sql.functions as F
from pyspark.sql.types import ArrayType, BooleanType, StringType, StructField, StructType
from spark_manager.spark_client import SparkClient

from etl_pipeline.xml_parser.config import XMLExtractorConfig
from etl_pipeline.xml_parser.xml_extractor import XMLExtractor, XMLFunction

FieldOutputType = Union[Optional[str], Optional[bool], List[Optional[bool]], List[Optional[str]]]
SparkTypeConfig = StructType


class AlertHitDictFactory:
    """Creates dictionary based on the customer scheme"""

    xml_util = XMLExtractor()

    def __init__(
        self,
        hit_config: XMLExtractorConfig,
        alert_config: XMLExtractorConfig,
    ):
        self.alert_config: XMLExtractorConfig = alert_config
        self.hit_config: XMLExtractorConfig = hit_config

    def _invoke_xml_util(self, config: XMLExtractorConfig, row: str) -> FieldOutputType:
        """Invokes xml function.

        Parameters
        ----------
        config : XMLExtractorConfig
            Config that maps standardized column name to Tuple of Function and xpath.
        row : str
            Alert in xml format.

        Returns
        -------
        FieldOutputType
            Dict in which key is the columns and result are values
            extracted from the xml string.
        """
        result = {}
        for field_name, (xml_util_func_name, xpath_arg) in config.items():
            xml_util_func_name = xml_util_func_name.value
            xml_util_func = getattr(self.xml_util, xml_util_func_name)
            result[field_name] = xml_util_func(row, xpath_arg)
        return result

    def _create_spark_schema(self, config: XMLExtractorConfig) -> SparkTypeConfig:
        """Creates structtype that can be fed to the Spark executors from XMLExtractorConfig.

        Parameters
        ----------
        config : XMLExtractorConfig
            Config for which the spark schema is created.

        Returns
        -------
        SparkTypeConfig
            StructType that is a list of pairs:
            function name - xpath.
        """
        struct_fields_list = []
        for field_name, (xml_util_func_name, _) in config.items():
            if xml_util_func_name in [
                XMLFunction.extract_string_elements_array,
                XMLFunction.extract_value,
            ]:
                field_type = StringType()
            else:
                field_type = BooleanType()

            if xml_util_func_name in [
                XMLFunction.extract_string_elements_array,
                XMLFunction.extract_boolean_array,
            ]:
                field_type = ArrayType(field_type)

            struct_fields_list.append(StructField(field_name, field_type))

        return StructType(struct_fields_list)

    def get_alert_spark_schema(self) -> SparkTypeConfig:
        """Returns
        -------
        SparkTypeConfig
            Spark schema for alert config.
        """
        return self._create_spark_schema(self.alert_config)

    def get_hit_spark_schema(self) -> SparkTypeConfig:
        """Returns
        -------
        SparkTypeConfig
            Spark schema for hit config.
        """
        return self._create_spark_schema(self.hit_config)


class WatchlistExtractor:
    def get_wl_hit_aliases_matched_name(
        self,
        hit_aliases_displayName: Iterable[str],
        hit_aliases_matchedName: Iterable[str],
        hit_inputExplanations: Iterable[str],
    ) -> List[str]:
        """Produce list of names that contains aliases.

        Parameters
        ----------
        hit_aliases_displayName : Iterable[str]
        hit_aliases_matchedName : Iterable[str]
        hit_inputExplanations : Iterable[str]

        Returns
        -------
        List[str]
        """
        result = []
        hit_inputExplanations = list(set(hit_inputExplanations))
        for hit_inputExplanation in hit_inputExplanations:
            if hit_inputExplanation in hit_aliases_matchedName:
                index_in_matchedName = hit_aliases_matchedName.index(hit_inputExplanation)
                result.append(hit_aliases_displayName[index_in_matchedName])
            else:
                result.append(hit_inputExplanation)
        return result

    def unwrap_alert_hits(
        self, df: pyspark.sql.DataFrame, schema: StructType
    ) -> pyspark.sql.DataFrame:
        """Parameters
        ----------
        df : pyspark.sql.DataFrame
        schema : StructType

        Returns
        -------
        pyspark.sql.DataFrame
        """
        return df.withColumn(
            "alert_hits",
            F.udf(self.extract_alert_and_hits_from_xml, schema)(self.xml_column_name),
        )

    def extract_alert_and_hits_from_xml(self, xml: str) -> Tuple[FieldOutputType, FieldOutputType]:
        """Parameters
        ----------
        xml : str

        Returns
        -------
        Tuple[FieldOutputType, FieldOutputType]
        """
        alert, hits = self.extract_alert_and_hits_string_from_xml(xml)
        alert_header_dict = self.extract_alert_header(alert)
        hit_dict_list = [self.parse_hit(hit) for hit in hits]
        return alert_header_dict, hit_dict_list

    def parse_alert_header(self, alert_header: lxml.etree._Element) -> FieldOutputType:
        """Parameters
        ----------
        alert_header : lxml.etree._Element

        Returns
        -------
        FieldOutputType
        """
        return self.alert_hit_dict_factory._invoke_xml_util(
            self.alert_hit_dict_factory.alert_config, alert_header
        )

    def parse_hit(self, hit: lxml.etree._Element) -> FieldOutputType:
        """Parameters
        ----------
        hit : lxml.etree._Element

        Returns
        -------
        FieldOutputType
        """
        return self.alert_hit_dict_factory._invoke_xml_util(
            self.alert_hit_dict_factory.hit_config, hit
        )


class XMLPipeline:
    def __init__(
        self,
        spark_instance: SparkClient = None,
        hit_config=None,
        alert_config=None,
        watchlist_cls: WatchlistExtractor = None,
    ):
        self.watchlist = watchlist_cls(hit_config=hit_config, alert_config=alert_config)
        self.spark_instance = spark_instance
        self.schema = None

    def unwrap_xml(self, source_path: str) -> pyspark.sql.DataFrame:
        """Reads table and transforms `xml` alert to table.

        Parameters
        ----------
        source_path : str

        Returns
        -------
        pyspark.sql.DataFrame
        """
        std_alert_df = self.spark_instance.read_delta(source_path)
        alert_df = self.watchlist.unwrap_alert_hits(std_alert_df, self.schema)
        alert_hits_df = self.watchlist.remove_unnecessary_columns(alert_df)
        ap_hit_names_sql = self.spark_instance.merge_to_target_col_from_source_cols_sql_expression(
            alert_hits_df,
            "ap_hit_names",
            [
                "hit_inputExplanations_matchedName_inputExplanation",
                "hit_inputExplanations_aliases_matchedName_inputExplanation",
            ],
        )
        alert_ap_hit_names_df = alert_hits_df.select("*", ap_hit_names_sql)
        return alert_ap_hit_names_df

    def clear_empty_hits(
        self, alert_ap_hit_names_df: pyspark.sql.DataFrame
    ) -> pyspark.sql.DataFrame:
        """Parameters
        ----------
        alert_ap_hit_names_df : pyspark.sql.DataFrame

        Returns
        -------
        pyspark.sql.DataFrame
        """
        alert_ap_wl_hit_names_df = alert_ap_hit_names_df.withColumn(
            "wl_hit_matched_name",
            F.when(
                F.expr("size(hit_explanations_matchedName_Explanation) > 0"),
                F.col("hit_displayName"),
            ).otherwise(F.lit(None)),
        ).withColumn(
            "wl_hit_aliases_matched_name",
            F.udf(self.watchlist.get_wl_hit_aliases_matched_name, ArrayType(StringType()))(
                "hit_aliases_displayName",
                "hit_aliases_matchedName",
                "hit_explanations_aliases_matchedName_Explanation",
            ),
        )
        return alert_ap_wl_hit_names_df

    def pipeline(self, source_path: str) -> pyspark.sql.DataFrame:
        """Parameters
        ----------
        source_path : str

        Returns
        -------
        pyspark.sql.DataFrame
        """
        alert_ap_hit_names_df = self.unwrap_xml(source_path)
        alert_ap_wl_hit_names_df = self.clear_empty_hits(alert_ap_hit_names_df)
        return alert_ap_wl_hit_names_df

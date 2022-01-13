import re
from typing import List, Tuple

import lxml
import pyspark
from pyspark.sql.types import ArrayType, StructField, StructType

from customized.config import AIA_ALERT_HEADER_CONFIG, AIA_HIT_CONFIG
from etl_pipeline.xml_parser.config import XMLExtractorConfig
from etl_pipeline.xml_parser.xml_pipeline import (
    AlertHitDictFactory,
    FieldOutputType,
    WatchlistExtractor,
    XMLPipeline,
)


class AIAWatchlistExtractor(WatchlistExtractor):
    clean_xml_declaration_regex = re.compile(r"<\?xml[^>]+>")
    xml_column_name = "html_file_key"

    def __init__(self, hit_config: XMLExtractorConfig, alert_config: XMLExtractorConfig):
        self.alert_hit_dict_factory = AlertHitDictFactory(
            hit_config=hit_config, alert_config=alert_config
        )

    def extract_alert_and_hits_string_from_xml(
        self, xml_string: str
    ) -> Tuple[lxml.etree._Element, List[lxml.etree._Element]]:
        """Parameters
        ----------
        xml_string : str
        Returns
        -------
        Tuple[lxml.etree._Element, List[lxml.etree._Element]]
        """
        xml_cleaned = self.clean_xml_declaration_regex.sub("", xml_string)
        alert = lxml.etree.fromstring(xml_cleaned)
        hits = alert.xpath('hits/elem[@name="hit"]')
        return alert, hits

    def extract_alert_header(self, alert: lxml.etree._Element) -> FieldOutputType:
        """Parameters
        ----------
        alert : lxml.etree._Element

        Returns
        -------
        FieldOutputType
        """
        alert_header = alert.xpath("alert-header")[0]
        alert_header_dict = self.parse_alert_header(alert_header)
        return alert_header_dict

    def remove_unnecessary_columns(self, df: pyspark.sql.DataFrame) -> pyspark.sql.DataFrame:
        df = (
            df.selectExpr("*", "alert_hits.*")
            .selectExpr("*", "explode(hits) as hit")
            .selectExpr("*", "alert_header.*")
            .selectExpr("*", "hit.*")
            .drop("alert_hits", "alert_header", "hits", "hit")
        )
        return df


class AIAXMLPipeline(XMLPipeline):
    _hit_config = AIA_HIT_CONFIG
    _alert_config = AIA_ALERT_HEADER_CONFIG
    _watchlist_cls = AIAWatchlistExtractor

    def __init__(self, *args, **kwargs):
        kwargs["hit_config"] = self._hit_config
        kwargs["watchlist_cls"] = self._watchlist_cls
        kwargs["alert_config"] = self._alert_config

        super().__init__(*args, **kwargs)

        hit_schema = self.watchlist.alert_hit_dict_factory.get_hit_spark_schema()
        self.schema = StructType(
            [
                StructField(
                    "alert_header", self.watchlist.alert_hit_dict_factory.get_alert_spark_schema()
                ),
                StructField("hits", ArrayType(hit_schema)),
            ]
        )

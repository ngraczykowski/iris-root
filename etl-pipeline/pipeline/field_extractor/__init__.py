import re

import lxml.etree

from silenteight.aia.utils import XMLUtil

from pyspark.sql.types import StringType, ArrayType, BooleanType, StructType, StructField
from pipeline.field_extractor.config import HIT_CONFIG, ALERT_HEADER_CONFIG
from enum import Enum


class AlertHitDictFactory:
    xml_util = XMLUtil()

    def __init__(self):
        self.alert_header_config = ALERT_HEADER_CONFIG
        self.hit_config = HIT_CONFIG

    def _invoke_xml_util(self, alert_or_hit_config, alert_or_hit):
        result = {}
        for k, v in alert_or_hit_config.items():
            field_name = k
            xml_util_func_name = v[0]
            xpath_arg = v[1]

            xml_util_func = getattr(self.xml_util, xml_util_func_name)
            result[field_name] = xml_util_func(alert_or_hit, xpath_arg)

        return result

    def parse_alert_header(self, alert_header):
        return self._invoke_xml_util(self.alert_header_config, alert_header)

    def parse_hit(self, hit):
        return self._invoke_xml_util(self.hit_config, hit)

    def _create_spark_schema(self, alert_or_hit_config):
        struct_fields_list = []
        for k, v in alert_or_hit_config.items():
            field_name = k
            xml_util_func_name = v[0]

            if "text" in xml_util_func_name:
                field_type = StringType()
            else:
                field_type = BooleanType()

            if "all" in xml_util_func_name:
                field_type = ArrayType(field_type)

            struct_fields_list.append(StructField(field_name, field_type))

        return StructType(struct_fields_list)

    def get_alert_spark_schema(self):
        return self._create_spark_schema(self.alert_header_config)

    def get_hit_spark_schema(self):
        return self._create_spark_schema(self.hit_config)


class AlertHitExtractor:
    clean_xml_declaration_regex = re.compile("<\?xml[^>]+>")
    alert_hit_dict_factory = AlertHitDictFactory()
    first_ordinal = 1

    def extract_alert_hits_from_xml(self, xml):
        xml_cleaned = self.clean_xml_declaration_regex.sub("", xml)
        try:
            alert = lxml.etree.fromstring(xml_cleaned)

            alert_header = alert.xpath("alert-header")[0]
            alert_header_dict = self.alert_hit_dict_factory.parse_alert_header(alert_header)

            hit_dict_list = []
            hits = alert.xpath('hits/elem[@name="hit"]')
            for hit in hits:
                hit_dict = self.alert_hit_dict_factory.parse_hit(hit)
                hit_dict_list.append(hit_dict)

            return alert_header_dict, hit_dict_list
        except:
            pass


#     def __extract_hits(self, alert_element):
#         hits = []
#         ordinal = self.first_ordinal
#         for hit in alert_element.xpath('hits//hit | Hits//hit'):
#             hit_dict = self.hit_dict_factory.create(alert=alert_element, hit=hit)
#             hit_dict['hit_ordinal'] = ordinal
#             serialized_hit_dict = SerializableOrderedDict(hit_dict).serialize_for_pandas()
#             hits.append(serialized_hit_dict)
#             ordinal += 1
#         return hits

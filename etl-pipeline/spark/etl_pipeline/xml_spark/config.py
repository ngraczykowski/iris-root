from typing import Dict, Tuple

from etl_pipeline.xml_parser.xml_extractor import XMLFunctionType

XMLExtractorConfig = Dict[str, Tuple[XMLFunctionType, str]]
XMLSparkExtractorConfig = Dict[str, Tuple[XMLFunctionType, str]]

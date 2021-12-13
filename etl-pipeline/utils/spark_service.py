from typing import NamedTuple

from pyspark.sql import SparkSession

import utils.config_service as configservice


class SparkProperties(NamedTuple):
    master: str
    app_name: str
    config: any


def load_spark_properties() -> SparkProperties:
    spark = configservice.get_spark_config()

    return SparkProperties(master=spark["master"], app_name=spark["app-name"], config=spark["config"])


def load_spark():
    properties = load_spark_properties()
    builder = SparkSession.builder.master(properties.master).appName(properties.app_name)

    for config_key in properties.config:
        builder.config(config_key, properties.config[config_key])

    return builder.getOrCreate()

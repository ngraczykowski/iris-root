import math

import psutil
import pyspark
from pyspark.sql import SparkSession

import etl_pipeline.utils.config_service as configservice
from etl_pipeline.config.config import (
    SPARK_APP_NAME,
    SPARK_DRIVER_MEMORY,
    SPARK_MASTER,
    SPARK_USE_OPTIMAL_CONFIG,
)
from etl_pipeline.logger import get_logger
from etl_pipeline.spark.spark_config import spark_conf


class SparkServer:
    logger = get_logger("Spark Launcher")

    def launch(self):
        self.set_spark_cpu_memory(spark_conf)
        spark = SparkSession.builder.config(conf=spark_conf).appName(SPARK_APP_NAME).getOrCreate()
        spark.sparkContext.setLogLevel("info")
        self.logger.info(f"pySpark version: {pyspark.__version__}")
        self.logger.info("Spark configs:")
        for i in spark_conf.getAll():
            self.logger.info(i)

        self.logger.info("Spark UI - %s" % spark.sparkContext.uiWebUrl)

    def to_bytes(self, value):
        if value.endswith("g"):
            return int(value.replace("g", "")) * 1024 * 1024 * 1024
        elif value.endswith("m"):
            return int(value.replace("m", "")) * 1024 * 1024
        elif value.endswith("k"):
            return int(value.replace("k", "")) * 1024

        return value

    def validate_available_memory(self):
        properties = configservice.get_spark_config()

        available_bytes = psutil.virtual_memory().available

        driver_mem_bytes = self.to_bytes(properties["config"]["spark.driver.memory"])
        executor_mem_bytes = self.to_bytes(properties["config"]["spark.executor.memory"])

        required_bytes = driver_mem_bytes + executor_mem_bytes

        min_available_memory_1 = (
            float(properties.get("min-available-memory-ratio", 0)) * required_bytes
        )
        min_available_memory_2 = (
            self.to_bytes(properties.get("min-available-memory-over-required", "0g"))
            + required_bytes
        )

        min_available_memory = max(min_available_memory_1, min_available_memory_2)

        self.logger.info(f"Available memory: {available_bytes}")
        self.logger.info(f"Required memory: {min_available_memory}")

        if available_bytes < min_available_memory:
            raise Exception(
                f"Not enough available memory left: {available_bytes} bytes."
                "Required at least: {min_available_memory} bytes"
            )

    def _get_optimal_ram(self):
        available_ram_gb = psutil.virtual_memory().available / (1024 ** 3)
        if available_ram_gb >= 64:
            ram_gb = math.ceil(available_ram_gb * 0.5)
        if available_ram_gb >= 32:
            ram_gb = math.ceil(available_ram_gb * 0.6)
        elif available_ram_gb >= 24:
            ram_gb = math.ceil(available_ram_gb * 0.7)
        elif available_ram_gb >= 16:
            ram_gb = math.ceil(available_ram_gb * 0.75)
        else:
            ram_gb = math.ceil(available_ram_gb * 0.8)

        return str(ram_gb) + "g"

    def set_spark_cpu_memory(self, spark_conf):
        cpu_count = psutil.cpu_count() / 2
        master = "local[%d]" % cpu_count if SPARK_USE_OPTIMAL_CONFIG else SPARK_MASTER
        memory = self._get_optimal_ram() if SPARK_USE_OPTIMAL_CONFIG else SPARK_DRIVER_MEMORY
        spark_conf.setAll(
            [("spark.master", "%s" % master), ("spark.driver.memory", "%s" % memory), ("spark.driver.bindAddress", "localhost")]
        )
        self.logger.info("Actual Spark Master: %s" % master)
        self.logger.info("Actual Spark Driver Memory: %s" % memory)

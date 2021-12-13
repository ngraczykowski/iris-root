from spark_manager.spark_client import SparkClient
from spark_manager.spark_config import SPARK_CONF
from spark_manager.spark_launcher import SparkLauncher

spark_instance = SparkClient(SPARK_CONF)

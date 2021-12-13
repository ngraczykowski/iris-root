import os
from pathlib import Path

from serppythonclient.yml import YamlLoader

ROOT_DIR = str(Path(__file__).parent.parent.absolute())

with open("/app/config.yaml") as file:
    config = YamlLoader.get_instance().load(file)


os.environ["SERP_HOME"] = ".."
DEFAULT_PROCESSING_POOL_SIZE = config["RESOURCES"]["APP_NAME"]
SPARK_APP_NAME = config["SPARK"]["APP_NAME"]
SPARK_MASTER = config["SPARK"]["MASTER"]
SPARK_DRIVER_MEMORY = config["SPARK"]["DRIVER_MEMORY"]
SPARK_EXECUTOR_MEMORY = config["SPARK"]["EXECUTOR_MEMORY"]
SPARK_USE_OPTIMAL_CONFIG = config["SPARK"]["USE_OPTIMAL_CONFIG"] == "true"
SPARK_IVY2_DIR = config["SPARK"]["IVY2_DIR"]
SPARK_DB_JARS = config["SPARK"]["DB_JARS"]
SPARK_TMP_DIR = config["SPARK"]["TMP_DIR"]
RAW_DATA_DIR = config["APP"]["RAW_DATA_DIR"]
STANDARDIZED_DATA_DIR = config["APP"]["STANDARDIZED_DATA_DIR"]
CLEANSED_DATA_DIR = config["APP"]["CLEANSED_DATA_DIR"]
APPLICATION_DATA_DIR = config["APP"]["APPLICATION_DATA_DIR"]
REPORT_DATA_DIR = config["APP"]["REPORT_DATA_DIR"]
SANDBOX_DATA_DIR = config["APP"]["SANDBOX_DATA_DIR"]
APPLICATION_CONF_DIR = config["APP"]["APPLICATION_CONF_DIR"]


def get_spark_config():
    return config["SPARK"]

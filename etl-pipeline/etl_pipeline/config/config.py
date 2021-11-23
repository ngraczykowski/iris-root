import os
import platform
from configparser import ConfigParser
from pathlib import Path

ROOT_DIR = str(Path(__file__).parent.parent.absolute())
DEFAULT_CONFIG_DIR = str(Path(ROOT_DIR).joinpath("config").joinpath("default"))
DEFAULT_WORKING_DIR = str(Path(ROOT_DIR).joinpath("data/e2e"))
CONFIG_DIR = os.getenv("S8_CONFIG_DIR_PATH", DEFAULT_CONFIG_DIR)
WORKING_DIR = os.getenv("MOAPOV_WORKING_DIR_PATH", DEFAULT_WORKING_DIR)


config_locations = [
    Path(CONFIG_DIR).joinpath("configuration.yml"),
    Path(CONFIG_DIR).joinpath("../default/configuration.yml"),
]


class MissingConfigurationFileException(Exception):
    def __init__(self):
        super().__init__(f"Missing configuration.yml file. Expected locations: {config_locations}")


CONFIG_PATH = None
for config_location in config_locations:
    if config_location.exists():
        CONFIG_PATH = str(config_location)

if CONFIG_PATH is None:
    raise MissingConfigurationFileException()
PIPELINE_PATH = os.getenv("S8_CONFIG_PIPELINE_PATH", CONFIG_PATH)
DEFAULT_PROCESSING_POOL_SIZE = 2
POV_INTEGRATION_PATH = os.path.join(os.getcwd(), "..")

if "S8_CONFIG_DIR_PATH" not in os.environ:
    MOAPOV_CONFIG_PATH = os.path.join(POV_INTEGRATION_PATH, "conf/default")
    os.environ["S8_CONFIG_DIR_PATH"] = MOAPOV_CONFIG_PATH

if "MOAPOV_WORKING_DIR_PATH" not in os.environ:
    MOAPOV_WORKING_DIR_PATH = os.path.join(POV_INTEGRATION_PATH, "working_dir/default")
    os.environ["MOAPOV_WORKING_DIR_PATH"] = MOAPOV_WORKING_DIR_PATH

MOAPOV_PIPELINE_RESULTS_DIR_PATH = os.path.join(POV_INTEGRATION_PATH, "notebooks-outputs")
MOAPOV_NOTEBOOKS_PATH = os.path.join(POV_INTEGRATION_PATH, "moapov/notebooks")
CS_NOTEBOOKS_PATH = os.path.join(POV_INTEGRATION_PATH, "pov_notebooks")

os.environ["MOAPOV_PIPELINE_RESULTS_DIR_PATH"] = MOAPOV_PIPELINE_RESULTS_DIR_PATH
os.environ["MOAPOV_NOTEBOOKS_PATH"] = MOAPOV_NOTEBOOKS_PATH
os.environ["CS_NOTEBOOKS_PATH"] = CS_NOTEBOOKS_PATH

DEBUG = True


PLATFORM_SYSTEM = platform.system()
config = ConfigParser()

if PLATFORM_SYSTEM == "Windows":
    config_file_path = "config-windows.ini"
elif PLATFORM_SYSTEM == "Darwin":
    config_file_path = "config-mac.ini"
else:
    config_file_path = f"{os.path.join(ROOT_DIR, 'config/config.ini')}"

config.read(config_file_path)

PYTHON_DATA_TOOLBOX_PATH = config["APP"]["PYTHON_DATA_TOOLBOX_PATH"]
DATA_DIR = config["APP"]["DATA_DIR"]

RAW_DATA_DIR = config["APP"]["RAW_DATA_DIR"]
STANDARDIZED_DATA_DIR = config["APP"]["STANDARDIZED_DATA_DIR"]
CLEANSED_DATA_DIR = config["APP"]["CLEANSED_DATA_DIR"]
APPLICATION_DATA_DIR = config["APP"]["APPLICATION_DATA_DIR"]
REPORT_DATA_DIR = config["APP"]["REPORT_DATA_DIR"]
SANDBOX_DATA_DIR = config["APP"]["SANDBOX_DATA_DIR"]
APPLICATION_CONF_DIR = config["APP"]["APPLICATION_CONF_DIR"]


SPARK_APP_NAME = config["SPARK"]["APP_NAME"]
SPARK_MASTER = config["SPARK"]["MASTER"]
SPARK_DRIVER_MEMORY = config["SPARK"]["DRIVER_MEMORY"]
SPARK_USE_OPTIMAL_CONFIG = config["SPARK"].getboolean("USE_OPTIMAL_CONFIG")

SPARK_IVY2_DIR = config["SPARK"]["IVY2_DIR"]
SPARK_DB_JARS = config["SPARK"]["DB_JARS"]
SPARK_TMP_DIR = config["SPARK"]["TMP_DIR"]
SERP_DIRECTORY = config["SERP"]["SERP_DIRECTORY"]
SERP_HOME = os.path.join(ROOT_DIR, SERP_DIRECTORY)
os.environ["SERP_HOME"] = SERP_HOME
os.environ["ROOT_DIR"] = ROOT_DIR
SERP_HOME = config["SERP"]["SERP_DIRECTORY"]

PYTHON_SKIP_PACKAGE_DEPENDENCY = config["PYTHON"].getboolean("SKIP_PACKAGE_DEPENDENCY")

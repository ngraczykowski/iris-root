import os

from omegaconf import OmegaConf

from etl_pipeline.logger import get_logger

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
pipeline_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "pipeline", "pipeline.yaml"))
columns_namespace = pipeline_config.SPARK_DATAFRAME_COLUMNS
pipeline_config = pipeline_config.PIPELINE
service_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))
logger = get_logger("Loading Config")


def load_config(file, alert_type):
    try:
        return OmegaConf.load(os.path.join(CONFIG_APP_DIR, "agents", file))
    except FileNotFoundError as e:
        logger.warning(f"Config does not exist for {alert_type}: {str(e)}")


DATASET_FILE_MAP = {
    "R_US_Active_Address": "agents_input_WM_ADDRESS.yaml",
    "R_US_Active_Party": "agents_input_WM_Party.yaml",
    "R_Global_MultiAccounts_Daily": "agents_input_ISG_Daily_Account.yaml",
    "R_Global_MultiParty_Daily": "agents_input_ISG_Daily_Party.yaml",
    "R_Global_MultiParty_Wkly": "agents_input_ISG_Weekly_Party.yaml",
    "R_Global_MultiAccounts_Wkly": "agents_input_ISG_Weekly_Account.yaml",
}


alert_agents_config = {
    alert_type: load_config(dataset_config_file, alert_type)
    for alert_type, dataset_config_file in DATASET_FILE_MAP.items()
}

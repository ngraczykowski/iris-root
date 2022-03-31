import os

from omegaconf import OmegaConf

from etl_pipeline.logger import get_logger

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
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


def load_agent_config(alert_type, alert_config):
    parsed_agent_config = {}
    for agent_name, agent_config in dict(alert_config).items():
        particular_agent_config = dict(agent_config)
        parsed_agent_config[agent_name] = {}
        for new_key in particular_agent_config:
            parsed_agent_config[agent_name][new_key] = []
            for element in particular_agent_config[new_key]:
                elements = element.split(".")
                for element in elements:
                    if element not in pipeline_config.cn.values():
                        logger.warning(
                            f"Field in agent configuration for {alert_type}: {element} is not registered field in pipeline.yaml"
                        )
                parsed_agent_config[agent_name][new_key].append(elements[-1])

    return parsed_agent_config, alert_config


def load_agent_configs():
    alert_agents_config = {
        alert_type: load_config(dataset_config_file, alert_type)
        for alert_type, dataset_config_file in DATASET_FILE_MAP.items()
    }
    alert_agents_config = {
        alert_type: load_agent_config(alert_type, config)
        for alert_type, config in alert_agents_config.items()
        if config
    }
    return alert_agents_config


class Pipeline:
    def __init__(self):
        self.config = None
        self.cn = None
        self.load_configs()

    def load_configs(self):
        config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "pipeline", "pipeline.yaml"))
        self.config = config.PIPELINE
        self.cn = config.PAYLOAD_KEYS

    def reload_pipeline_config(self):
        self.load_configs()


service_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))
pipeline_config = Pipeline()

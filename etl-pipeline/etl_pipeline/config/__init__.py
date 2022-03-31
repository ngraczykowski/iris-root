import os

from omegaconf import OmegaConf

from etl_pipeline.logger import get_logger

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
logger = get_logger("Loading Config")


def load_agent_config(alert_config):
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
                            f"Field in agent configuration for: {element} is not registered field in pipeline.yaml"
                        )
                parsed_agent_config[agent_name][new_key].append(elements[-1])

    return parsed_agent_config, alert_config


def load_agent_configs():
    alert_agents_config = {
        "alert_type": OmegaConf.load(os.path.join(CONFIG_APP_DIR, "agents", "agents.yaml"))
    }
    alert_agents_config = {
        alert_type: load_agent_config(config)
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

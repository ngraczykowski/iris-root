import os
import pathlib
from typing import Any, Optional

import yaml

from agent_base.utils.config.agent_config import AgentConfig, ConfigurationException
from agent_base.utils.config.agent_config_loader import AgentConfigLoader

CONFIG_DIRS = (pathlib.Path("./config"),)


class Config:
    def __init__(
        self,
        configuration_dirs=CONFIG_DIRS,
        required: bool = False,
        env_configuration_dir_key: str = "AGENT_CONFIGURATION_DIR",
    ):
        self.configuration_dirs = (
            *self._get_from_environment(env_configuration_dir_key),
            *configuration_dirs,
        )
        self.application_config = self.load_yaml_config("application.yaml", required=required)
        self._load_agent_config()

    def _load_agent_config(self):
        if self.application_config:
            self.agent_config = AgentConfigLoader(self.application_config).load()
        else:
            self.agent_config = AgentConfig()

    @staticmethod
    def _get_from_environment(configuration_dir_key: str):
        path = os.environ.get(configuration_dir_key)
        if path:
            yield pathlib.Path(path)

    def get_config_path(self, config_file_name: str, required=False) -> Optional[pathlib.Path]:
        for configuration_dir in self.configuration_dirs:
            configuration_path = configuration_dir / config_file_name
            if configuration_path.exists():
                return configuration_path

        if required:
            raise ConfigurationException(f"Configuration file {config_file_name} missing")
        else:
            return None

    def load_yaml_config(self, config_file_name: str, required=False) -> Optional[Any]:
        configuration_path = self.get_config_path(config_file_name, required)
        if not configuration_path:
            return None

        with configuration_path.open("rt") as config_file:
            return yaml.load(config_file, Loader=yaml.FullLoader)

    def reload(self):
        self.application_config = self.load_yaml_config("application.yaml", required=True)
        self._load_agent_config()

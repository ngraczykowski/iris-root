import pathlib
from typing import Any, Optional

import yaml


class ConfigurationException(Exception):
    pass


class Config:
    def __init__(self, configuration_dirs=(pathlib.Path("./config"),), required=False):
        self.configuration_dirs = configuration_dirs
        self.application_config = self.load_yaml_config(
            "application.yaml", required=required
        )

    def get_config_path(
        self, config_file_name: str, required=False
    ) -> Optional[pathlib.Path]:
        for configuration_dir in self.configuration_dirs:
            configuration_path = configuration_dir / config_file_name
            if configuration_path.exists():
                return configuration_path

        if required:
            raise ConfigurationException(
                f"Configuration file {config_file_name} missing"
            )
        else:
            return None

    def load_yaml_config(self, config_file_name: str, required=False) -> Optional[Any]:
        configuration_path = self.get_config_path(config_file_name, required)
        if not configuration_path:
            return None

        with configuration_path.open("rt") as config_file:
            return yaml.load(config_file, Loader=yaml.FullLoader)

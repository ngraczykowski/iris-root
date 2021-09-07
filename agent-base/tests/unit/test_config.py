import os
import pathlib

import pytest

from agent_base.utils.config import Config

DEFAULT_DIR = pathlib.Path("./config")


def test_config_without_arguments():
    config = Config()
    assert config.configuration_dirs == (DEFAULT_DIR,)


def test_config_with_configuration_dir():
    config_dir = pathlib.Path("./other_config_dir")
    config = Config((config_dir,))
    assert config.configuration_dirs == (config_dir,)


def test_config_with_env_variable():
    config_dir = "./other_config_dir"
    os.environ["AGENT_TEST_CONFIGURATION_DIR"] = config_dir
    config = Config(env_configuration_dir_key="AGENT_TEST_CONFIGURATION_DIR")
    assert config.configuration_dirs == (pathlib.Path(config_dir), DEFAULT_DIR)


def test_application_required_config():
    config_dir = pathlib.Path("./non_existing_config_dir")
    with pytest.raises(Exception):
        Config((config_dir,), required=True)

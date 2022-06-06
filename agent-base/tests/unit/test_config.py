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


@pytest.fixture
def setup_and_tear_down():
    os.environ.pop("GRPC_PORT", None)
    yield
    os.environ.pop("GRPC_PORT", None)


def test_config_from_env(setup_and_tear_down):
    default_grpc_port = 9090
    new_grpc_port = 5000
    config = Config()
    assert config.application_config["agent"]["grpc"]["port"] == default_grpc_port
    os.environ["GRPC_PORT"] = str(new_grpc_port)
    config = Config()
    assert config.agent_config.agent_grpc_service.grpc_port == new_grpc_port

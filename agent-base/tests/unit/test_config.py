import os
import pathlib

import pytest

from agent_base.utils import Config, ConfigurationException

DEFAULT_DIR = pathlib.Path("./config")
DEFAULT_GRPC_PORT = 9090


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
    os.environ.pop("AGENT_PROCESSES", None)
    yield
    os.environ.pop("GRPC_PORT", None)
    os.environ.pop("AGENT_PROCESSES", None)


def test_config_default():
    config = Config()
    assert config.agent_config.agent_grpc_service
    assert config.agent_config.consul
    assert config.agent_config.messaging
    assert config.agent_config.rabbitmq
    assert config.agent_config.uds


def test_config_from_env(setup_and_tear_down):
    config_without_grpc_port_dir = "tests/resources/config"

    # no param in application.yaml nor in env variables
    with pytest.raises(ConfigurationException):
        Config((pathlib.Path(config_without_grpc_port_dir),))

    new_grpc_port = 5000
    os.environ["GRPC_PORT"] = str(new_grpc_port)

    # param from env is superior to in this from application.yaml:
    config = Config()
    assert config.agent_config.agent_grpc_service.grpc_port == new_grpc_port

    os.environ.pop("GRPC_PORT")

    # when param not in env take from application.yaml:
    config = Config()
    assert config.agent_config.agent_grpc_service.grpc_port == DEFAULT_GRPC_PORT


def test_config_reload(setup_and_tear_down):
    new_grpc_port = 5000
    os.environ["GRPC_PORT"] = str(DEFAULT_GRPC_PORT)
    os.environ["AGENT_PROCESSES"] = "2"
    config = Config((pathlib.Path("tests/resources/config"),))
    assert config.agent_config.agent_grpc_service.grpc_port == DEFAULT_GRPC_PORT
    os.environ["GRPC_PORT"] = str(new_grpc_port)
    config.reload()
    assert config.agent_config.agent_grpc_service.grpc_port == new_grpc_port

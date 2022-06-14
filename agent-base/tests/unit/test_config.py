import json
import os
import pathlib

import pytest

from agent_base.utils import Config, ConfigurationException

DEFAULT_DIR = pathlib.Path("./config")
DEFAULT_GRPC_PORT = 9090
CONFIG_WITH_ENV_ONLY_PATH = pathlib.Path("tests/resources/config")
with open(CONFIG_WITH_ENV_ONLY_PATH / "env_vars_to_values.json") as file:
    ENV_VARS_TO_VALUES = json.load(file)


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


def test_config_default():
    config = Config()
    assert config.application_config.agent_grpc_service
    assert config.application_config.consul
    assert config.application_config.messaging
    assert config.application_config.rabbitmq
    assert config.application_config.uds


@pytest.fixture
def setup_and_tear_down_agent_service():
    # just cleaning pre and after test

    os.environ.pop("GRPC_PORT", None)
    os.environ.pop("AGENT_PROCESSES", None)
    yield
    os.environ.pop("GRPC_PORT", None)
    os.environ.pop("AGENT_PROCESSES", None)


def test_config_loading_order(setup_and_tear_down_agent_service):
    # no param in application.yaml nor set in env variables
    with pytest.raises(ConfigurationException):
        Config((pathlib.Path(CONFIG_WITH_ENV_ONLY_PATH),))

    new_grpc_port = 5000
    os.environ["GRPC_PORT"] = str(new_grpc_port)

    # param from env is superior to in this from application.yaml:
    config = Config()
    assert config.application_config.agent_grpc_service.grpc_port == new_grpc_port

    # when param not in env take from application.yaml:
    os.environ.pop("GRPC_PORT")
    config = Config()
    assert config.application_config.agent_grpc_service.grpc_port == DEFAULT_GRPC_PORT


def test_config_reload(setup_and_tear_down_agent_service):
    new_grpc_port = 5000
    os.environ["GRPC_PORT"] = str(DEFAULT_GRPC_PORT)
    os.environ["AGENT_PROCESSES"] = "2"
    config = Config((CONFIG_WITH_ENV_ONLY_PATH,))
    assert config.application_config.agent_grpc_service.grpc_port == DEFAULT_GRPC_PORT
    os.environ["GRPC_PORT"] = str(new_grpc_port)
    config.reload()
    assert config.application_config.agent_grpc_service.grpc_port == new_grpc_port


@pytest.fixture
def setup_and_tear_down_all():
    for var, value in ENV_VARS_TO_VALUES.items():
        os.environ[var] = str(value)
    yield
    for var in ENV_VARS_TO_VALUES:
        os.environ.pop(var)


def test_config_load_from_env_only(setup_and_tear_down_all):
    config = Config((CONFIG_WITH_ENV_ONLY_PATH,))
    app_config = config.application_config
    assert app_config.agent_grpc_service.grpc_port == ENV_VARS_TO_VALUES["GRPC_PORT"]
    assert app_config.agent_grpc_service.processes == ENV_VARS_TO_VALUES["AGENT_PROCESSES"]

    assert app_config.rabbitmq.host == ENV_VARS_TO_VALUES["RABBITMQ_HOST"]
    assert app_config.rabbitmq.port == ENV_VARS_TO_VALUES["RABBITMQ_PORT"]
    assert app_config.rabbitmq.login == ENV_VARS_TO_VALUES["RABBITMQ_USERNAME"]
    assert app_config.rabbitmq.password == ENV_VARS_TO_VALUES["RABBITMQ_PASSWORD"]
    assert app_config.rabbitmq.virtualhost == ENV_VARS_TO_VALUES["RABBITMQ_VIRTUALHOST"]

    assert app_config.messaging.request.exchange == ENV_VARS_TO_VALUES["REQUEST_EXCHANGE"]
    assert app_config.messaging.request.routing_key == ENV_VARS_TO_VALUES["REQUEST_ROUTING_KEY"]
    assert app_config.messaging.request.queue_name == ENV_VARS_TO_VALUES["REQUEST_QUEUE_NAME"]

    assert app_config.messaging.response.exchange == ENV_VARS_TO_VALUES["RESPONSE_EXCHANGE"]
    assert app_config.messaging.response.routing_key == ENV_VARS_TO_VALUES["RESPONSE_ROUTING_KEY"]

    assert app_config.uds.address == ENV_VARS_TO_VALUES["UDS_ADDRESS"]
    assert app_config.uds.timeout == ENV_VARS_TO_VALUES["UDS_TIMEOUT"]

import pathlib

import pytest

from agent_base.agent import Agent
from agent_base.utils import Config


def test_agent_without_config():
    agent = Agent()
    assert agent.config
    assert list(agent.config.configuration_dirs) == [pathlib.Path("./config")]


def test_agent_with_config():
    config = Config([pathlib.Path("./some_dir")])
    agent = Agent(config)
    assert agent.config


def test_agent_resolve():
    with pytest.raises(NotImplementedError):
        Agent().resolve()

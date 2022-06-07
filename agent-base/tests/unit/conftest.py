import pytest

from agent_base.utils.config import Config


@pytest.fixture()
def local_config():
    yield Config()

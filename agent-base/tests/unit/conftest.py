import pytest

from agent_base.utils import Config


@pytest.fixture()
def local_config():
    yield Config()

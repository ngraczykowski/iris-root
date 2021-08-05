import pathlib

import pytest

from agent_base.utils import Config


@pytest.fixture()
def local_config():
    configuration_path = pathlib.Path("./config/application.yaml")
    configuration_path.symlink_to("application.local.yaml")
    try:
        yield Config([configuration_path.parent])
    finally:
        configuration_path.unlink()

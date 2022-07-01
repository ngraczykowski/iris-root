import time

import pytest

ESTIMATED_CONTAINERS_RUN_TIMEOUT = 20


@pytest.fixture(scope="package", autouse=True)
def wait_for_all_containers():
    time.sleep(ESTIMATED_CONTAINERS_RUN_TIMEOUT)
    yield

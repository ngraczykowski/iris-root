import pytest


def pytest_addoption(parser):
    parser.addoption("--without-rabbitmq", action="store_true", default=False)


def pytest_configure(config):
    config.addinivalue_line("markers", "rabbitmq: mark test that needs spark")


def pytest_collection_modifyitems(config, items):
    if config.getoption("--without-rabbitmq"):
        skip_rabbitmq = pytest.mark.skip(reason="rabbitmq disabled")
        for item in items:
            if "rabbitmq" in item.keywords:
                item.add_marker(skip_rabbitmq)

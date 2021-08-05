import asyncio
import contextlib
import functools
import multiprocessing
import sys

from company_name.main import main as run_agent

from tests.integration.mocks import DataSourceMock


@contextlib.contextmanager
def run_in_process(f):
    p = multiprocessing.Process(target=f, args=())
    p.start()
    try:
        yield
    except Exception as err:
        print(repr(err))
        raise
    finally:
        p.kill()
        p.join()
        sys.stdout.flush()
        sys.stderr.flush()


@contextlib.contextmanager
def run_agent_in_process():
    with run_in_process(run_agent):
        yield


def run_data_source_mock(config, alerts):
    loop = asyncio.get_event_loop()
    mock = DataSourceMock(config["grpc"]["client"]["data-source"]["address"])
    mock.alerts = alerts
    loop.create_task(mock.start())
    try:
        loop.run_forever()
    finally:
        sys.stdout.flush()
        loop.run_until_complete(mock.stop())


@contextlib.contextmanager
def run_data_source_mock_in_process(config, alerts):
    with run_in_process(functools.partial(run_data_source_mock, config, alerts)):
        yield

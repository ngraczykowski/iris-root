import subprocess
import time

import pytest
from grpc_health.v1.health_pb2 import HealthCheckRequest, HealthCheckResponse
from grpc_health.v1.health_pb2_grpc import HealthStub

from s8_python_network.grpc_channel.grpc_channel import SSLCredentials, get_channel
from s8_python_network.utils import kill_process_on_the_port, kill_recursive

TIMEOUT = 0.5


def test_grpc_insecure():
    port = 9091
    kill_process_on_the_port(port)
    time.sleep(TIMEOUT * 2)
    server_process = subprocess.Popen("python -m tests.test_grpc.server_mocks.insecure".split())
    time.sleep(TIMEOUT)
    channel = get_channel(f"localhost:{port}", asynchronous=False)
    assert channel
    health_stub = HealthStub(channel)
    resp = health_stub.Check(HealthCheckRequest())
    assert HealthCheckResponse.ServingStatus.Name(resp.status) == "SERVING"
    kill_recursive(server_process.pid)
    time.sleep(TIMEOUT)


def test_grpc_secure():
    port = 9092
    kill_process_on_the_port(port)
    time.sleep(TIMEOUT * 2)
    server_process = subprocess.Popen("python -m tests.test_grpc.server_mocks.secure".split())
    time.sleep(TIMEOUT)
    ssl_files_dir = "tests/test_grpc/ssl_example"

    ssl_credentials = SSLCredentials(
        f"{ssl_files_dir}/ca.pem",
        f"{ssl_files_dir}/client-key.pem",
        f"{ssl_files_dir}/client.pem",
    )
    channel = get_channel(
        f"localhost:{port}", asynchronous=False, ssl=True, ssl_credentials=ssl_credentials
    )
    assert channel
    health_stub = HealthStub(channel)
    resp = health_stub.Check(HealthCheckRequest())
    assert HealthCheckResponse.ServingStatus.Name(resp.status) == "SERVING"
    kill_recursive(server_process.pid)
    time.sleep(TIMEOUT)


@pytest.mark.asyncio
async def test_grpc_aio_insecure():
    port = 9093
    kill_process_on_the_port(port)
    time.sleep(TIMEOUT * 2)
    server_process = subprocess.Popen("python -m tests.test_grpc.server_mocks.aio_insecure".split())
    time.sleep(TIMEOUT)
    channel = get_channel(f"localhost:{port}", asynchronous=True)
    assert channel
    health_stub = HealthStub(channel)
    resp = await health_stub.Check(HealthCheckRequest())
    assert HealthCheckResponse.ServingStatus.Name(resp.status) == "SERVING"
    kill_recursive(server_process.pid)
    time.sleep(TIMEOUT)


@pytest.mark.asyncio
async def test_grpc_aio_secure():
    port = 9094
    kill_process_on_the_port(port)
    time.sleep(TIMEOUT * 2)
    server_process = subprocess.Popen("python -m tests.test_grpc.server_mocks.aio_secure".split())
    time.sleep(TIMEOUT)
    ssl_files_dir = "tests/test_grpc/ssl_example"

    ssl_credentials = SSLCredentials(
        f"{ssl_files_dir}/ca.pem",
        f"{ssl_files_dir}/client-key.pem",
        f"{ssl_files_dir}/client.pem",
    )
    channel = get_channel(
        f"localhost:{port}", asynchronous=True, ssl=True, ssl_credentials=ssl_credentials
    )
    assert channel
    health_stub = HealthStub(channel)
    resp = await health_stub.Check(HealthCheckRequest())
    assert HealthCheckResponse.ServingStatus.Name(resp.status) == "SERVING"
    kill_recursive(server_process.pid)
    time.sleep(TIMEOUT)

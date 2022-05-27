import subprocess
import time

import pytest
from grpc_health.v1.health_pb2 import HealthCheckRequest, HealthCheckResponse
from grpc_health.v1.health_pb2_grpc import HealthStub

from s8_python_network.grpc_channel.grpc_channel import get_channel
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


@pytest.mark.skip
def test_grpc_secure():
    port = 9092
    kill_process_on_the_port(port)
    time.sleep(TIMEOUT * 2)
    server_process = subprocess.Popen(
        "python -m s8_python_network.grpc_channel.server_mocks.secure".split()
    )
    time.sleep(TIMEOUT)
    channel = get_channel(f"localhost:{port}", asynchronous=False)
    assert channel
    health_stub = HealthStub(channel)
    resp = health_stub.Check(HealthCheckRequest())
    assert HealthCheckResponse.ServingStatus.Name(resp.status) == "SERVING"
    kill_recursive(server_process.pid)
    time.sleep(TIMEOUT)

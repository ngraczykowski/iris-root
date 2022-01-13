import pathlib
import subprocess
import time
import unittest

import grpc
import psutil
from agent_base.utils import Config
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2 import (
    CompareOrganizationNamesRequest,
)
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2_grpc import (
    OrganizationNameAgentStub,
)

PORT = Config().load_yaml_config("application.grpc.yaml")["agent"]["grpc"]["port"]
GRPC_ADDRESS = f"localhost:{PORT}"


TIMEOUT_SEC = 5
TEST_CASES = [
    {
        "data": {"alerted_party_names": ["ABC"], "watchlist_party_names": ["ABCDE"]},
        "expected": {"solution": "MATCH"},
    },
    {
        "data": {"alerted_party_names": ["ABC"], "watchlist_party_names": ["DEF"]},
        "expected": {"solution": "NO_MATCH"},
    },
    {
        "data": {"alerted_party_names": [], "watchlist_party_names": []},
        "expected": {"solution": "NO_DATA"},
    },
]


class ServerIsNotRunning(Exception):
    pass


def grpc_server_on(channel) -> bool:
    # https://stackoverflow.com/questions/45759491/how-to-know-if-a-grpc-server-is-available/61384353#61384353
    try:
        grpc.channel_ready_future(channel).result(timeout=TIMEOUT_SEC)
        return True
    except grpc.FutureTimeoutError:
        return False


def wait_for_server():
    channel = grpc.insecure_channel(GRPC_ADDRESS)
    counter = 0
    while counter < 10 and not grpc_server_on(channel):
        time.sleep(5)
        counter += 1
    if counter == 10:
        raise ServerIsNotRunning("Check server")


def kill_recursive(process_pid: int):
    process = psutil.Process(process_pid)
    for proc in process.children(recursive=True):
        proc.kill()
    process.kill()


def kill_process_on_the_port():
    kill = subprocess.Popen(
        "kill -9 $(netstat -ltnp | "
        f"grep -w :{PORT} | "
        "awk '{ print $7 }' | "
        "grep -o '[0-9]\\+' )".split()
    )
    kill.wait()


class TestServer(unittest.TestCase):
    def setUp(self):
        self.config_path = pathlib.Path("./config/application.yaml")
        self.config_path.symlink_to("application.grpc.yaml")  # link to file from the same dir
        kill_process_on_the_port()
        self.server_process = subprocess.Popen("python -m company_name.main -v --grpc".split())
        wait_for_server()

    def tearDown(self):
        kill_recursive(self.server_process.pid)
        self.config_path.unlink()

    def test_check_grpc_response(self):
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        stub = OrganizationNameAgentStub(channel)
        for test_case in TEST_CASES:
            request = CompareOrganizationNamesRequest(**test_case["data"])
            resp = stub.CompareOrganizationNames(request)
            self.assertEqual(test_case["expected"]["solution"], resp.solution)

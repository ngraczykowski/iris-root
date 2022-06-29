import subprocess
import time
import unittest

from s8_python_network.grpc_channel import get_channel
from s8_python_network.utils import grpc_server_on, kill_process_on_the_port, kill_recursive
from silenteight.agent.companynamesurrounding.v1.api.company_name_surrounding_agent_pb2 import (
    CheckCompanyNameSurroundingRequest,
)
from silenteight.agent.companynamesurrounding.v1.api.company_name_surrounding_agent_pb2_grpc import (
    CompanyNameSurroundingAgentStub,
)


class ServerIsNotRunning(Exception):
    pass


TIMEOUT_SEC = 0.5
TEST_CASES = [
    {
        "expected": 0,
        "data": {"names": []},
    },
    {
        "expected": 1,
        "data": {"names": ["KGHM S.A."]},
    },
    {
        "expected": 2,
        "data": {"names": ["Silent Eight PTE LTD"]},
    },
    {
        "expected": 0,  # because of ap_names length > 1
        "data": {"names": ["Silent Eight PTE LTD", "KHGM S. A."]},
    },
]

PORT = 24806
GRPC_ADDRESS = f"localhost:{PORT}"


class TestServer(unittest.TestCase):
    @staticmethod
    def wait_for_server():
        channel = get_channel(GRPC_ADDRESS)
        counter = 0
        while counter < 10 and not grpc_server_on(channel, TIMEOUT_SEC):
            time.sleep(2)
            counter += 1
        if counter == 10:
            raise ServerIsNotRunning("Check server")

    def setUp(self):
        kill_process_on_the_port(PORT)
        self.server_process = subprocess.Popen("python company_name_surrounding -v --grpc".split())
        self.wait_for_server()

    def tearDown(self):
        kill_recursive(self.server_process.pid)

    def test_check_grpc_response(self):
        channel = get_channel(GRPC_ADDRESS)
        stub = CompanyNameSurroundingAgentStub(channel)
        for test_case in TEST_CASES:
            request = CheckCompanyNameSurroundingRequest(**test_case["data"])
            resp = stub.CheckCompanyNameSurrounding(request)
            self.assertEqual(test_case["expected"], resp.result)
            self.assertEqual(str(test_case["expected"]), resp.solution)

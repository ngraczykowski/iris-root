import subprocess
import time
import unittest

import grpc
from silenteight.agent.companynamesurrounding.v1.api.company_name_surrounding_agent_pb2 import (
    CheckCompanyNameSurroundingRequest,
)
from silenteight.agent.companynamesurrounding.v1.api.company_name_surrounding_agent_pb2_grpc import (
    CompanyNameSurroundingAgentStub,
)


class ServerIsNotRunning(Exception):
    pass


TIMEOUT_SEC = 5
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

PORT = 9090
GRPC_ADDRESS = f"localhost:{PORT}"


def grpc_server_on(channel) -> bool:
    # https://stackoverflow.com/questions/45759491/how-to-know-if-a-grpc-server-is-available/61384353#61384353
    try:
        grpc.channel_ready_future(channel).result(timeout=TIMEOUT_SEC)
        return True
    except grpc.FutureTimeoutError:
        return False


class TestServer(unittest.TestCase):
    @staticmethod
    def wait_for_server():
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        counter = 0
        while counter < 10 and not grpc_server_on(channel):
            time.sleep(5)
            counter += 1
        if counter == 10:
            raise ServerIsNotRunning("Check server")

    @staticmethod
    def kill_process_on_the_port():
        kill = subprocess.Popen(
            f"kill -9 $(netstat -ltnp | "
            "grep -w :{PORT} | "
            "awk '{ print $7 }' | "
            "grep -o '[0-9]\\+' )".split()
        )
        kill.wait()

    def setUp(self):
        self.kill_process_on_the_port()
        self.server_process = subprocess.Popen("python company_name_surrounding -v --grpc".split())
        self.wait_for_server()

    def tearDown(self):
        self.server_process.kill()

    def test_check_grpc_response(self):
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        stub = CompanyNameSurroundingAgentStub(channel)
        for test_case in TEST_CASES:
            request = CheckCompanyNameSurroundingRequest(**test_case["data"])
            resp = stub.CheckCompanyNameSurrounding(request)
            self.assertEqual(test_case["expected"], resp.result)
            self.assertEqual(str(test_case["expected"]), resp.solution)

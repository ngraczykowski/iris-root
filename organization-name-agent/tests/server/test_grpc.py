import subprocess
import time
import unittest

import grpc
from agent_base.utils import Config
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2 import (
    CompareOrganizationNamesRequest,
)
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2_grpc import (
    OrganizationNameAgentStub,
)

from tests.server.utils import (
    TEST_CASES,
    ServerIsNotRunning,
    grpc_server_on,
    kill_process_on_the_port,
    kill_recursive,
)

PORT = Config().load_yaml_config("application.yaml")["agent"]["grpc"]["port"]
GRPC_ADDRESS = f"localhost:{PORT}"
TIMEOUT_SEC = 0.5


def wait_for_server(address):
    channel = grpc.insecure_channel(address)
    counter = 0
    while counter < 10 and not grpc_server_on(channel, TIMEOUT_SEC):
        time.sleep(2)
        counter += 1
    if counter == 10:
        raise ServerIsNotRunning("Check server")


class TestServer(unittest.TestCase):
    def setUp(self):
        kill_process_on_the_port(PORT)
        self.server_process = subprocess.Popen("python -m company_name.main -v --grpc".split())
        wait_for_server(GRPC_ADDRESS)

    def tearDown(self):
        kill_recursive(self.server_process.pid)
        time.sleep(TIMEOUT_SEC)

    def test_check_grpc_response(self):
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        stub = OrganizationNameAgentStub(channel)
        for test_case in TEST_CASES:
            request = CompareOrganizationNamesRequest(**test_case["data"])
            resp = stub.CompareOrganizationNames(request)
            self.assertEqual(test_case["solution"], resp.solution)
            self.assertEqual(test_case["results_number"], len(resp.reason.results))
            if resp.reason.results:
                first_result = resp.reason.results[0]
                probability = first_result.solution_probability
                self.assertAlmostEqual(test_case["probability"], probability, places=2)

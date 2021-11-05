import subprocess
import time
import unittest

import grpc
from silenteight.agent.bankidentificationcodes.v1.api.bank_identification_codes_agent_pb2 import (
    CheckBankIdentificationCodesRequest,
)
from silenteight.agent.bankidentificationcodes.v1.api.bank_identification_codes_agent_pb2_grpc import (
    BankIdentificationCodesAgentStub,
)


class ServerIsNotRunning(Exception):
    pass


TIMEOUT_SEC = 5

TEST_CASES = [
    {
        "expected_solution": "MATCH",
        "expected_conclusion": "MatchingTextIsPartOfLongerSequenceReason",
        "data": {
            "altered_party_matching_field": " ABCDE OF FGHI JKL 2085846280FS\n",
            "watchlist_matching_text": "58462",
            "watchlist_type": "some type",
            "watchlist_search_codes": ["58462", "6106782"],
            "watchlist_bic_codes": [],
        },
        "field_to_check": "altered_party_matching_sequence",
        "field_to_check_expected_value": "2085846280FS",
    },
    {
        "expected_solution": "NO_MATCH",
        "expected_conclusion": "MatchingTextMatchesWlSearchCodeReason",
        "data": {
            "altered_party_matching_field": "Some Text123",
            "watchlist_matching_text": "Text",
            "watchlist_type": "some type",
            "watchlist_search_codes": ["Text123"],
            "watchlist_bic_codes": ["Another"],
        },
        "field_to_check": "watchlist_search_codes",
        "field_to_check_expected_value": ["TEXT123"],
    },
]

GRPC_ADDRESS = "localhost:9090"


def grpc_server_on(channel) -> bool:
    # https://stackoverflow.com/questions/45759491/how-to-know-if-a-grpc-server-is-available/61384353#61384353
    try:
        grpc.channel_ready_future(channel).result(timeout=TIMEOUT_SEC)
        return True
    except grpc.FutureTimeoutError:
        return False


class TestServer(unittest.TestCase):
    def wait_for_server(self):
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        counter = 0
        while counter < 10 and not grpc_server_on(channel):
            time.sleep(5)
            counter += 1
        if counter == 10:
            raise ServerIsNotRunning("Check server and if package is installed")

    def setUp(self):
        self.server_process = subprocess.Popen(
            "python bank_identification_codes_agent -v --grpc".split()
        )
        self.wait_for_server()

    def tearDown(self):
        self.server_process.kill()

    def test_check_grpc_response(self):
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        stub = BankIdentificationCodesAgentStub(channel)
        for test_case in TEST_CASES:
            request = CheckBankIdentificationCodesRequest(**test_case["data"])
            resp = stub.CheckBankIdentificationCodes(request)
            self.assertEqual(test_case["expected_solution"], resp.solution)
            self.assertEqual(test_case["expected_conclusion"], resp.reason.conclusion)
            self.assertEqual(
                test_case["field_to_check_expected_value"],
                getattr(resp.reason, test_case["field_to_check"]),
            )

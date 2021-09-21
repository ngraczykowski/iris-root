import grpc
from silenteight.agent.bankidentificationcodes.v1.api.bank_identification_codes_agent_pb2 import (
    CheckBankIdentificationCodesRequest,
)
from silenteight.agent.bankidentificationcodes.v1.api.bank_identification_codes_agent_pb2_grpc import (
    BankIdentificationCodesAgentStub,
)

# This test may be run to check if grpc service works properly. Make sure that port is correct.
# To use, first run bic agent grpc using:
# python bank_identification_codes_agent -v --grpc


test_cases = [
    {
        "solution": "MATCH",
        "conclusion": "MatchingTextIsPartOfLongerSequenceReason",
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
        "solution": "NO_MATCH",
        "conclusion": "MatchingTextMatchesWlSearchCodeReason",
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


def check_grpc_response():
    channel = grpc.insecure_channel("localhost:24805")
    stub = BankIdentificationCodesAgentStub(channel)
    for test_case in test_cases:
        request = CheckBankIdentificationCodesRequest(**test_case["data"])
        resp = stub.CheckBankIdentificationCodes(request)
        print(
            "Solution:\n",
            "  expected: {0}\n".format(test_case["solution"]),
            "  actual:   {0}".format(resp.solution),
        )
        print(
            "Conclusion: \n",
            "  expected: {0}\n".format(test_case["conclusion"]),
            "  actual:   {0}".format(resp.reason.conclusion),
        )
        print(
            "Field {0}:\n".format(test_case["field_to_check"]),
            "  expected: {0}\n".format(test_case["field_to_check_expected_value"]),
            "  actual:   {0}".format(getattr(resp.reason, test_case["field_to_check"])),
        )
        print("\n")


if __name__ == "__main__":
    check_grpc_response()

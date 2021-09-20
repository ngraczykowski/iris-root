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


def check_grpc_response():
    request = CheckBankIdentificationCodesRequest(
        altered_party_matching_field=" ABCDE OF FGHI JKL 2085846280FS\n",
        watchlist_matching_text="58462",
        watchlist_type="some type",
        watchlist_search_codes=["58462", "6106782"],
        watchlist_bic_codes=[],
    )
    channel = grpc.insecure_channel("localhost:24805")
    stub = BankIdentificationCodesAgentStub(channel)
    resp = stub.CheckBankIdentificationCodes(request)
    print("Result status: {0}".format("OK" if resp.solution == "MATCH" else "INVALID"))
    print("Expected solution: MATCH", ", get solution: {0}".format(resp.solution))
    print(
        "Expected conclusion: MatchingTextIsPartOfLongerSequenceReason",
        ", get solution: {0}".format(resp.reason.conclusion),
    )


if __name__ == "__main__":
    check_grpc_response()

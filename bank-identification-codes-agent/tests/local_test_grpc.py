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
    print("Solution:\n", "expected: MATCH\n", "actual:   {0}\n".format(resp.solution))
    print(
        "Conclusion: \n",
        "expected: MatchingTextIsPartOfLongerSequenceReason\n",
        "actual:   {0}\n".format(resp.reason.conclusion),
    )
    print(
        "Field altered_party_matching_sequence:\n",
        "expected: 2085846280FS \n",
        "actual:   {0}\n".format(resp.reason.altered_party_matching_sequence),
    )


if __name__ == "__main__":
    check_grpc_response()

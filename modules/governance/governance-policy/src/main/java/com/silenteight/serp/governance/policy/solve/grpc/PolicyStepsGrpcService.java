package com.silenteight.serp.governance.policy.solve.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.governance.v1.api.GetSolutionsRequest;
import com.silenteight.proto.governance.v1.api.GetSolutionsResponse;
import com.silenteight.proto.governance.v1.api.PolicyStepsGovernanceGrpc;
import com.silenteight.serp.governance.policy.solve.SolveUseCase;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

@RequiredArgsConstructor
class PolicyStepsGrpcService
    extends PolicyStepsGovernanceGrpc.PolicyStepsGovernanceImplBase {

  @NonNull
  private SolveUseCase solveUseCase;

  @Override
  public void getSolutions(
      GetSolutionsRequest request, StreamObserver<GetSolutionsResponse> responseObserver) {
    try {
      responseObserver.onNext(solveUseCase.solve(request));
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      Status status = Status
          .newBuilder()
          .setCode(Code.INTERNAL_VALUE)
          .setMessage("Unhandled error occurred in Governance while calling 'getSolutions'.")
          .build();

      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }
}

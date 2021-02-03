package com.silenteight.serp.governance.policy.solve.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.governance.api.v1.PolicyStepsGovernanceGrpc;
import com.silenteight.governance.api.v1.SolveFeaturesRequest;
import com.silenteight.governance.api.v1.SolveFeaturesResponse;
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
  public void solveFeatures(
      SolveFeaturesRequest request, StreamObserver<SolveFeaturesResponse> responseObserver) {
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

package com.silenteight.serp.governance.policy.solve.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.solve.SolveUseCase;
import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

@RequiredArgsConstructor
class PolicyStepsGrpcService
    extends PolicyStepsSolvingGrpc.PolicyStepsSolvingImplBase {

  @NonNull
  private SolveUseCase solveUseCase;

  @Override
  public void batchSolveFeatures(
      BatchSolveFeaturesRequest request,
      StreamObserver<BatchSolveFeaturesResponse> responseObserver) {

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

package com.silenteight.serp.governance.policy.solve.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.signature.InvalidInputException;
import com.silenteight.serp.governance.policy.solve.SolveUseCase;
import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@Slf4j
@RequiredArgsConstructor
class PolicyStepsGrpcService
    extends PolicyStepsSolvingGrpc.PolicyStepsSolvingImplBase {

  @NonNull
  private final SolveUseCase solveUseCase;

  @Override
  public void batchSolveFeatures(
      BatchSolveFeaturesRequest request,
      StreamObserver<BatchSolveFeaturesResponse> responseObserver) {

    try {
      responseObserver.onNext(solveUseCase.solve(request));
      responseObserver.onCompleted();
    } catch (InvalidInputException e) {
      log.error(e.getMessage());
      Status status = createStatus(Code.INVALID_ARGUMENT_VALUE, e.getMessage());
      responseObserver.onError(toStatusRuntimeException(status));
    } catch (RuntimeException e) {
      log.error(e.getMessage());
      Status status = createStatus(
          Code.INTERNAL_VALUE,
          "Unhandled error occurred in Governance while calling 'batchSolveFeatures'.");
      responseObserver.onError(toStatusRuntimeException(status));
    }
  }

  @NotNull
  private Status createStatus(int invalidArgumentValue, String message) {
    return Status
        .newBuilder()
        .setCode(invalidArgumentValue)
        .setMessage(message)
        .build();
  }
}

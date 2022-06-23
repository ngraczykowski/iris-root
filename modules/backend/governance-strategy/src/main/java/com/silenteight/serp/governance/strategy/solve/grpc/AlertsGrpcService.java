package com.silenteight.serp.governance.strategy.solve.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.strategy.solve.SolveAlertUseCase;
import com.silenteight.solving.api.v1.AlertsSolvingGrpc;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@RequiredArgsConstructor
class AlertsGrpcService
    extends AlertsSolvingGrpc.AlertsSolvingImplBase {

  @NonNull
  private final SolveAlertUseCase solveAlertUseCase;

  @Override
  public void batchSolveAlerts(
      BatchSolveAlertsRequest request, StreamObserver<BatchSolveAlertsResponse> responseObserver) {

    try {
      responseObserver.onNext(solveAlertUseCase.solve(request));
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      Status status = Status
          .newBuilder()
          .setCode(Code.INTERNAL_VALUE)
          .setMessage("Unhandled error occurred in Governance while calling 'batchSolveAlerts'.")
          .build();

      responseObserver.onError(toStatusRuntimeException(status));
    }
  }
}

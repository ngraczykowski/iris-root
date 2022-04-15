package com.silenteight.adjudication.engine.mock.governance;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.AlertsSolvingGrpc;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static java.util.Arrays.asList;

@GrpcService
@RequiredArgsConstructor
@Profile("mockgovernance")
class MockAlertsSolvingGrpc extends AlertsSolvingGrpc.AlertsSolvingImplBase {

  private final List<String> availableSolutions =
      asList("Manual Investigation");

  @Override
  public void batchSolveAlerts(
      BatchSolveAlertsRequest request,
      StreamObserver<BatchSolveAlertsResponse> responseObserver) {
    BatchSolveAlertsResponse.Builder builder = BatchSolveAlertsResponse.newBuilder();
    for (int i = 0; i < request.getAlertsCount(); i++) {
      var alert = request.getAlertsList().get(i);
      builder.addSolutions(SolveAlertSolutionResponse.newBuilder()
          .setAlertName(alert.getName())
          .setAlertSolution(availableSolutions.get(i % availableSolutions.size()))
          .build());
    }

    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}

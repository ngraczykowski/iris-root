package com.silenteight.adjudication.engine.governance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.AlertsSolvingGrpc.AlertsSolvingBlockingStub;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class AlertSolvingClient {

  private final AlertsSolvingBlockingStub stub;

  private final Duration timeout;

  @Timed
  public BatchSolveAlertsResponse batchSolveAlerts(BatchSolveAlertsRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isDebugEnabled()) {
      log.debug("Requesting alert solutions: deadline={}, request={}", deadline, request);
    }

    var response = stub
        .withDeadline(deadline)
        .batchSolveAlerts(request);

    if (response.getSolutionsCount() < request.getAlertsCount()) {
      log.error("Received less than expected number of alert solutions, solving all as"
              + " ACTION_INVESTIGATE: solutionCount={}, alertCount={}, authority={}",
          response.getSolutionsCount(), request.getAlertsCount(), stub.getChannel().authority());

      var noDecisionSolutions = request.getAlertsList().stream()
          .map(alert -> SolveAlertSolutionResponse
              .newBuilder()
              .setAlertName(alert.getName())
              .setAlertSolution("ACTION_INVESTIGATE")
              .build())
          .collect(Collectors.toList());

      response = BatchSolveAlertsResponse.newBuilder()
          .addAllSolutions(noDecisionSolutions)
          .build();
    }

    if (log.isDebugEnabled()) {
      log.debug("Received alert solutionCount={}", response.getSolutionsCount());
    }

    return response;
  }
}

package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.AlertsSolvingGrpc.AlertsSolvingBlockingStub;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
class AlertSolvingClient {

  private final AlertsSolvingBlockingStub alertsSolvingBlockingStub;

  private final Duration timeout;

  public BatchSolveAlertsResponse batchSolveAlerts(BatchSolveAlertsRequest request) {
    // TODO(ahaczewski): Add logging and checks for expected and valid response.
    return alertsSolvingBlockingStub
        .withDeadlineAfter(timeout.toMillis(), TimeUnit.MILLISECONDS)
        .batchSolveAlerts(request);
  }
}

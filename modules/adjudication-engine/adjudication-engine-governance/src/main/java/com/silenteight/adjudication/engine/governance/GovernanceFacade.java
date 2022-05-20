package com.silenteight.adjudication.engine.governance;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GovernanceFacade {

  private final AlertSolvingClient alertSolvingClient;
  private final PolicyStepsClient policyStepsClient;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public BatchSolveAlertsResponse batchSolveAlerts(BatchSolveAlertsRequest request) {
    return alertSolvingClient.batchSolveAlerts(request);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public BatchSolveFeaturesResponse batchSolveFeatures(BatchSolveFeaturesRequest request) {
    return policyStepsClient.batchSolveFeatures(request);
  }
}

package com.silenteight.adjudication.engine.governance;

import lombok.RequiredArgsConstructor;

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

  public BatchSolveAlertsResponse batchSolveAlerts(BatchSolveAlertsRequest request) {
    return alertSolvingClient.batchSolveAlerts(request);
  }

  public BatchSolveFeaturesResponse batchSolveFeatures(BatchSolveFeaturesRequest request) {
    return policyStepsClient.batchSolveFeatures(request);
  }
}

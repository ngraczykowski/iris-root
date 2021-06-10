package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc.PolicyStepsSolvingBlockingStub;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
class GovernancePolicyStepsApiClient {

  private final PolicyStepsSolvingBlockingStub policyStepsStub;

  private final Duration timeout;

  public BatchSolveFeaturesResponse batchSolveFeatures(BatchSolveFeaturesRequest request) {
    return policyStepsStub
        .withDeadlineAfter(timeout.toMillis(), TimeUnit.MILLISECONDS)
        .batchSolveFeatures(request);
  }
}

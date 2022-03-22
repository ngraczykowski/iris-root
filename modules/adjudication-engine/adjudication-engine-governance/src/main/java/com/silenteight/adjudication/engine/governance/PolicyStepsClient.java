package com.silenteight.adjudication.engine.governance;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc.PolicyStepsSolvingBlockingStub;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
class PolicyStepsClient {

  private final PolicyStepsSolvingBlockingStub policyStepsStub;

  private final Duration timeout;

  public BatchSolveFeaturesResponse batchSolveFeatures(BatchSolveFeaturesRequest request) {
    // TODO(ahaczewski): Add logging and checks for expected and valid response.
    return policyStepsStub
        .withDeadlineAfter(timeout.toMillis(), TimeUnit.MILLISECONDS)
        .batchSolveFeatures(request);
  }
}

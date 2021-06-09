package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc.PolicyStepsSolvingBlockingStub;

@RequiredArgsConstructor
class GovernancePolicyStepsApiClient {

  private final PolicyStepsSolvingBlockingStub policyStepsStub;

  public BatchSolveFeaturesResponse batchSolveFeatures(BatchSolveFeaturesRequest request) {
    return policyStepsStub.batchSolveFeatures(request);
  }
}

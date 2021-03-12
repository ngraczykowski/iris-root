package com.silenteight.serp.governance.policy.solve;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
class Step {

  private final FeatureVectorSolution solution;
  private final UUID stepId;
  private final Collection<FeatureLogic> featureLogics;

  boolean matchesFeatureValues(Map<String, String> featureValuesByName) {
    return featureLogics.stream().allMatch(featureLogic -> featureLogic.match(featureValuesByName));
  }

  SolveResponse getResponse() {
    return new SolveResponse(solution, stepId);
  }
}

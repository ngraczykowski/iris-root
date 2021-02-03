package com.silenteight.serp.governance.policy.solve;

import lombok.RequiredArgsConstructor;

import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
class Step {

  private final FeatureVectorSolution solution;
  private final UUID stepId;
  private final Collection<FeatureLogic> featureLogics;

  boolean matchesFeatureValues(Map<String, String> featureValuesByName) {
    return featureLogics
        .stream()
        .anyMatch(featureLogic -> matchesFeatureValues(featureLogic, featureValuesByName));
  }

  private boolean matchesFeatureValues(
      FeatureLogic featureLogic, Map<String, String> featureValuesByName) {

    return featureLogic
        .getFeatures()
        .stream()
        .filter(feature -> feature.matchesFeatureValues(featureValuesByName))
        .limit(featureLogic.getCount())
        .count() == featureLogic.getCount();
  }

  SolveResponse getResponse() {
    return new SolveResponse(solution, stepId);
  }
}

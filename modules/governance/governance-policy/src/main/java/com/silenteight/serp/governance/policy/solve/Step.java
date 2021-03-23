package com.silenteight.serp.governance.policy.solve;

import lombok.Value;

import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import javax.annotation.concurrent.ThreadSafe;

import static java.util.List.copyOf;

@Value
@ThreadSafe
class Step {

  FeatureVectorSolution solution;
  UUID stepId;
  Collection<FeatureLogic> featureLogics;

  Step(FeatureVectorSolution solution, UUID stepId, Collection<FeatureLogic> featureLogics) {
    this.solution = solution;
    this.stepId = stepId;
    this.featureLogics = copyOf(featureLogics);
  }

  boolean matchesFeatureValues(Map<String, String> featureValuesByName) {
    return featureLogics.stream().allMatch(featureLogic -> featureLogic.match(featureValuesByName));
  }

  SolveResponse getResponse() {
    return new SolveResponse(solution, stepId);
  }
}

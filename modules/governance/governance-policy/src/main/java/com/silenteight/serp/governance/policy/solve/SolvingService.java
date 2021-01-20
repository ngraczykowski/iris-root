package com.silenteight.serp.governance.policy.solve;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import java.util.List;
import java.util.Map;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_NO_DECISION;

@Slf4j
public class SolvingService {

  private static final SolveResponse DEFAULT_SOLUTION =
      new SolveResponse(SOLUTION_NO_DECISION);

  public SolveResponse solve(List<Step> steps, Map<String, String> featureValuesByName) {
    SolveResponse response = getSolutionForValues(steps, featureValuesByName);
    //TODO(mmastylo): Send Configure Branch message to a queue
    return response;
  }

  private SolveResponse getSolutionForValues(
      List<Step> steps, Map<String, String> featureValuesByName) {

    return steps
        .stream()
        .filter(step -> step.matchesFeatureValues(featureValuesByName))
        .map(Step::getResponse)
        .peek(step -> log.debug("Using step: {}", step.getStepId()))
        .findFirst()
        .orElse(DEFAULT_SOLUTION);
  }
}

package com.silenteight.serp.governance.policy.solve;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import java.util.Map;

import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;

@Slf4j
public class SolvingService {

  private static final SolveResponse DEFAULT_SOLUTION =
      new SolveResponse(SOLUTION_NO_DECISION);

  public SolveResponse solve(
      StepsSupplier stepsSupplier, Map<String, String> featureValuesByName) {

    return stepsSupplier
        .get()
        .stream()
        .filter(step -> step.matchesFeatureValues(featureValuesByName))
        .map(Step::getResponse)
        .peek(step -> log.debug("Using step: {}", step.getStepId()))
        .findFirst()
        .orElse(DEFAULT_SOLUTION);
  }
}

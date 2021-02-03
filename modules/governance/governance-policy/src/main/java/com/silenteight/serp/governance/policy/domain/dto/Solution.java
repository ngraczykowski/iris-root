package com.silenteight.serp.governance.policy.domain.dto;

import com.silenteight.governance.api.v1.FeatureVectorSolution;

import java.util.stream.Stream;

public enum Solution {

  NO_DECISION(FeatureVectorSolution.SOLUTION_NO_DECISION),
  FALSE_POSITIVE(FeatureVectorSolution.SOLUTION_FALSE_POSITIVE),
  HINTED_FALSE_POSITIVE(FeatureVectorSolution.SOLUTION_HINTED_FALSE_POSITIVE),
  POTENTIAL_TRUE_POSITIVE(FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE),
  HINTED_POTENTIAL_TRUE_POSITIVE(FeatureVectorSolution.SOLUTION_HINTED_POTENTIAL_TRUE_POSITIVE);

  private final FeatureVectorSolution solutionNoDecision;

  Solution(FeatureVectorSolution solutionNoDecision) {
    this.solutionNoDecision = solutionNoDecision;
  }

  public static Solution of(FeatureVectorSolution solution) {
    return Stream.of(Solution.values())
        .filter(value -> value.solutionNoDecision.equals(solution))
        .findFirst()
        .orElseThrow(
            () -> new FeatureVectorSolutionInSolutionNotFoundException(solution.name()));
  }

  private static class FeatureVectorSolutionInSolutionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 917277213027711638L;

    public FeatureVectorSolutionInSolutionNotFoundException(String message) {
      super("Could not find solution for FeatureVectorSolution(" + message + ")");
    }
  }
}

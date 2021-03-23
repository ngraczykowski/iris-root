package com.silenteight.adjudication.engine.mock.governance;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.Arrays.asList;

class MockFeatureVectorSolutionReduction {

  private static final List<String> REDUCTION_ORDER =
      asList("MATCH", "NO_DECISION", "NO_MATCH", "NO_DATA");
  private static final FeatureVectorSolution DEFAULT_SOLUTION = SOLUTION_NO_DECISION;
  private static final Map<String, FeatureVectorSolution> FEATURE_VECTOR_VALUE_TO_SOLUTION_MAP =
      Map.of(
          "MATCH", SOLUTION_POTENTIAL_TRUE_POSITIVE,
          "NO_MATCH", SOLUTION_FALSE_POSITIVE
      );

  public FeatureVectorSolution solveFeatureVector(List<String> featureVectorValues) {
    Optional<String> reducedFeatureValue = reduceFeatureVectorValues(featureVectorValues);
    return reducedFeatureValue
        .map(FEATURE_VECTOR_VALUE_TO_SOLUTION_MAP::get)
        .orElse(DEFAULT_SOLUTION);
  }

  private Optional<String> reduceFeatureVectorValues(List<String> featureVectorValues) {
    for (String solution : REDUCTION_ORDER) {
      if (featureVectorValues.contains(solution)) {
        return Optional.of(solution);
      }
    }
    return Optional.empty();
  }
}

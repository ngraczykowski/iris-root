package com.silenteight.serp.governance.strategy.solve;

import lombok.NonNull;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.Collection;

import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.*;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.*;

class MatchSolutionsReducer {

  RecommendedAction reduce(Collection<FeatureVectorSolution> solutions) {
    validateSolutions(solutions);

    if (solutions.contains(SOLUTION_POTENTIAL_TRUE_POSITIVE))
      return ACTION_POTENTIAL_TRUE_POSITIVE;

    if (solutions.contains(SOLUTION_HINTED_POTENTIAL_TRUE_POSITIVE))
      return ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE;

    if (hasAnyNoDecisionAndAnyFalsePositiveOrHinted(solutions))
      return ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE;

    if (hasAllNoDecision(solutions))
      return ACTION_INVESTIGATE;

    if (hasAllFalsePositive(solutions))
      return ACTION_FALSE_POSITIVE;

    if (hasAllFalsePositiveOrHinted(solutions))
      return ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE;

    throw new IllegalArgumentException("Illegal set of solutions provided: " + solutions);
  }

  private static void validateSolutions(@NonNull Collection<FeatureVectorSolution> solutions) {
    if (solutions.isEmpty())
      throw new IllegalArgumentException("Solutions must not be empty");
  }

  private static boolean hasAnyNoDecisionAndAnyFalsePositiveOrHinted(
      @NonNull Collection<FeatureVectorSolution> solutions) {

    return hasAnyNoDecision(solutions) && hasAnyFalsePositiveOrHinted(solutions);
  }

  private static boolean hasAnyNoDecision(@NonNull Collection<FeatureVectorSolution> solutions) {
    return solutions
        .stream()
        .anyMatch(MatchSolutionsReducer::isNoDecision);
  }

  private static boolean isNoDecision(FeatureVectorSolution solution) {
    return SOLUTION_NO_DECISION == solution;
  }

  private static boolean hasAnyFalsePositiveOrHinted(
      @NonNull Collection<FeatureVectorSolution> solutions) {

    return solutions
        .stream()
        .anyMatch(MatchSolutionsReducer::isFalsePositiveOrHinted);
  }

  private static boolean isFalsePositiveOrHinted(FeatureVectorSolution solution) {
    return SOLUTION_FALSE_POSITIVE == solution || SOLUTION_HINTED_FALSE_POSITIVE == solution;
  }

  private static boolean hasAllNoDecision(@NonNull Collection<FeatureVectorSolution> solutions) {
    return solutions
        .stream()
        .allMatch(MatchSolutionsReducer::isNoDecision);
  }

  private static boolean hasAllFalsePositive(
      @NonNull Collection<FeatureVectorSolution> solutions) {

    return solutions
        .stream()
        .allMatch(MatchSolutionsReducer::isFalsePositive);
  }

  private static boolean isFalsePositive(FeatureVectorSolution solution) {
    return SOLUTION_FALSE_POSITIVE == solution;
  }

  private static boolean hasAllFalsePositiveOrHinted(
      @NonNull Collection<FeatureVectorSolution> solutions) {

    return solutions
        .stream()
        .allMatch(MatchSolutionsReducer::isFalsePositiveOrHinted);
  }
}

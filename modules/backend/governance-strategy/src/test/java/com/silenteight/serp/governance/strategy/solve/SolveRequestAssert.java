package com.silenteight.serp.governance.strategy.solve;

import com.silenteight.solving.api.v1.AnalystSolution;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import org.assertj.core.api.AbstractAssert;

import java.util.Objects;
import java.util.Set;

class SolveRequestAssert extends AbstractAssert<SolveRequestAssert, SolveRequest> {

  SolveRequestAssert(SolveRequest solveRequest) {
    super(solveRequest, SolveRequestAssert.class);
  }

  public static SolveRequestAssert assertThat(SolveRequest actual) {
    return new SolveRequestAssert(actual);
  }

  public SolveRequestAssert hasPreviousAnalystSolution(AnalystSolution analystSolution) {
    if (!Objects.equals(actual.getPreviousAnalystSolution(), analystSolution)) {
      failWithMessage("Expected previous analyst solution to be <%s>, but was <%s>",
          analystSolution, actual.getPreviousAnalystSolution());
    }

    return this;
  }

  public SolveRequestAssert hasSolvedMatchesFeatureVectorSolutions(
      FeatureVectorSolution... featureVectorSolutions) {

    Set<FeatureVectorSolution> featureVectorSolutionsSet = Set.of(featureVectorSolutions);
    if (!actual.getSolvedMatchesBranchSolutions().containsAll(featureVectorSolutionsSet)) {
      failWithMessage(
          "Expected solved matches feature vector solutions to be <%s>, but was <%s>",
          featureVectorSolutionsSet,
          actual.getSolvedMatchesBranchSolutions());
    }

    return this;
  }

  public SolveRequestAssert hasUnsolvedMatchesFeatureVectorSolutions(
      FeatureVectorSolution... featureVectorSolutions) {

    Set<FeatureVectorSolution> featureVectorSolutionsSet = Set.of(featureVectorSolutions);
    if (!actual.getUnsolvedMatchesBranchSolutions().containsAll(featureVectorSolutionsSet)) {
      failWithMessage(
          "Expected unsolved matches feature vector solutions to be <%s>, but was <%s>",
          featureVectorSolutionsSet,
          actual.getUnsolvedMatchesBranchSolutions());
    }

    return this;
  }

  public SolveRequestAssert hasObsoleteMatchesFeatureVectorSolutions(
      FeatureVectorSolution... featureVectorSolutions) {

    Set<FeatureVectorSolution> featureVectorSolutionsSet = Set.of(featureVectorSolutions);
    if (!actual.getObsoleteMatchesBranchSolutions().containsAll(featureVectorSolutionsSet)) {
      failWithMessage(
          "Expected obsoleted matches feature vector solutions to be <%s>, but was <%s>",
          featureVectorSolutionsSet,
          actual.getObsoleteMatchesBranchSolutions());
    }

    return this;
  }
}

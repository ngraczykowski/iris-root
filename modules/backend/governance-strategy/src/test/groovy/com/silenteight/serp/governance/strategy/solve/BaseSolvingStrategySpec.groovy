package com.silenteight.serp.governance.strategy.solve

import com.silenteight.solving.api.v1.AnalystSolution
import com.silenteight.solving.api.v1.FeatureVectorSolution

import spock.lang.Specification

import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.*
import static com.silenteight.solving.api.v1.AnalystSolution.*
import static com.silenteight.solving.api.v1.FeatureVectorSolution.*

abstract class BaseSolvingStrategySpec extends Specification {

  protected static final AnalystSolution AS_NONE = ANALYST_SOLUTION_UNSPECIFIED
  protected static final AnalystSolution AS_FP = ANALYST_FALSE_POSITIVE
  protected static final AnalystSolution AS_TC = ANALYST_TRUE_POSITIVE
  protected static final AnalystSolution AS_PTP = ANALYST_POTENTIAL_TRUE_POSITIVE
  protected static final AnalystSolution AS_O = ANALYST_OTHER

  protected static final FeatureVectorSolution BS_U = FEATURE_VECTOR_SOLUTION_UNSPECIFIED
  protected static final FeatureVectorSolution BS_ND = SOLUTION_NO_DECISION
  protected static final FeatureVectorSolution BS_PTP = SOLUTION_POTENTIAL_TRUE_POSITIVE
  protected static final FeatureVectorSolution BS_FP = SOLUTION_FALSE_POSITIVE
  protected static final FeatureVectorSolution BS_HFP = SOLUTION_HINTED_FALSE_POSITIVE
  protected static final FeatureVectorSolution BS_HPTP = SOLUTION_HINTED_POTENTIAL_TRUE_POSITIVE

  protected static final RecommendedAction R_ND = ACTION_INVESTIGATE
  protected static final RecommendedAction R_PF = ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE
  protected static final RecommendedAction R_PTP = ACTION_POTENTIAL_TRUE_POSITIVE
  protected static final RecommendedAction R_FP = ACTION_FALSE_POSITIVE
  protected static final RecommendedAction R_HFP = ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE
  protected static final RecommendedAction R_HPTP = ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE

  private SolvingStrategy strategy

  abstract SolvingStrategy getStrategy();

  def setup() {
    strategy = getStrategy()
  }

  def solve(
      AnalystSolution analystSolution,
      List<FeatureVectorSolution> obsoleteMatches,
      List<FeatureVectorSolution> solvedMatches,
      List<FeatureVectorSolution> unsolvedMatches,
      RecommendedAction expectedAiSolution) {

    def request = SolveRequest
        .builder()
        .previousAnalystSolution(analystSolution)
        .solvedMatchesBranchSolutions(solvedMatches)
        .unsolvedMatchesBranchSolutions(unsolvedMatches)
        .obsoleteMatchesBranchSolutions(obsoleteMatches)
        .build()

    strategy.solve(request).recommendedAction == expectedAiSolution
  }
}

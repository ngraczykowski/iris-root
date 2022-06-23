package com.silenteight.serp.governance.strategy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.Collection;

import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.*;
import static com.silenteight.solving.api.v1.AnalystSolution.ANALYST_OTHER;
import static com.silenteight.solving.api.v1.AnalystSolution.ANALYST_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.solving.api.v1.AnalystSolution.ANALYST_TRUE_POSITIVE;

@RequiredArgsConstructor
@Slf4j
class NsDenySolveStrategy implements SolvingStrategy {

  @NonNull
  private final MatchSolutionsReducer reducer;

  @Override
  public SolveResponse solve(SolveRequest request) {
    if (log.isTraceEnabled())
      log.trace("solve request: {}", request);

    if (request.hasObsoleteMatchesOnly())
      return new SolveResponse(ACTION_FALSE_POSITIVE);

    Collection<FeatureVectorSolution> unsolved = request.getUnsolvedMatchesBranchSolutions();
    if (unsolved.isEmpty())
      return new SolveResponse(ACTION_INVESTIGATE);

    RecommendedAction unsolvedMatchesRecommendedAction = reducer.reduce(unsolved);
    if (isPotentialTruePositiveOrHinted(unsolvedMatchesRecommendedAction))
      return new SolveResponse(unsolvedMatchesRecommendedAction);

    if (unsolvedMatchesRecommendedAction == ACTION_INVESTIGATE)
      return new SolveResponse(ACTION_INVESTIGATE);

    if (isPreviousSolutionPositive(request)
        || isDenyPartiallyFalseScenario(request, unsolvedMatchesRecommendedAction))
      return new SolveResponse(ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE);
    else
      return new SolveResponse(unsolvedMatchesRecommendedAction);
  }

  private static boolean isPreviousSolutionPositive(SolveRequest request) {
    return request.isPreviousAnalystSolutionIn(
        ANALYST_TRUE_POSITIVE, ANALYST_POTENTIAL_TRUE_POSITIVE, ANALYST_OTHER);
  }

  private static boolean isPotentialTruePositiveOrHinted(RecommendedAction recommendedAction) {
    return ACTION_POTENTIAL_TRUE_POSITIVE == recommendedAction
        || ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE == recommendedAction;
  }

  private static boolean isDenyPartiallyFalseScenario(
      SolveRequest request, RecommendedAction recommendedAction) {

    return request.hasSolvedMatches() && isFalsePositive(recommendedAction);
  }

  private static boolean isFalsePositive(RecommendedAction recommendedAction) {
    return ACTION_FALSE_POSITIVE == recommendedAction;
  }
}

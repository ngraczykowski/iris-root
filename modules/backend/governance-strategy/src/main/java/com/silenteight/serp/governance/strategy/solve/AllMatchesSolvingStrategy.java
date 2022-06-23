package com.silenteight.serp.governance.strategy.solve;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.Collection;

import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.ACTION_FALSE_POSITIVE;
import static org.apache.commons.collections4.CollectionUtils.union;

@RequiredArgsConstructor
class AllMatchesSolvingStrategy implements SolvingStrategy {

  private final MatchSolutionsReducer reducer;

  @Override
  public SolveResponse solve(SolveRequest request) {
    if (request.hasObsoleteMatchesOnly())
      return new SolveResponse(ACTION_FALSE_POSITIVE);

    Collection<FeatureVectorSolution> allRecommendedActions = union(
        request.getSolvedMatchesBranchSolutions(),
        request.getUnsolvedMatchesBranchSolutions());

    return new SolveResponse(reducer.reduce(allRecommendedActions));
  }
}

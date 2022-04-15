package com.silenteight.serp.governance.strategy.solve;

import lombok.RequiredArgsConstructor;

import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.ACTION_FALSE_POSITIVE;

@RequiredArgsConstructor
class UnsolvedMatchesSolvingStrategy implements SolvingStrategy {

  private final MatchSolutionsReducer reducer;

  @Override
  public SolveResponse solve(SolveRequest request) {
    if (request.hasObsoleteMatchesOnly())
      return new SolveResponse(ACTION_FALSE_POSITIVE);

    return new SolveResponse(reducer.reduce(request.getUnsolvedMatchesBranchSolutions()));
  }
}

package com.silenteight.serp.governance.strategy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum SolvingStrategyType {

  ALL_MATCHES(AllMatchesSolvingStrategy::new),
  USE_ANALYST_SOLUTION(UseAnalystSolutionSolvingStrategy::new),
  UNSOLVED_MATCHES(UnsolvedMatchesSolvingStrategy::new),
  NS_DENY(NsDenySolveStrategy::new);

  @NonNull
  private final Function<MatchSolutionsReducer, SolvingStrategy> factoryFunction;

  public SolvingStrategy getStrategy() {
    return factoryFunction.apply(new MatchSolutionsReducer());
  }
}

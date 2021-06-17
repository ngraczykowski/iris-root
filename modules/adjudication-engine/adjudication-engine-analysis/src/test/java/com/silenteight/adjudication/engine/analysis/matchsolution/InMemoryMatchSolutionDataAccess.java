package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.api.v1.MatchSolution;

class InMemoryMatchSolutionDataAccess implements MatchSolutionDataAccess {

  @Override
  public MatchSolution getMatchSolution(
      long matchSolutionId) {
    if (matchSolutionId == 1)
      return MatchSolution.newBuilder().build();
    return null;
  }
}

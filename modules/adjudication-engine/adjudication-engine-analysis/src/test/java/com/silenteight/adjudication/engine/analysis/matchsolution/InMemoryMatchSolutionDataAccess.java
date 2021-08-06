package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.api.v1.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class InMemoryMatchSolutionDataAccess implements MatchSolutionDataAccess {

  private final List<SaveMatchSolutionRequest> matchSolutions = new ArrayList<>();

  @Override
  public MatchSolution getMatchSolution(
      long matchSolutionId) {
    if (matchSolutionId == 1)
      return MatchSolution.newBuilder().build();
    return null;
  }

  @Override
  public void save(
      Collection<SaveMatchSolutionRequest> requests) {
    matchSolutions.addAll(requests);
  }

  public int count() {
    return matchSolutions.size();
  }
}

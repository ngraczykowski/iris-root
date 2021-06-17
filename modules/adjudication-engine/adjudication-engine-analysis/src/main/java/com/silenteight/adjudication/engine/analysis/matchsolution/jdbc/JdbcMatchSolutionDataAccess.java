package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionDataAccess;

import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class JdbcMatchSolutionDataAccess implements MatchSolutionDataAccess {

  private final SelectMatchSolutionQuery selectMatchSolutionQuery;

  @Override
  public MatchSolution getMatchSolution(long matchSolutionId) {
    return selectMatchSolutionQuery.execute(matchSolutionId);
  }
}

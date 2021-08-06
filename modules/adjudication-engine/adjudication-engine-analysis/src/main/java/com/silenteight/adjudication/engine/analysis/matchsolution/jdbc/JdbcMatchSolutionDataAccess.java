package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionDataAccess;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@RequiredArgsConstructor
@Repository
class JdbcMatchSolutionDataAccess implements MatchSolutionDataAccess {

  private final SelectMatchSolutionQuery selectMatchSolutionQuery;
  private final InsertMatchSolutionQuery insertMatchSolutionQuery;

  @Override
  public MatchSolution getMatchSolution(long matchSolutionId) {
    return selectMatchSolutionQuery.execute(matchSolutionId);
  }

  @Override
  public void save(Collection<SaveMatchSolutionRequest> requests) {
    insertMatchSolutionQuery.execute(requests);
  }
}

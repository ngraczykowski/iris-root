package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.MatchSolution;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetMatchSolutionUseCase {

  private final MatchSolutionDataAccess matchSolutionDataAccess;

  MatchSolution getMatchSolution(String matchSolutionName) {
    var matchSolutionId = ResourceName.create(matchSolutionName).getLong("match-solutions");
    return matchSolutionDataAccess.getMatchSolution(matchSolutionId);
  }
}

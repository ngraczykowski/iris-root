package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.api.v1.MatchSolution;

public interface MatchSolutionDataAccess {

  MatchSolution getMatchSolution(long matchSolutionId);
}

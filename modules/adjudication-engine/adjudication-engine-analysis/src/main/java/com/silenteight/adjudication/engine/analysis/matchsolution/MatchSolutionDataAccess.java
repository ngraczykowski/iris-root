package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.api.v1.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;

import java.util.Collection;

public interface MatchSolutionDataAccess {

  MatchSolution getMatchSolution(long matchSolutionId);

  void save(Collection<SaveMatchSolutionRequest> requests);
}

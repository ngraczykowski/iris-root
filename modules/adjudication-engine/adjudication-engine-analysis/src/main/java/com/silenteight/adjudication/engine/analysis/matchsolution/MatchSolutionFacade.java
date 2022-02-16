package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.MatchSolution;
import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;
import com.silenteight.adjudication.internal.v1.MatchFeaturesUpdated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MatchSolutionFacade {

  private final HandleMatchFeaturesUpdatedUseCase handleMatchFeaturesUpdatedUseCase;

  private final HandleMatchCategoriesUpdatedUseCase handleMatchCategoriesUpdatedUseCase;

  private final GetMatchSolutionUseCase getMatchSolutionUseCase;

  public List<MatchesSolved> handleMatchFeaturesUpdated(
      MatchFeaturesUpdated matchFeaturesUpdated) {
    return handleMatchFeaturesUpdatedUseCase.handleMatchFeaturesUpdated(matchFeaturesUpdated);
  }

  public Optional<MatchesSolved> handleMatchCategoriesUpdated(
      MatchCategoriesUpdated matchCategoriesUpdated) {

    return handleMatchCategoriesUpdatedUseCase.handleMatchCategoriesUpdated(matchCategoriesUpdated);
  }

  public MatchSolution getMatchSolution(String matchSolutionName) {
    return getMatchSolutionUseCase.getMatchSolution(matchSolutionName);
  }
}

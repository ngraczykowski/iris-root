package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;
import com.silenteight.adjudication.internal.v1.MatchFeaturesUpdated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MatchSolutionFacade {

  private final SolveAnalysisMatchesUseCase solveAnalysisMatchesUseCase;

  private final HandleMatchFeaturesUpdatedUseCase handleMatchFeaturesUpdatedUseCase;

  private final HandleMatchCategoriesUpdatedUseCase handleMatchCategoriesUpdatedUseCase;

  public void solveAnalysisMatches(String analysisName) {
    solveAnalysisMatchesUseCase.solveAnalysisMatches(analysisName);
  }

  public List<MatchesSolved> handleMatchFeaturesUpdated(
      MatchFeaturesUpdated matchFeaturesUpdated) {
    return handleMatchFeaturesUpdatedUseCase.handleMatchFeaturesUpdated(matchFeaturesUpdated);
  }

  public Optional<MatchesSolved> handleMatchCategoriesUpdated(
      MatchCategoriesUpdated matchCategoriesUpdated) {

    return handleMatchCategoriesUpdatedUseCase.handleMatchCategoriesUpdated(matchCategoriesUpdated);
  }
}

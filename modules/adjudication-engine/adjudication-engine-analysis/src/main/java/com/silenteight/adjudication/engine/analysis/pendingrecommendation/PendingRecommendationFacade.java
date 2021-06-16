package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PendingRecommendationFacade {

  private final HandleAddedAnalysisDatasetsUseCase handleAddedAnalysisDatasetsUseCase;
  private final RemoveSolvedPendingRecommendationUseCase removeSolvedPendingRecommendationUseCase;

  public Optional<PendingRecommendations> handleAddedAnalysisDatasets(
      AddedAnalysisDatasets addedAnalysisDatasets) {

    return handleAddedAnalysisDatasetsUseCase.handleAddedAnalysisDatasets(addedAnalysisDatasets);
  }

  public void removeSolvedPendingRecommendation() {
    removeSolvedPendingRecommendationUseCase.removeSolvedPendingRecommendation();
  }
}

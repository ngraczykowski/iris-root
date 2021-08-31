package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PendingRecommendationFacade {

  private final HandleAnalysisAlertsAddedUseCase handleAnalysisAlertsAddedUseCase;
  private final RemoveSolvedPendingRecommendationUseCase removeSolvedPendingRecommendationUseCase;

  public Optional<PendingRecommendations> handleAnalysisAlertsAdded(
      AnalysisAlertsAdded addedAnalysisAlerts) {

    return handleAnalysisAlertsAddedUseCase.handleAnalysisAlertsAdded(addedAnalysisAlerts);
  }

  public void removeSolvedPendingRecommendation() {
    removeSolvedPendingRecommendationUseCase.removeSolvedPendingRecommendation();
  }
}

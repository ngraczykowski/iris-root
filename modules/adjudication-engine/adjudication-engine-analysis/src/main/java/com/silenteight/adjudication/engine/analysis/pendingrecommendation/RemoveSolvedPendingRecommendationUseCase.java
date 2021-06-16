package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RemoveSolvedPendingRecommendationUseCase {

  private final PendingRecommendationDataAccess pendingRecommendationDataAccess;

  void removeSolvedPendingRecommendation() {
    pendingRecommendationDataAccess.removeSolvedPendingRecommendations();
  }
}

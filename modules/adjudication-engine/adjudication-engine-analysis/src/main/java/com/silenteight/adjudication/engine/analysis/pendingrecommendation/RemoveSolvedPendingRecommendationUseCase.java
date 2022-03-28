package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RemoveSolvedPendingRecommendationUseCase {

  private final PendingRecommendationDataAccess pendingRecommendationDataAccess;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void removeSolvedPendingRecommendation() {
    pendingRecommendationDataAccess.removeSolvedPendingRecommendations();
  }
}

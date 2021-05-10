package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class CreatePendingRecommendationsUseCase {

  private final PendingRecommendationDataAccess dataAccess;

  @Transactional
  boolean createPendingRecommendations(long analysisId) {
    return dataAccess.createPendingRecommendations(analysisId) > 0;
  }
}

package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

@RequiredArgsConstructor
class StoreGnsRtRecommendationUseCase {

  private final ScbRecommendationService scbRecommendationService;

  void storeRecommendation(Recommendation recommendation) {
    scbRecommendationService.saveRecommendation(recommendation);
  }
}

package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;

@RequiredArgsConstructor
class StoreGnsRtRecommendationUseCase {

  private final ScbRecommendationService scbRecommendationService;

  void storeRecommendation(AlertRecommendation alertRecommendation) {
    scbRecommendationService.saveRecommendation(alertRecommendation);
  }
}

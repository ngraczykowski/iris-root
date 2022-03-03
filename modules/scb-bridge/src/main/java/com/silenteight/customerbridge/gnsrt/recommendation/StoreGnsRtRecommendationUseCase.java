package com.silenteight.customerbridge.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.recommendation.ScbRecommendationService;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;

@RequiredArgsConstructor
class StoreGnsRtRecommendationUseCase {

  private final ScbRecommendationService scbRecommendationService;

  void storeRecommendation(AlertRecommendation alertRecommendation) {
    scbRecommendationService.saveRecommendation(alertRecommendation);
  }
}

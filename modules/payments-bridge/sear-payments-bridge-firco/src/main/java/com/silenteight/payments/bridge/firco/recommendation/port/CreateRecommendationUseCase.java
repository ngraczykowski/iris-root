package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.payments.bridge.firco.recommendation.service.RecommendationEntity;
import com.silenteight.payments.bridge.firco.recommendation.service.RecommendationWrapper;

public interface CreateRecommendationUseCase {

  RecommendationEntity createRecommendation(RecommendationWrapper recommendationWrapper);
}

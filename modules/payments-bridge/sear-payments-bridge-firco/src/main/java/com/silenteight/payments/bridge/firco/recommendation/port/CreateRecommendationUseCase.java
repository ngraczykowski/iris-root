package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationWrapper;

public interface CreateRecommendationUseCase {

  RecommendationId createRecommendation(RecommendationWrapper recommendationWrapper);
}

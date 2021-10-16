package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.payments.bridge.firco.recommendation.model.Recommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;

public interface GetRecommendationUseCase {

  Recommendation get(RecommendationId recommendation);
}

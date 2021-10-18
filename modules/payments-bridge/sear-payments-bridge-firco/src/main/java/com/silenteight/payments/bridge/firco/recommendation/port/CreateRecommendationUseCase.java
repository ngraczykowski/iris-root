package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;

import java.util.UUID;

public interface CreateRecommendationUseCase {

  RecommendationId createAdjudicationRecommendation(UUID alertId,
      RecommendationWithMetadata metadata);

  RecommendationId createBridgeRecommendation(UUID alertId,
      RecommendationReason reason);

}

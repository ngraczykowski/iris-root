package com.silenteight.payments.bridge.ae.recommendation.port;

import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

public interface RecommendationClientPort {

  RecommendationWithMetadata receiveRecommendation(GetRecommendationRequest request);
}

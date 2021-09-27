package com.silenteight.payments.bridge.ae.recommendation.port;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;

public interface RecommendationClientPort {

  Recommendation receiveRecommendation(GetRecommendationRequest request);
}

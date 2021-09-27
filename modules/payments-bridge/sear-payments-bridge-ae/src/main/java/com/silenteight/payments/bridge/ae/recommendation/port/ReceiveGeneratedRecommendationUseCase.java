package com.silenteight.payments.bridge.ae.recommendation.port;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;

public interface ReceiveGeneratedRecommendationUseCase {

  void handleGeneratedRecommendationMessage(RecommendationsGenerated recommendationsGenerated);
}

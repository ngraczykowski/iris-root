package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

public interface RecommendationClientApi {

  RecommendationsOut recommendation(String analysisId);
}

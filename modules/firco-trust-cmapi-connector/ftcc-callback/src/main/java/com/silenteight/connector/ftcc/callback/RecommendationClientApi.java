package com.silenteight.connector.ftcc.callback;

import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import java.util.List;

public interface RecommendationClientApi {

  RecommendationsOut recommendation(List<String> alertId);
}

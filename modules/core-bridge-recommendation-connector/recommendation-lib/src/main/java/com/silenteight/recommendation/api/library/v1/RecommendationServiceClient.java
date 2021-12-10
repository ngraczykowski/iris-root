package com.silenteight.recommendation.api.library.v1;

public interface RecommendationServiceClient {

  RecommendationsOut getRecommendations(RecommendationsIn request);
}

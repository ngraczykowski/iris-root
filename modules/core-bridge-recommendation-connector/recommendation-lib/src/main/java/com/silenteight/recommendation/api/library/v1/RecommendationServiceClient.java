package com.silenteight.recommendation.api.library.v1;

import com.silenteight.proto.recommendation.api.v1.RecommendationResponse;

import java.util.Iterator;

public interface RecommendationServiceClient {

  RecommendationsOut getRecommendations(RecommendationsIn request);

  Iterator<RecommendationResponse> streamRecommendations(RecommendationsIn request);
}

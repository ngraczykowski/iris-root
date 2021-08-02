package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

public interface RecommendationService {

  RecommendationWithMetadata getRecommendationWithMetadata(@NonNull String recommendationName);
}

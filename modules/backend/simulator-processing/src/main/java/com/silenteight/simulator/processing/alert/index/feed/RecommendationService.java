package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

import java.util.Iterator;

public interface RecommendationService {

  RecommendationWithMetadata getRecommendationWithMetadata(@NonNull String recommendationName);

  Iterator<RecommendationWithMetadata> streamRecommendationsWithMetadata(
      @NonNull String analysisName);
}

package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;

public interface RecommendationService {

  Recommendation getRecommendation(@NonNull String recommendationName);

  RecommendationMetadata getMetadata(@NonNull String recommendationName);
}

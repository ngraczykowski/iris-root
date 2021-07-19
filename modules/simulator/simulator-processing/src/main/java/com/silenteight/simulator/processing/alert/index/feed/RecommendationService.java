package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Recommendation;

public interface RecommendationService {

  Recommendation getRecommendation(@NonNull String recommendationName);
}

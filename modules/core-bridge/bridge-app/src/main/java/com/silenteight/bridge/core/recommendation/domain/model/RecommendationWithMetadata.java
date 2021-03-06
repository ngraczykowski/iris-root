package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;

public record RecommendationWithMetadata(String name,
                                         String alertName,
                                         String analysisName,
                                         String recommendedAction,
                                         String recommendationComment,
                                         OffsetDateTime recommendedAt,
                                         RecommendationMetadata metadata,
                                         Boolean timeout
) {

  @Builder
  public RecommendationWithMetadata {}
}

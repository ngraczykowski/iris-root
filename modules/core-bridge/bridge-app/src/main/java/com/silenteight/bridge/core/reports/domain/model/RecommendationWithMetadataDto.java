package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;

public record RecommendationWithMetadataDto(String name,
                                            String alertName,
                                            String analysisName,
                                            String recommendedAction,
                                            String recommendationComment,
                                            OffsetDateTime recommendedAt,
                                            RecommendationMetadataDto metadata,
                                            Boolean timeout
) {

  @Builder
  public RecommendationWithMetadataDto {}
}

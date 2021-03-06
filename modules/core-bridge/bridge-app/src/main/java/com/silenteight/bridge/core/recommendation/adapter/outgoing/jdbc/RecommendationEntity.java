package com.silenteight.bridge.core.recommendation.adapter.outgoing.jdbc;

import lombok.Builder;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("core_bridge_recommendations")
record RecommendationEntity(@Id long id,
                            String name,
                            String alertName,
                            String analysisName,
                            Instant recommendedAt,
                            String recommendedAction,
                            String recommendationComment,
                            RecommendationMetadata payload,
                            Boolean timeout
) {

  @Builder
  RecommendationEntity {}
}

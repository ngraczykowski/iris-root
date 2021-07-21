package com.silenteight.hsbc.bridge.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import java.time.OffsetDateTime;

@Builder
@Value
public class RecommendationWithMetadataDto {

  @NonNull String alert;
  @NonNull String name;
  @NonNull String recommendedAction;
  @NonNull String recommendationComment;
  OffsetDateTime date;
  RecommendationMetadata metadata;
  String s8recommendedAction;
}

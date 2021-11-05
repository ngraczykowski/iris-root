package com.silenteight.adjudication.api.library.v1.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class RecommendationWithMetadataOut {

  @NonNull String alert;
  @NonNull String name;
  @NonNull String recommendedAction;
  @NonNull String recommendationComment;
  OffsetDateTime date;
  RecommendationMetadataOut metadata;
  String s8recommendedAction;
}

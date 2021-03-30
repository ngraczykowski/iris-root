package com.silenteight.hsbc.bridge.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class RecommendationDto {

  @NonNull String alert;
  @NonNull String name;
  @NonNull String recommendedAction;
  @NonNull String recommendationComment;
  OffsetDateTime date;
}

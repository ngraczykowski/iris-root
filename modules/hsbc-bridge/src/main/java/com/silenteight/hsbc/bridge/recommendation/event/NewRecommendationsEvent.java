package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;

@Value
@Builder
public class NewRecommendationsEvent {

  @NonNull String analysis;
  @Nullable String dataset;
}

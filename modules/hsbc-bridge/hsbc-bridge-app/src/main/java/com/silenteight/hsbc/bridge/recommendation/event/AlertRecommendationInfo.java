package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.NonNull;
import lombok.Value;

@Value
public class AlertRecommendationInfo {

  @NonNull String alert;
  @NonNull String recommendation;
}

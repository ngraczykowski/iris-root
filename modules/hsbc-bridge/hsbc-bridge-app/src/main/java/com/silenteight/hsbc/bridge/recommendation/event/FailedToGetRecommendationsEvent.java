package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.Value;

@Value
public class FailedToGetRecommendationsEvent {

  String analysis;
}

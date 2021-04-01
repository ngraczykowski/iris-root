package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.Value;

import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;

@Value
public class NewRecommendationEvent {

  RecommendationDto recommendation;
}

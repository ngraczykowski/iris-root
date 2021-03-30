package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;

@AllArgsConstructor
@Getter
public class NewRecommendationEvent {

  private final RecommendationDto recommendation;
}

package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class RecommendedActionStatistics {

  private final Map<String, Integer> statistics;

  public Integer getRecommendedActionCount(String recommendedAction) {
    return statistics.getOrDefault(recommendedAction, 0);
  }
}

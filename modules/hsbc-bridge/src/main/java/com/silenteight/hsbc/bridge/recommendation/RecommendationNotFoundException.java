package com.silenteight.hsbc.bridge.recommendation;

public class RecommendationNotFoundException extends RuntimeException {

  public RecommendationNotFoundException(String alert) {
    super("Recommendation not found for alert: " + alert);
  }
}

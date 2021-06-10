package com.silenteight.hsbc.bridge.recommendation;

public class RecommendationNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -2177534644482171259L;

  public RecommendationNotFoundException(String alert) {
    super("Recommendation not found for alert: " + alert);
  }
}

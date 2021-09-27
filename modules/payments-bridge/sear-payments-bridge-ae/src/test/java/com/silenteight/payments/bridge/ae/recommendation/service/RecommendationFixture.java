package com.silenteight.payments.bridge.ae.recommendation.service;

import com.silenteight.adjudication.api.v1.Recommendation;

class RecommendationFixture {

  public static Recommendation createRecommendation() {
    return Recommendation
        .newBuilder()
        .setAlert("alerts/1")
        .setRecommendationComment("comment")
        .build();
  }
}

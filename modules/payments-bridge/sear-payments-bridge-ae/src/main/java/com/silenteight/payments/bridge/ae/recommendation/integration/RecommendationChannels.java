package com.silenteight.payments.bridge.ae.recommendation.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class RecommendationChannels {

  public static final String ALERT_RECOMMENDATION_GATEWAY_CHANNEL =
      "alertRecommendationGatewayChannel";

  public static final String ALERT_RECOMMENDATION_OUTBOUND_CHANNEL =
      "alertRecommendationOutboundChannel";
}

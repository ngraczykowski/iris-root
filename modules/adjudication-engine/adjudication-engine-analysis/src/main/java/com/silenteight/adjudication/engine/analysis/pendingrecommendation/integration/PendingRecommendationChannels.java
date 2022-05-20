package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PendingRecommendationChannels {

  public static final String ADDED_ANALYSIS_ALERTS_INBOUND_CHANNEL =
      "addedAnalysisAlertsInboundChannel";

  public static final String PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL =
      "pendingRecommendationsOutboundChannel";
}

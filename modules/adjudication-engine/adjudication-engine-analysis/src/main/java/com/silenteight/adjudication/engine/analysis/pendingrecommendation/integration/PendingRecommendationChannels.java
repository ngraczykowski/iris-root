package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PendingRecommendationChannels {

  static final String ADDED_ANALYSIS_DATASETS_INBOUND_CHANNEL =
      "addedAnalysisDatasetsInboundChannel";

  public static final String PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL =
      "pendingRecommendationsOutboundChannel";
}

package com.silenteight.adjudication.engine.analysis.recommendation.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RecommendationChannels {

  public static final String MATCHES_SOLVED_RECOMMENDATION_INBOUND_CHANNEL
      = "matchesSolvedRecommendationInboundChannel";

  public static final String COMMENT_INPUT_UPDATED_RECOMMENDATION_INBOUND_CHANNEL
      = "commentInputUpdatedRecommendationInboundChannel";

  public static final String RECOMMENDATIONS_GENERATED_OUTBOUND_CHANNEL
      = "recommendationsGeneratedOutboundChannel";
}

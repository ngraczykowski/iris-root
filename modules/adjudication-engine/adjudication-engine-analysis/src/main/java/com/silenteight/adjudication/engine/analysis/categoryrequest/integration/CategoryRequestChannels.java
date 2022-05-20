package com.silenteight.adjudication.engine.analysis.categoryrequest.integration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryRequestChannels {

  public static final String CATEGORY_REQUEST_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL =
      "categoryRequestPendingRecommendationsInboundChannel";

  public static final String MATCH_CATEGORIES_UPDATED_OUTBOUND_CHANNEL =
      "matchCategoriesUpdatedOutboundChannel";
}

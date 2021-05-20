package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class IntegrationChannels {

  static final String PENDING_RECOMMENDATIONS_PUB_SUB_CHANNEL =
      "pendingRecommendationsPubSubChannel";
}

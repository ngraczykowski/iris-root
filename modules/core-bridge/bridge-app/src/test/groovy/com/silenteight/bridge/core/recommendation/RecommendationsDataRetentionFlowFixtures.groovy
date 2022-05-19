package com.silenteight.bridge.core.recommendation

import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.dataretention.api.v1.AlertsExpired

import java.time.OffsetDateTime

class RecommendationsDataRetentionFlowFixtures {

  static def ANALYSIS_NAME = 'analysis/1'

  static def RECOMMENDATIONS = [
      RecommendationWithMetadata.builder()
          .alertName('alert/1')
          .name("recommendation/1")
          .analysisName(ANALYSIS_NAME)
          .recommendedAction("action")
          .recommendationComment("comment")
          .recommendedAt(OffsetDateTime.now())
          .metadata(new RecommendationMetadata([MatchMetadata.builder().build()]))
          .build()
  ]

  static def ALERTS_EXPIRED_MESSAGE = AlertsExpired.newBuilder()
      .addAllAlerts(RECOMMENDATIONS.collect {it.alertName()})
      .addAllAlertsData([])
      .build()
}

package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.RecommendationFixtures

import spock.lang.Specification

class RecommendationMapperSpec extends Specification {

  def 'should create recommendation response'() {
    given:
    def batchWithAlerts = RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    def recommendationWithMetadata = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]
    def recommendedAtForErrorAlerts = RecommendationFixtures.RECOMMENDATION_RECOMMENDED_AT

    when:
    def recommendation = RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts, recommendationWithMetadata, recommendedAtForErrorAlerts)

    then:
    recommendation == RecommendationFixtures.RECOMMENDATION_RESPONSE
  }

  def 'should create recommendation response for erroneous alert'() {
    given:
    def batchWithAlerts = RecommendationFixtures.BATCH_WITH_ERROR_ALERT_DTO
    def recommendationWithMetadata = []
    def recommendedAtForErrorAlerts = RecommendationFixtures.RECOMMENDATION_RECOMMENDED_AT

    when:
    def recommendation = RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts, recommendationWithMetadata, recommendedAtForErrorAlerts)

    then:
    recommendation == RecommendationFixtures.ERRONEOUS_RECOMMENDATION_RESPONSE
  }
}

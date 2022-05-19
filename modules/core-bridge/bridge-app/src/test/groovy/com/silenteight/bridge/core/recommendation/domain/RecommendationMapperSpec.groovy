package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationDto

import spock.lang.Specification

class RecommendationMapperSpec extends Specification {

  def 'should create recommendation response'() {
    given:
    def batchWithAlerts = RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    def recommendationWithMetadata = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]
    def recommendedAtForErrorAlerts = RecommendationFixtures.RECOMMENDATION_RECOMMENDED_AT
    def batchStatistics = RecommendationFixtures.BATCH_STATISTICS

    when:
    def recommendation = RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts, recommendationWithMetadata, recommendedAtForErrorAlerts, batchStatistics)

    then:
    recommendation == RecommendationFixtures.RECOMMENDATIONS_RESPONSE
  }

  def 'should create recommendation response for erroneous alert'() {
    given:
    def batchWithAlerts = RecommendationFixtures.BATCH_WITH_ERROR_ALERT_DTO
    def recommendationWithMetadata = []
    def recommendedAtForErrorAlerts = RecommendationFixtures.RECOMMENDATION_RECOMMENDED_AT
    def batchStatistics = RecommendationFixtures.BATCH_STATISTICS

    when:
    def recommendation = RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts, recommendationWithMetadata, recommendedAtForErrorAlerts, batchStatistics)

    then:
    recommendation == RecommendationFixtures.ERRONEOUS_RECOMMENDATIONS_RESPONSE
  }

  def 'should create recommendation response for null recommendation metadata'() {
    given:
    def batchWithAlerts = RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    def recommendationWithNullMetadata = [RecommendationFixtures.RECOMMENDATION_WITHOUT_METADATA]
    def recommendedAtForErrorAlerts = RecommendationFixtures.RECOMMENDATION_RECOMMENDED_AT
    def batchStatistics = RecommendationFixtures.BATCH_STATISTICS

    when:
    def recommendation = RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts, recommendationWithNullMetadata, recommendedAtForErrorAlerts,
        batchStatistics)

    then:
    recommendation == RecommendationFixtures.RECOMMENDATION_WITHOUT_METADATA_RESPONSE
  }

  def 'should map dto to RecommendationResponse'() {
    given:
    def dto = RecommendationDto.builder()
        .batchId(RecommendationFixtures.BATCH_ID_WITH_POLICY)
        .alert(RecommendationFixtures.ALERTS_WITHOUT_MATCHES)
        .matchWithAlertIds(RecommendationFixtures.MATCHES_WITH_ALERTS_IDS)
        .recommendation(RecommendationFixtures.RECOMMENDATION_WITH_METADATA)
        .statistics(RecommendationFixtures.BATCH_STATISTICS)
        .build()

    when:
    def recommendation = RecommendationMapper.toRecommendationResponse(dto)

    then:
    recommendation == RecommendationFixtures.RECOMMENDATION_RESPONSE
  }

  def 'should map dto to erroneous RecommendationResponse'() {
    given:
    def dto = RecommendationDto.builder()
        .batchId(RecommendationFixtures.BATCH_ID_WITH_POLICY)
        .alert(RecommendationFixtures.ERROR_ALERTS_WITHOUT_MATCHES)
        .matchWithAlertIds(RecommendationFixtures.MATCHES_WITH_ALERTS_IDS)
        .recommendation(null)
        .statistics(RecommendationFixtures.BATCH_STATISTICS)
        .recommendedAtForErrorAlerts(RecommendationFixtures.RECOMMENDATION_RECOMMENDED_AT)
        .build()

    when:
    def recommendation = RecommendationMapper.toRecommendationResponse(dto)

    then:
    recommendation == RecommendationFixtures.ERRONEOUS_RECOMMENDATION_RESPONSE
  }
}

package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService

import spock.lang.Specification
import spock.lang.Subject

import java.time.OffsetDateTime

class RecommendationFacadeSpec extends Specification {

  def recommendationService = Mock(RecommendationService)
  def recommendationRepository = Mock(RecommendationRepository)
  def recommendationPublisher = Mock(RecommendationEventPublisher)
  def recommendationsStatisticsService = Mock(RecommendationsStatisticsService)

  @Subject
  def underTest = new RecommendationFacade(
      recommendationService,
      recommendationPublisher,
      recommendationRepository,
      recommendationsStatisticsService
  )

  def 'should proceed ready recommendations'() {
    given:
    def analysis = 'someAnalysis'

    when:
    underTest.proceedReadyRecommendations(analysis)

    then:
    1 * recommendationService.getRecommendations(analysis) >> [RECOMMENDATION_WITH_METADATA]
    1 * recommendationRepository.saveAll(_ as List<RecommendationWithMetadata>)
    1 * recommendationPublisher.publish(_ as RecommendationsReceivedEvent)
  }

  def 'should get recommendations statistics'() {
    given:
    def analysisName = 'analysisName'

    when:
    underTest.getRecommendationsStatistics(analysisName)

    then:
    1 * recommendationsStatisticsService.createRecommendationsStatistics(analysisName)
  }

  private static RECOMMENDATION_WITH_METADATA = RecommendationWithMetadata.builder()
      .name('someName')
      .alertName('someAlert')
      .analysisName('someAnalysis')
      .recommendedAt(OffsetDateTime.MAX)
      .recommendationComment('someComment')
      .recommendedAction('someAction')
      .metadata(null)
      .build()
}

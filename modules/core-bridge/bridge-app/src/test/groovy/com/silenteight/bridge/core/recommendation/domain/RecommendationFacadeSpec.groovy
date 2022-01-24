package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationsCommand
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService
import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand

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
    def analysis = Fixtures.ANALYSIS_NAME

    when:
    underTest.proceedReadyRecommendations(analysis)

    then:
    1 * recommendationService.getRecommendations(analysis) >>
        [Fixtures.RECOMMENDATION_WITH_METADATA]
    1 * recommendationRepository.saveAll(_ as List<RecommendationWithMetadata>)
    1 * recommendationPublisher.publish(_ as RecommendationsReceivedEvent)
  }

  def 'should get recommendations statistics'() {
    given:
    def analysisName = Fixtures.ANALYSIS_NAME

    when:
    underTest.getRecommendationsStatistics(analysisName)

    then:
    1 * recommendationsStatisticsService.createRecommendationsStatistics(analysisName)
  }

  def 'should get recommendations by analysis name'() {
    given:
    def command = Fixtures.GET_RECOMMENDATIONS_COMMAND

    when:
    underTest.getRecommendations(command)

    then:
    1 * recommendationRepository.findByAnalysisName(Fixtures.ANALYSIS_NAME)
  }

  static class Fixtures {

    static def RECOMMENDATION_NAME = 'someName'
    static def ANALYSIS_NAME = 'analysisName'
    static def ALERT_NAME = 'someAlert'
    static def RECOMMENDATION_COMMENT = 'someComment'
    static def RECOMMENDATION_ACTION = 'someAction'

    static def RECOMMENDATION_WITH_METADATA = RecommendationWithMetadata.builder()
        .name(RECOMMENDATION_NAME)
        .alertName(ALERT_NAME)
        .analysisName(ANALYSIS_NAME)
        .recommendedAt(OffsetDateTime.MAX)
        .recommendationComment(RECOMMENDATION_COMMENT)
        .recommendedAction(RECOMMENDATION_ACTION)
        .metadata(null)
        .build()

    static def GET_RECOMMENDATIONS_COMMAND = new GetRecommendationsCommand(ANALYSIS_NAME)
    static def GET_BATCH_WITH_ALERTS_COMMAND = new GetBatchWithAlertsCommand(ANALYSIS_NAME)
  }
}

package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService

import spock.lang.Specification
import spock.lang.Subject

import java.time.OffsetDateTime

class RecommendationFacadeSpec extends Specification {

  def registrationService = Mock(RegistrationService)
  def recommendationService = Mock(RecommendationService)
  def recommendationRepository = Mock(RecommendationRepository)
  def recommendationPublisher = Mock(RecommendationEventPublisher)

  @Subject
  def underTest = new RecommendationFacade(
      registrationService,
      recommendationService,
      recommendationPublisher,
      recommendationRepository
  )

  def 'should proceed ready recommendations'() {
    given:
    def analysis = Fixtures.ANALYSIS_NAME

    when:
    underTest.proceedReadyRecommendations(analysis)

    then:
    1 * recommendationService.getRecommendations(analysis) >> [Fixtures.RECOMMENDATION_WITH_METADATA]
    1 * recommendationRepository.saveAll(_ as List<RecommendationWithMetadata>)
    1 * recommendationPublisher.publish(_ as RecommendationsReceivedEvent)
  }

  def 'should get recommendations by analysis name'() {
    given:
    def command = Fixtures.GET_RECOMMENDATIONS_RESPONSE_COMMAND

    when:
    underTest.getRecommendationsResponse(command)

    then:
    1 * registrationService.getBatchWithAlerts(Fixtures.ANALYSIS_NAME) >> RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    1 * recommendationRepository.findByAnalysisName(Fixtures.ANALYSIS_NAME) >> [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]
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

    static def GET_RECOMMENDATIONS_RESPONSE_COMMAND = new GetRecommendationCommand(ANALYSIS_NAME)
  }
}

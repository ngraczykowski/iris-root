package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService

import spock.lang.Specification
import spock.lang.Subject

class RecommendationFacadeSpec extends Specification {

  def registrationService = Mock(RegistrationService)
  def recommendationProcessor = Mock(RecommendationProcessor)
  def recommendationRepository = Mock(RecommendationRepository)
  def batchStatisticsService = Mock(BatchStatisticsService)

  @Subject
  def underTest = new RecommendationFacade(
      registrationService,
      recommendationProcessor,
      recommendationRepository,
      batchStatisticsService
  )

  def 'should proceed ready recommendations'() {
    given:
    def command = RecommendationFixtures.READY_RECOMMENDATIONS_COMMAND

    when:
    underTest.proceedReadyRecommendations(command)

    then:
    1 * recommendationProcessor
        .proceedReadyRecommendations(RecommendationFixtures.READY_RECOMMENDATIONS_COMMAND.recommendationsWithMetadata())
  }

  def 'should proceed batch timeout'() {
    given:
    def command = RecommendationFixtures.TIMED_OUT_RECOMMENDATIONS_COMMAND

    when:
    underTest.proceedBatchTimeout(command)

    then:
    1 * recommendationProcessor.createTimedOutRecommendations(
        RecommendationFixtures.TIMED_OUT_RECOMMENDATIONS_COMMAND.analysisName(),
        RecommendationFixtures.TIMED_OUT_RECOMMENDATIONS_COMMAND.alertNames())
  }


  def 'should get recommendations by analysis name'() {
    given:
    def command = RecommendationFixtures.GET_RECOMMENDATIONS_RESPONSE_COMMAND
    def recommendations = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]

    when:
    underTest.getRecommendationsResponse(command)

    then:
    1 * registrationService.getBatchWithAlerts(RecommendationFixtures.ANALYSIS_NAME) >>
        RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    1 * recommendationRepository.findByAnalysisName(RecommendationFixtures.ANALYSIS_NAME) >> recommendations
    1 * batchStatisticsService.createBatchStatistics(
        RecommendationFixtures.BATCH_WITH_ALERTS_DTO.alerts(), recommendations) >>
        RecommendationFixtures.BATCH_STATISTICS
  }
}

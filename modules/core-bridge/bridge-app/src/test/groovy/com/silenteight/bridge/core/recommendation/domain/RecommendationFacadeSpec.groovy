package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.command.ProceedDataRetentionOnRecommendationsCommand
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.AlertsStreamProperties
import com.silenteight.proto.recommendation.api.v1.RecommendationResponse

import io.grpc.stub.StreamObserver
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.stream.Stream

class RecommendationFacadeSpec extends Specification {

  def registrationService = Mock(RegistrationService)
  def recommendationProcessor = Mock(RecommendationProcessor)
  def recommendationRepository = Mock(RecommendationRepository)
  def batchStatisticsService = Mock(BatchStatisticsService)
  def alertsStreamProperties = new AlertsStreamProperties(1)
  def dataRetentionService = Mock(RecommendationsDataRetentionService)

  @Subject
  def underTest = new RecommendationFacade(
      registrationService,
      recommendationProcessor,
      recommendationRepository,
      batchStatisticsService,
      alertsStreamProperties,
      dataRetentionService
  )

  def 'should proceed ready recommendations'() {
    given:
    def command = RecommendationFixtures.READY_RECOMMENDATIONS_COMMAND

    when:
    underTest.proceedReadyRecommendations(command)

    then:
    1 * registrationService.getBatchPriority(RecommendationFixtures.ANALYSIS_NAME) >> RecommendationFixtures.BATCH_PRIORITY_DTO
    1 * recommendationProcessor
        .proceedReadyRecommendations(RecommendationFixtures.READY_RECOMMENDATIONS_COMMAND, RecommendationFixtures.BATCH_PRIORITY)
  }

  def 'should proceed batch timeout'() {
    given:
    def command = RecommendationFixtures.TIMED_OUT_RECOMMENDATIONS_COMMAND

    when:
    underTest.proceedBatchTimeout(command)

    then:
    1 * registrationService.getBatchPriority(RecommendationFixtures.ANALYSIS_NAME) >> RecommendationFixtures.BATCH_PRIORITY_DTO
    1 * recommendationProcessor.createTimedOutRecommendations(RecommendationFixtures.TIMED_OUT_RECOMMENDATIONS_COMMAND, RecommendationFixtures.BATCH_PRIORITY)
  }


  def 'should get recommendations by analysis name'() {
    given:
    def command = RecommendationFixtures.GET_RECOMMENDATIONS_RESPONSE_COMMAND
    def recommendations = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]

    when:
    underTest.getRecommendationsResponse(command)

    then:
    1 * registrationService.getBatchWithAlerts(
        RecommendationFixtures.ANALYSIS_NAME, command.alertNames()) >>
        RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    1 * recommendationRepository.findByAnalysisName(RecommendationFixtures.ANALYSIS_NAME) >> recommendations
    1 * batchStatisticsService.createBatchStatistics(
        RecommendationFixtures.BATCH_WITH_ALERTS_DTO.alerts(), recommendations) >>
        RecommendationFixtures.BATCH_STATISTICS
  }

  def 'should get recommendations by analysis name and alert names'() {
    given:
    def analysisName = RecommendationFixtures.ANALYSIS_NAME
    def alertNames = [RecommendationFixtures.ALERT_NAME]
    def command = RecommendationFixtures.GET_RECOMMENDATIONS_BY_ALERT_NAMES_RESPONSE_COMMAND
    def recommendations = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]

    when:
    underTest.getRecommendationsResponse(command)

    then:
    1 * registrationService.getBatchWithAlerts(
        RecommendationFixtures.ANALYSIS_NAME, command.alertNames()) >>
        RecommendationFixtures.BATCH_WITH_ALERTS_DTO
    1 * recommendationRepository.findByAnalysisNameAndAlertNameIn(analysisName, alertNames) >> recommendations
    1 * batchStatisticsService.createBatchStatistics(
        RecommendationFixtures.BATCH_WITH_ALERTS_DTO.alerts(), recommendations) >>
        RecommendationFixtures.BATCH_STATISTICS
  }

  @Unroll
  def 'should stream recommendation, #desc'() {
    given:
    def analysisName = RecommendationFixtures.ANALYSIS_NAME
    def alertNames = [RecommendationFixtures.ALERT_NAME]
    def command = RecommendationFixtures.GET_RECOMMENDATIONS_BY_ALERT_NAMES_RESPONSE_COMMAND
    def observer = Mock(StreamObserver<RecommendationResponse>)

    when:
    underTest.streamRecommendationResponses(command, observer)

    then:
    1 * registrationService.getBatchId(command.analysisName()) >> RecommendationFixtures.BATCH_ID_WITH_POLICY

    1 * registrationService.streamAllAlerts(
        RecommendationFixtures.BATCH_ID_WITH_POLICY.id(), command.alertNames()) >> alerts

    1 * batchStatisticsService.createBatchStatistics(
        RecommendationFixtures.BATCH_ID_WITH_POLICY.id(), analysisName, alertNames) >>
        RecommendationFixtures.BATCH_STATISTICS

    1 * registrationService.getAllMatchesForAlerts(Set.of(1L)) >> RecommendationFixtures.MATCHES_WITH_ALERTS_IDS
    recommendationCall * recommendationRepository.findByAnalysisNameAndAlertNameIn(analysisName, alertNames) >> List.of(RecommendationFixtures.RECOMMENDATION_WITH_METADATA)

    1 * observer.onNext(_)

    where:
    desc                  | alerts                                                                | recommendationCall
    "with alert names"    | Stream.of(RecommendationFixtures.ALERTS_WITHOUT_MATCHES)              | 1
    "without alert names" | Stream.of(RecommendationFixtures.ALERTS_WITHOUT_MATCHES_WITHOUT_NAME) | 0
  }

  def 'should call perform data retention'() {
    given:
    def command = new ProceedDataRetentionOnRecommendationsCommand(['1'])

    when:
    underTest.performDataRetention(command)

    then:
    dataRetentionService.performDataRetention(command)
  }
}

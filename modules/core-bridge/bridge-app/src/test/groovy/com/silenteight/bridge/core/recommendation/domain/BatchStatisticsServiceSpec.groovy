package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService

import spock.lang.Specification
import spock.lang.Subject

class BatchStatisticsServiceSpec extends Specification {

  def recommendationsStatisticsService = Mock(RecommendationsStatisticsService)

  def registrationService = Mock(RegistrationService)

  @Subject
  def underTest = new BatchStatisticsService(recommendationsStatisticsService, registrationService)

  def 'should create batch statistics'() {
    given:
    def alerts = [
        createAlertWithMatchesDtoWithStatus(AlertStatus.RECOMMENDED),
        createAlertWithMatchesDtoWithStatus(AlertStatus.DELIVERED),
        createAlertWithMatchesDtoWithStatus(AlertStatus.ERROR),
        createAlertWithMatchesDtoWithStatus(AlertStatus.ERROR),
    ]

    def recommendationsStatistics = RecommendationsStatistics.builder()
        .truePositiveCount(3)
        .falsePositiveCount(4)
        .manualInvestigationCount(5)
        .build()

    def recommendations = []

    and:
    1 * recommendationsStatisticsService.createRecommendationsStatistics(recommendations) >>
        recommendationsStatistics

    when:
    def result = underTest.createBatchStatistics(alerts, recommendations)

    then:
    with(result) {
      totalProcessedCount() == 2
      recommendedAlertsCount() == 2
      totalUnableToProcessCount() == 2
      with(recommendationsStats()) {
        truePositiveCount() == 3
        falsePositiveCount() == 4
        manualInvestigationCount() == 5
        errorCount() == 2
      }
    }
  }

  def 'should create batch statistics from DB'() {
    given:
    def batchId = "batchId/0"
    def analysisName = "analysis_name"
    def alertsName = List.of("alert_name/1", "alert_name/2", "alert_name/3", "alert_name/4")

    def alertStatistics = Map.of(
        AlertStatus.DELIVERED, 1L,
        AlertStatus.RECOMMENDED, 1L,
        AlertStatus.ERROR, 2L)

    def recommendationsStatistics = RecommendationsStatistics.builder()
        .truePositiveCount(3)
        .falsePositiveCount(4)
        .manualInvestigationCount(5)
        .build()

    when:
    def result = underTest.createBatchStatistics(batchId, analysisName, alertsName)

    then:
    1 * registrationService.getAlertsStatusStatistics(batchId, alertsName) >> alertStatistics
    1 * recommendationsStatisticsService
        .createRecommendationsStatistics(analysisName, alertsName) >> recommendationsStatistics

    with(result) {
      totalProcessedCount() == 2
      recommendedAlertsCount() == 2
      totalUnableToProcessCount() == 2
      with(recommendationsStats()) {
        truePositiveCount() == 3
        falsePositiveCount() == 4
        manualInvestigationCount() == 5
        errorCount() == 2
      }
    }
  }

  private static def createAlertWithMatchesDtoWithStatus(AlertStatus status) {
    AlertWithMatchesDto.builder()
        .status(status)
        .build()
  }
}

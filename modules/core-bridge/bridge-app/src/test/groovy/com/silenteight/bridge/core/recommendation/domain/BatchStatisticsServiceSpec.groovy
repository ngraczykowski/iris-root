package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics

import spock.lang.Specification
import spock.lang.Subject

class BatchStatisticsServiceSpec extends Specification {

  def recommendationsStatisticsService = Mock(RecommendationsStatisticsService)

  @Subject
  def underTest = new BatchStatisticsService(recommendationsStatisticsService)

  def 'should create batch statistics'() {
    given:
    def alerts = [
        createAlertWithMatchesDtoWithStatus(AlertStatus.RECOMMENDED),
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
      totalProcessedCount() == 1
      recommendedAlertsCount() == 1
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

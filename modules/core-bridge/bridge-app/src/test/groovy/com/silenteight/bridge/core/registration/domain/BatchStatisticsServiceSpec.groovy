package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.AlertStatusStatistics
import com.silenteight.bridge.core.registration.domain.model.RecommendationsStatistics
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.RecommendationsStatisticsService

import spock.lang.Specification
import spock.lang.Subject

class BatchStatisticsServiceSpec extends Specification {

  def alertRepository = Mock(AlertRepository)
  def recommendationsStatisticsService = Mock(RecommendationsStatisticsService)

  @Subject
  def underTest = new BatchStatisticsService(alertRepository, recommendationsStatisticsService)

  def 'should create batch completed statistics'() {
    given:
    def batchId = 'batchId';
    def analysisName = 'analysisName'
    def alertStatusStatistics = new AlertStatusStatistics(
        Map.of(
            AlertStatus.RECOMMENDED, 1,
            AlertStatus.ERROR, 2))

    def recommendationsStatistics = RecommendationsStatistics.builder()
        .truePositiveCount(3)
        .falsePositiveCount(4)
        .manualInvestigationCount(5)
        .build()

    and:
    1 * alertRepository.countAlertsByStatusForBatchId(batchId) >> alertStatusStatistics
    1 * recommendationsStatisticsService.createRecommendationsStatistics(analysisName) >> recommendationsStatistics

    when:
    def result = underTest.createBatchCompletedStatistics(batchId, analysisName)

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

  def 'should create batch error statistics'() {
    when:
    def result = underTest.createBatchErrorStatistics()

    then:
    with(result) {
      totalProcessedCount() == 0
      recommendedAlertsCount() == 0
      totalUnableToProcessCount() == 0
      with(recommendationsStats()) {
        truePositiveCount() == 0
        falsePositiveCount() == 0
        manualInvestigationCount() == 0
        errorCount() == 0
      }
    }
  }
}

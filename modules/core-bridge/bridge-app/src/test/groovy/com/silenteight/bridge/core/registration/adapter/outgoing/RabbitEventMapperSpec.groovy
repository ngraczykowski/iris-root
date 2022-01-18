package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted
import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics.RecommendationsStats

import spock.lang.Specification
import spock.lang.Subject

class RabbitEventMapperSpec extends Specification {

  @Subject
  def underTest = new RabbitEventMapper()

  def 'should map to message batch completed'() {
    given:
    def batchCompleted = BatchCompleted.builder()
        .id('batchId')
        .analysisId('analysisName')
        .alertIds(['firstAlertName', 'secondAlertName'])
        .batchMetadata('batchMetadata')
        .statistics(batchStatistics)
        .build()

    when:
    def result = underTest.toMessageBatchCompleted(batchCompleted)

    then:
    with(result) {
      batchId == 'batchId'
      analysisId == 'analysisName'
      alertIdsList.first() == 'firstAlertName'
      batchMetadata == 'batchMetadata'

      with(statistics) {
        totalProcessedCount == 1
        recommendedAlertsCount == 2
        totalUnableToProcessCount == 3

        with(recommendationsStatistics) {
          truePositiveCount == 4
          falsePositiveCount == 5
          manualInvestigationCount == 6
          errorCount == 7
        }
      }
    }
  }

  def 'should map to message batch error'() {
    given:
    def batchError = BatchError.builder()
        .id('batchId')
        .batchMetadata('batchMetadata')
        .errorDescription('Failed to register batch in Core Bridge Registration')
        .statistics(batchStatistics)
        .build()

    when:
    def result = underTest.toMessageBatchError(batchError)

    then:
    with(result) {
      batchId == 'batchId'
      batchMetadata == 'batchMetadata'
      errorDescription == 'Failed to register batch in Core Bridge Registration'

      with(statistics) {
        totalProcessedCount == 1
        recommendedAlertsCount == 2
        totalUnableToProcessCount == 3

        with(recommendationsStatistics) {
          truePositiveCount == 4
          falsePositiveCount == 5
          manualInvestigationCount == 6
          errorCount == 7
        }
      }
    }
  }

  def static recommendationsStats = RecommendationsStats.builder()
      .truePositiveCount(4)
      .falsePositiveCount(5)
      .manualInvestigationCount(6)
      .errorCount(7)
      .build()

  def static batchStatistics = BatchStatistics.builder()
      .totalProcessedCount(1)
      .recommendedAlertsCount(2)
      .totalUnableToProcessCount(3)
      .recommendationsStats(recommendationsStats)
      .build()
}

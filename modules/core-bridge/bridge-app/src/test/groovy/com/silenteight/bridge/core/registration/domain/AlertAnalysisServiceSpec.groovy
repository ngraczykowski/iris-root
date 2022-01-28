package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.AddAlertToAnalysisCommand.FedMatch
import com.silenteight.bridge.core.registration.domain.AddAlertToAnalysisCommand.FeedingStatus
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertName
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.Duration

class AlertAnalysisServiceSpec extends Specification {

  def batchRepository = Mock(BatchRepository)
  def analysisService = Mock(AnalysisService)
  def analysisProperties = new RegistrationAnalysisProperties(Duration.ofMinutes(10), false)
  def alertRepository = Mock(AlertRepository)

  @Subject
  def underTest = new AlertAnalysisService(batchRepository, analysisService, analysisProperties, alertRepository)

  def 'should process 1 success alert and 1 failed alert'() {
    given:
    def batch = createBatchWithStatus('batch1', BatchStatus.STORED)
    def succeededAlert = createAlert(
        batch.id(), 'alert1', ['succeededAlertMatch1', 'succeededAlertMatch2']
    )
    def failedAlert = createAlert(batch.id(), 'alert2', ['failedAlertMatch'])

    def commands = [
        AddAlertToAnalysisCommand.builder()
            .batchId(batch.id())
            .alertId(succeededAlert.alertId())
            .feedingStatus(FeedingStatus.SUCCESS)
            .fedMatches([new FedMatch('succeededAlertMatch1',)])
            .build(),
        AddAlertToAnalysisCommand.builder()
            .batchId(batch.id())
            .alertId(failedAlert.alertId())
            .feedingStatus(FeedingStatus.FAILURE)
            .fedMatches([new FedMatch('failedAlertMatch')])
            .build()
    ]

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    with(batchRepository) {
      1 * findById(batch.id()) >> Optional.of(batch)
      1 * updateStatus(batch.id(), BatchStatus.PROCESSING)
    }
    1 * analysisService.addAlertsToAnalysis(batch.analysisName(), [succeededAlert.name()], _)
    with(alertRepository) {
      1 * updateStatusToProcessing(batch.id(), [succeededAlert.alertId()])
      1 * updateStatusToError(batch.id(), [failedAlert.alertId()])
      1 * findAllAlertNamesByBatchIdAndAlertIdIn(batch.id(), [succeededAlert.alertId()]) >>
          [new AlertName(succeededAlert.name())]
    }
    0 * _
  }

  @Unroll
  def "should not process alert due to its batch status: #batchStatus"() {
    given:
    def batch = createBatchWithStatus('batch1', batchStatus)
    def commands = [
        AddAlertToAnalysisCommand.builder()
            .batchId(batch.id())
            .alertId('alert1')
            .feedingStatus(FeedingStatus.SUCCESS)
            .build()
    ]

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    0 * _

    where:
    batchStatus << [BatchStatus.ERROR, BatchStatus.COMPLETED, BatchStatus.DELIVERED]
  }

  private static def createBatchWithStatus(String id, BatchStatus status) {
    Batch.builder()
        .id(id)
        .status(status)
        .analysisName("analysis of batch $id")
        .build()
  }

  private static def createAlert(String batchId, String alertId, Collection<String> matchIds) {
    def matches = matchIds.collect(
        matchId -> new Match(matchId, matchId))

    Alert.builder()
        .batchId(batchId)
        .alertId(alertId)
        .name("alert/$alertId")
        .matches(matches)
        .build()
  }
}

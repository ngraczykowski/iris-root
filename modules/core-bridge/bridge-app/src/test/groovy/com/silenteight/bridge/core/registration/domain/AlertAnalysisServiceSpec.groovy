package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FedMatch
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FeedingStatus
import com.silenteight.bridge.core.registration.domain.model.Alert
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
  def underTest = new AlertAnalysisService(
      batchRepository,
      analysisService,
      analysisProperties,
      alertRepository)

  def 'should process 1 success alert and 1 failed alert'() {
    given:
    def batch = createBatchWithStatus('batch_1', BatchStatus.STORED)
    def succeededAlert = createAlert(
        batch.id(), 'alert_1', ['match_1', 'match_2'], '')
    def failedAlert = createAlert(
        batch.id(), 'alert_2', ['match_3'], 'Failed to flatten alert payload.')

    def commands = [
        AddAlertToAnalysisCommand.builder()
            .batchId(batch.id())
            .alertName(succeededAlert.name())
            .errorDescription(succeededAlert.errorDescription())
            .feedingStatus(FeedingStatus.SUCCESS)
            .fedMatches([new FedMatch('match_1'), new FedMatch('match_2')])
            .build(),
        AddAlertToAnalysisCommand.builder()
            .batchId(batch.id())
            .alertName(failedAlert.name())
            .errorDescription(failedAlert.errorDescription())
            .feedingStatus(FeedingStatus.FAILURE)
            .fedMatches([new FedMatch('match_3')])
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
      1 * updateStatusToProcessing(batch.id(), [succeededAlert.name()])
      1 * updateStatusToError(batch.id(), Map.of('Failed to flatten alert payload.', [failedAlert.name()] as Set<String>))
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
            .alertName('alerts/1')
            .errorDescription('')
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

  private static def createAlert(
      String batchId, String alertId, Collection<String> matchIds, String errorDescription) {
    def matches = matchIds.collect(
        matchId -> new Match("matches/$matchId", matchId)
    )

    Alert.builder()
        .batchId(batchId)
        .alertId(alertId)
        .errorDescription(errorDescription)
        .name("alerts/$alertId")
        .matches(matches)
        .build()
  }
}

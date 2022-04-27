package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand
import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand.FedMatch
import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand.FeedingStatus
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.domain.strategy.BatchStrategyFactory
import com.silenteight.bridge.core.registration.domain.strategy.UdsFedAlertsProcessorStrategy

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class UdsFedAlertsServiceSpec extends Specification {

  def batchRepository = Mock(BatchRepository)
  def alertRepository = Mock(AlertRepository)
  def batchStrategyFactory = Mock(BatchStrategyFactory)
  def udsFedAlertsProcessorStrategy = Mock(UdsFedAlertsProcessorStrategy)
  def batchService = Mock(BatchService)
  def alertService = Mock(AlertService)

  @Subject
  def underTest = new UdsFedAlertsService(batchRepository, alertRepository, batchStrategyFactory, alertService, batchService)

  def 'should process 1 success alert and 1 failed alert and #desc'() {
    given:
    def batch = createBatchWithStatus('batch_1', BatchStatus.STORED)
    def succeededAlert = createAlert(
        batch.id(), 'alert_1', ['match_1', 'match_2'], '')
    def failedAlert = createAlert(
        batch.id(), 'alert_2', ['match_3'], 'Failed to flatten alert payload.')

    def commands = [
        ProcessUdsFedAlertsCommand.builder()
            .batchId(batch.id())
            .alertName(succeededAlert.name())
            .errorDescription(succeededAlert.errorDescription())
            .feedingStatus(FeedingStatus.SUCCESS)
            .fedMatches([new FedMatch('match_1'), new FedMatch('match_2')])
            .build(),
        ProcessUdsFedAlertsCommand.builder()
            .batchId(batch.id())
            .alertName(failedAlert.name())
            .errorDescription(failedAlert.errorDescription())
            .feedingStatus(FeedingStatus.FAILURE)
            .fedMatches([new FedMatch('match_3')])
            .build()
    ]

    when:
    underTest.processUdsFedAlerts(commands)

    then:
    with(batchRepository) {
      1 * findById(batch.id()) >> Optional.of(batch)
      1 * updateStatus(batch.id(), BatchStatus.PROCESSING)
    }
    1 * batchStrategyFactory.getStrategyForUdsFedAlertsProcessor(batch) >> udsFedAlertsProcessorStrategy
    1 * udsFedAlertsProcessorStrategy.processUdsFedAlerts(batch, _ as List<String>)
    with(alertRepository) {
      1 * updateStatusToError(
          batch.id(), Map.of('Failed to flatten alert payload.', [failedAlert.name()] as Set<String>))
    }
    1 * alertService.hasNoPendingAlerts(batch) >> hasNoPendingAlerts
    if (hasNoPendingAlerts) {
      1 * batchService.completeBatch(batch)
    }
    0 * _

    where:
    hasNoPendingAlerts | desc
    true               | "should complete Batch"
    false              | "should not complete Batch"
  }

  @Unroll
  def "should not process alert due to its batch status: #batchStatus"() {
    given:
    def batch = createBatchWithStatus('batch1', batchStatus)
    def commands = [
        ProcessUdsFedAlertsCommand.builder()
            .batchId(batch.id())
            .alertName('alerts/1')
            .errorDescription('')
            .feedingStatus(FeedingStatus.SUCCESS)
            .build()
    ]

    when:
    underTest.processUdsFedAlerts(commands)

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

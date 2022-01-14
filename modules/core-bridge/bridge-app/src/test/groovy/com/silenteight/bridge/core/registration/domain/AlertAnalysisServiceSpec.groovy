package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Alert.Status
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.MatchRepository
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties

import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class AlertAnalysisServiceSpec extends Specification {

  def batchRepository = Mock(BatchRepository)
  def analysisService = Mock(AnalysisService)
  def analysisProperties = new RegistrationAnalysisProperties(Duration.ofMinutes(10))
  def alertRepository = Mock(AlertRepository)
  def matchRepository = Mock(MatchRepository)

  @Subject
  def underTest = new AlertAnalysisService(
      batchRepository, analysisService, analysisProperties, alertRepository, matchRepository)

  def 'should add alerts to analysis per batch'() {
    given: 'create 3 processable batches'
    def batches = [
        createBatchWithStatus('batch1', BatchStatus.STORED),
        createBatchWithStatus('batch2', BatchStatus.PROCESSING)
    ]

    and: 'create 3 commands per each batch'
    Map<String, List<AddAlertToAnalysisCommand>> batchCommandsMap = [:]
    def commands = []
    batches.each {batch ->
      batchCommandsMap[batch.id()] = []
      (1..3).each {i ->
        def command = createAddAlertToAnalysisCommand(batch.id())
        batchCommandsMap[batch.id()].add(command)
        commands.add(command)
      }
    }

    and: 'create 3 alerts per each batch'
    Map<String, List<Alert>> batchAlertsMap = [:]
    batches.each {batch ->
      def batchAlerts = batchCommandsMap[batch.id()]
          .collect {createAlert(batch.id(), it.alertId(), it.matchIds())}
      batchAlertsMap[batch.id()] = batchAlerts
    }

    and: 'mock invocations'
    batches.each {batch ->
      1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
      1 * alertRepository.findAllByBatchIdAndAlertIdIn(batch.id(), _) >> batchAlertsMap[batch.id()]
    }

    when:
    underTest.addAlertsToAnalysis(commands)

    then: 'should update batch with initial status STORED'
    1 * batchRepository.updateStatus('batch1', BatchStatus.PROCESSING)

    and: 'should add alerts to analysis and update statuses'
    batches.each {batch ->
      def alertNames = batchAlertsMap[batch.id()].collect {it.name()}
      1 * analysisService.addAlertsToAnalysis(batch.analysisName(), alertNames, _)

      def alertIds = batchCommandsMap[batch.id()].collect {it.alertId()}
      1 * alertRepository.updateStatusByBatchIdAndAlertIdIn(Status.PROCESSING, batch.id(), alertIds)

      def matchIds = batchAlertsMap[batch.id()].collect {it.matches().collect {it.name()}}.flatten()
      1 * matchRepository.updateStatusByNameIn(Match.Status.PROCESSING, matchIds)
      0 * matchRepository.updateStatusByNameIn(Match.Status.ERROR, _)
    }
  }

  def 'should not process alerts due to their batch status'() {
    given: 'create batches'
    def batches = [
        createBatchWithStatus('batch1', BatchStatus.ERROR),
        createBatchWithStatus('batch2', BatchStatus.COMPLETED),
        createBatchWithStatus('batch3', BatchStatus.DELIVERED),
    ]

    and: 'create commands per each batch'
    def commands = []
    batches.each {batch ->
      def command = createAddAlertToAnalysisCommand(batch.id())
      commands.add(command)
    }

    and: 'mock invocations'
    batches.each {batch ->
      1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    }

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    0 * batchRepository.updateStatus(_, _)
    0 * analysisService.addAlertsToAnalysis(_, _)
  }

  def "should set 1 match status to ERROR and 1 to PROCESSING"() {
    given:
    def batch = createBatchWithStatus("batch1", BatchStatus.STORED)
    def commands = [createAddAlertToAnalysisCommand(batch.id())]
    def nonExistingInCommandMatchId = UUID.randomUUID().toString()
    def alert = Alert.builder()
        .alertId(commands.first().alertId())
        .name('alert/123')
        .matches(
            [
                Match.builder()
                    .matchId(commands.first().matchIds().first())
                    .name('exists_in_command')
                    .build(),
                Match.builder()
                    .matchId(nonExistingInCommandMatchId)
                    .name('not_exists_in_command')
                    .build()
            ])
        .build()

    and: 'mock invocations'
    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    1 * alertRepository.findAllByBatchIdAndAlertIdIn(_, _) >> [alert]

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    1 * batchRepository.updateStatus(batch.id(), BatchStatus.PROCESSING)
    1 * analysisService.addAlertsToAnalysis(batch.analysisName(), [alert.name()], _)
    1 * alertRepository
        .updateStatusByBatchIdAndAlertIdIn(Status.PROCESSING, batch.id(), [alert.alertId()])
    1 * matchRepository.updateStatusByNameIn(Match.Status.PROCESSING, ['exists_in_command'])
    1 * matchRepository.updateStatusByNameIn(Match.Status.ERROR, ['not_exists_in_command'])
  }

  private static def createAddAlertToAnalysisCommand(def batchId) {
    new AddAlertToAnalysisCommand(
        batchId,
        'alert_id_' + UUID.randomUUID(),
        ['match_id_' + UUID.randomUUID()].toSet()
    )
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
        matchId ->
            Match.builder()
                .matchId(matchId)
                .name(matchId)
                .build()
    )
    Alert.builder()
        .batchId(batchId)
        .alertId(alertId)
        .matches(matches)
        .build()
  }
}

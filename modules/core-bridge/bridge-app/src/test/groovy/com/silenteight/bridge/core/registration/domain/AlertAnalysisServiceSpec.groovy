package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties

import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class AlertAnalysisServiceSpec extends Specification {

  def batchRepository = Mock(BatchRepository)
  def analysisService = Mock(AnalysisService)
  def analysisProperties = new RegistrationAnalysisProperties(Duration.ofMinutes(10))

  @Subject
  def underTest = new AlertAnalysisService(batchRepository, analysisService, analysisProperties)

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

    and: 'mock invocations'
    batches.each {batch ->
      1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    }

    when:
    underTest.addAlertsToAnalysis(commands)

    then: 'should update batch with initial status STORED'
    1 * batchRepository.updateStatus('batch1', BatchStatus.PROCESSING)

    and: 'should add alerts to analysis'
    batches.each {batch ->
      1 * analysisService.addAlertsToAnalysis(batch.analysisName(), batchCommandsMap[batch.id()], _)
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

  private static def createAddAlertToAnalysisCommand(def batchId) {
    new AddAlertToAnalysisCommand(
        batchId,
        'alertId',
        ['matchId'].toSet()
    )
  }

  private static def createBatchWithStatus(String id, BatchStatus status) {
    Batch.builder()
        .id(id)
        .status(status)
        .build()
  }
}

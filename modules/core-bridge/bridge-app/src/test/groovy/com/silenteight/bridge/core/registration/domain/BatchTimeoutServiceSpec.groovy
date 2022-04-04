package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand
import com.silenteight.bridge.core.registration.domain.model.AlertName
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchCompleted
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.stream.IntStream

import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.*

class BatchTimeoutServiceSpec extends Specification {

  def batchRepository = Mock(BatchRepository)
  def alertRepository = Mock(AlertRepository)
  def batchEventPublisher = Mock(BatchEventPublisher)

  @Subject
  def underTest = new BatchTimeoutService(batchRepository, alertRepository, batchEventPublisher)

  @Unroll
  def "#desc"() {
    def batch = createBatch(PROCESSING, alertsCount)
    def command = new VerifyBatchTimeoutCommand(batch.id())
    def alertNames = IntStream.range(0, pendingAlerts)
        .collect({it -> "alert" + it})

    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    1 * alertRepository.findNamesByBatchIdAndStatusIsRegisteredOrProcessing(batch.id()) >>
        alertNames.collect {new AlertName(it)}
    1 * alertRepository.countAllAlerts(batch.id()) >> registeredAlerts

    when:
    underTest.verifyBatchTimeout(command)

    then:
    batchTimedOut * batchEventPublisher.publish(new BatchTimedOut(batch.analysisName(), alertNames))
    batchCompleted * batchEventPublisher.publish(
        new BatchCompleted(batch.id(), batch.analysisName(), batch.batchMetadata()))

    where:
    desc                                                                                                                                      | pendingAlerts | alertsCount | registeredAlerts | batchTimedOut | batchCompleted
    "Should not send TimedOut event when no pending alerts, should not mark batch as completed when quantity of alerts = alertsCount"         | 0             | 10          | 10               | 0            | 0
    "Should send TimedOut event when pending alerts are present, should not mark batch as completed when quantity of alerts = alertsCount"    | 3             | 10          | 10               | 1            | 0
    "Should send TimedOut event when pending alerts are present, should mark batch as completed when quantity of alerts < alertsCount"        | 3             | 10          | 3                | 1            | 1
    "Should not send TimedOut event when no pending alerts, should not mark batch as completed when quantity of alerts < alertsCount"         | 0             | 10          | 3                | 0            | 1
  }

  def "should ignore batch due to its status #status"() {
    given:
    def batch = createBatch(status, 10)
    def command = new VerifyBatchTimeoutCommand(batch.id())
    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)

    when:
    underTest.verifyBatchTimeout(command)

    then:
    noExceptionThrown()
    0 * batchEventPublisher._

    where:
    status << [COMPLETED, DELIVERED]
  }

  def "should do nothing when batch with given id does not exist"() {
    given:
    def batchId = Fixtures.BATCH_ID
    def command = new VerifyBatchTimeoutCommand(batchId)
    1 * batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.verifyBatchTimeout(command)

    then:
    noExceptionThrown()
    0 * batchEventPublisher._
  }

  def "should not publish message when no pending alerts were found"() {
    given:
    def batch = createBatch(PROCESSING, 10)
    def command = new VerifyBatchTimeoutCommand(batch.id())

    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    1 * alertRepository.findNamesByBatchIdAndStatusIsRegisteredOrProcessing(batch.id()) >> []
    1 * alertRepository.countAllAlerts(batch.id()) >> 3

    when:
    underTest.verifyBatchTimeout(command)

    then:
    0 * batchEventPublisher.publish(BatchTimedOut)
    1 * batchEventPublisher
        .publish(new BatchCompleted(batch.id(), batch.analysisName(), batch.batchMetadata()))
  }

  def "should mark batch with status #status as completed and publish batch completed when all alerts are erroneous"() {
    given:
    def alertsCount = 10
    def batch = createBatch(status, alertsCount)
    def batchId = batch.id()
    def expectedBatchCompletedEvent = BatchCompleted.builder()
        .id(batch.id())
        .analysisId(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .build()
    def command = new VerifyBatchTimeoutCommand(batchId)

    1 * batchRepository.findById(batchId) >> Optional.of(batch)
    1 * alertRepository.countAllErroneousAlerts(batchId) >> alertsCount
    1 * batchRepository.updateStatusToCompleted(batchId)

    when:
    underTest.verifyBatchTimeoutForAllErroneousAlerts(command)

    then:
    1 * batchEventPublisher.publish(expectedBatchCompletedEvent)

    where:
    status << [STORED, PROCESSING]
  }

  def 'should ignore batch timeout when erroneous alerts are less than total alerts count'() {
    given:
    def alertsCount = 10
    def batch = createBatch(status, alertsCount)
    def batchId = batch.id()
    def command = new VerifyBatchTimeoutCommand(batchId)
    def expectedBatchCompletedEvent = BatchCompleted.builder()
        .id(batch.id())
        .analysisId(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .build()

    1 * batchRepository.findById(batchId) >> Optional.of(batch)
    1 * alertRepository.countAllErroneousAlerts(batchId) >> 4
    0 * batchRepository.updateStatusToCompleted(batchId)

    when:
    underTest.verifyBatchTimeoutForAllErroneousAlerts(command)

    then:
    0 * batchEventPublisher.publish(expectedBatchCompletedEvent)

    where:
    status << [STORED, PROCESSING]
  }

  def "should ignore batch timeout when it is in #status status"() {
    given:
    def alertsCount = 10
    def batch = createBatch(COMPLETED, alertsCount)
    def batchId = batch.id()
    def command = new VerifyBatchTimeoutCommand(batchId)
    def expectedBatchCompletedEvent = BatchCompleted.builder()
        .id(batch.id())
        .analysisId(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .build()

    1 * batchRepository.findById(batchId) >> Optional.of(batch)
    0 * alertRepository.countAllErroneousAlerts(batchId)
    0 * batchRepository.updateStatusToCompleted(batchId)

    when:
    underTest.verifyBatchTimeoutForAllErroneousAlerts(command)

    then:
    0 * batchEventPublisher.publish(expectedBatchCompletedEvent)

    where:
    status << [ERROR, DELIVERED]
  }

  private static def createBatch(BatchStatus status, long alertsCount) {
    Batch.builder()
        .id(Fixtures.BATCH_ID)
        .analysisName('analysis/test')
        .status(status)
        .alertsCount(alertsCount)
        .build()
  }
}

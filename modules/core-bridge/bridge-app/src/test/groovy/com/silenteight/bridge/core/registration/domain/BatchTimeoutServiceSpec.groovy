package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand
import com.silenteight.bridge.core.registration.domain.model.AlertName
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.SolvingBatchCompleted
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.*

class BatchTimeoutServiceSpec extends Specification {

  def batchRepository = Mock(BatchRepository)
  def alertRepository = Mock(AlertRepository)
  def batchEventPublisher = Mock(BatchEventPublisher)

  @Subject
  def underTest = new BatchTimeoutService(batchRepository, alertRepository, batchEventPublisher)

  def "should publish BatchTimedOut event for batch with pending alerts"() {
    given:
    def batch = createBatch(PROCESSING, 10)
    def command = new VerifyBatchTimeoutCommand(batch.id())
    def alertNames = ['alert1', 'alert2', 'alert3']

    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    1 * alertRepository.findNamesByBatchIdAndStatusIsRegisteredOrProcessing(batch.id()) >>
        alertNames.collect {new AlertName(it)}

    when:
    underTest.verifyBatchTimeout(command)

    then:
    1 * batchEventPublisher.publish(new BatchTimedOut(batch.analysisName(), alertNames))
    0 * batchEventPublisher
        .publish(new SolvingBatchCompleted(batch.id(), batch.analysisName(), batch.batchMetadata()))
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

  def "should not publish BatchTimedOut message when no pending alerts were found, should publish BatchCompleted message when number of registered alert is lower that alerts count"() {
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
        .publish(new SolvingBatchCompleted(batch.id(), batch.analysisName(), batch.batchMetadata()))
  }

  def "should not publish BatchTimedOut and BatchCompleted messages"() {
    given:
    def batch = createBatch(PROCESSING, 10)
    def command = new VerifyBatchTimeoutCommand(batch.id())

    1 * batchRepository.findById(batch.id()) >> Optional.of(batch)
    1 * alertRepository.findNamesByBatchIdAndStatusIsRegisteredOrProcessing(batch.id()) >> []
    1 * alertRepository.countAllAlerts(batch.id()) >> 10

    when:
    underTest.verifyBatchTimeout(command)

    then:
    0 * batchEventPublisher.publish(BatchTimedOut)
    0 * batchEventPublisher
        .publish(new SolvingBatchCompleted(batch.id(), batch.analysisName(), batch.batchMetadata()))
  }

  def "should mark batch with status #status as completed and publish batch completed when all alerts are erroneous"() {
    given:
    def alertsCount = 10
    def batch = createBatch(status, alertsCount)
    def batchId = batch.id()
    def expectedBatchCompletedEvent = SolvingBatchCompleted.builder()
        .id(batch.id())
        .analysisName(batch.analysisName())
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
    def expectedBatchCompletedEvent = SolvingBatchCompleted.builder()
        .id(batch.id())
        .analysisName(batch.analysisName())
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
    def expectedBatchCompletedEvent = SolvingBatchCompleted.builder()
        .id(batch.id())
        .analysisName(batch.analysisName())
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

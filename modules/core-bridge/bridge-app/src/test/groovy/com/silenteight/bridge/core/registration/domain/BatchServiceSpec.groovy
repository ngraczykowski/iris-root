package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.model.*
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.*
import com.silenteight.bridge.core.registration.domain.strategy.BatchCompletionStrategy
import com.silenteight.bridge.core.registration.domain.strategy.BatchRegistrationStrategy
import com.silenteight.bridge.core.registration.domain.strategy.BatchStrategyFactory

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class BatchServiceSpec extends Specification {

  def eventPublisher = Mock(BatchEventPublisher)
  def batchRepository = Mock(BatchRepository)
  def batchStrategyFactory = Mock(BatchStrategyFactory)
  def batchRegistrationStrategy = Mock(BatchRegistrationStrategy)
  def batchStatisticsRepository = Mock(BatchStatisticsRepository)

  @Subject
  def underTest = new BatchService(eventPublisher, batchRepository, batchStrategyFactory, batchStatisticsRepository)

  def 'should register batch'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_BATCH_COMMAND

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    1 * batchStrategyFactory.getStrategyForRegistration(registerBatchCommand) >> batchRegistrationStrategy
    1 * batchRegistrationStrategy.register(registerBatchCommand) >> RegistrationFixtures.BATCH
  }

  def 'should return batch if already exists'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_BATCH_COMMAND
    def existedBatch = RegistrationFixtures.batch(BatchStatus.STORED)

    and:
    batchRepository.findById(batchId) >> Optional.of(existedBatch)

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    0 * batchStrategyFactory.getStrategyForRegistration(registerBatchCommand)
    0 * batchRegistrationStrategy.register(registerBatchCommand)
  }

  @Unroll
  def 'should not register batch if already exists and has not allowed status: `#status`'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_BATCH_COMMAND
    def registeredBatch = RegistrationFixtures.batch(status)

    and:
    batchRepository.findById(batchId) >> Optional.of(registeredBatch)

    when:
    underTest.register(registerBatchCommand)

    then:
    thrown(IllegalStateException.class)

    where:
    status << [null, BatchStatus.ERROR, BatchStatus.DELIVERED, BatchStatus.COMPLETED]
  }

  def 'should call updateStatusAndErrorDescription method with status error and error description when batch exists'() {
    given:
    def batchId = Fixtures.BATCH_ID

    and:
    batchRepository.findById(batchId) >> Optional.of(RegistrationFixtures.BATCH)

    when:
    underTest.notifyBatchError(RegistrationFixtures.NOTIFY_BATCH_ERROR_COMMAND)

    then:
    1 * batchRepository.updateStatusAndErrorDescription(
        batchId, BatchStatus.ERROR, RegistrationFixtures.ERROR_DESCRIPTION)
    1 * eventPublisher.publish(RegistrationFixtures.BATCH_ERROR)
    0 * batchRepository.create(_ as Batch)
  }

  def 'should create new batch as error when batch does not exist'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def errorDescription = RegistrationFixtures.ERROR_DESCRIPTION
    def metadata = RegistrationFixtures.METADATA
    def isSimulation = true

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.notifyBatchError(RegistrationFixtures.NOTIFY_BATCH_ERROR_COMMAND)

    then:
    1 * batchRepository.create(_ as Batch) >> Batch.error(batchId, errorDescription, metadata, isSimulation)
    1 * eventPublisher.publish(RegistrationFixtures.BATCH_ERROR)
    0 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR)
  }

  def 'should find batch by analysis name'() {
    given:
    def analysisName = RegistrationFixtures.ANALYSIS_NAME

    when:
    def result = underTest.findBatchByAnalysisName(analysisName)

    then:
    1 * batchRepository.findBatchByAnalysisName(analysisName) >>
        Optional.of(RegistrationFixtures.BATCH)
    result == RegistrationFixtures.BATCH
  }

  def 'should find batch by id'() {
    given:
    def analysisName = Fixtures.BATCH_ID

    when:
    def result = underTest.findBatchById(analysisName)

    then:
    1 * batchRepository.findById(analysisName) >> Optional.of(RegistrationFixtures.BATCH)
    result == RegistrationFixtures.BATCH
  }

  def 'should find batch priority with status by batch id'() {
    given:
    def batchId = Fixtures.BATCH_ID

    when:
    def result = underTest.findPendingBatch(batchId)

    then:
    1 * batchRepository.findBatchPriorityById(batchId) >>
        Optional.of(new BatchPriorityWithStatus(RegistrationFixtures.BATCH_PRIORITY, BatchStatus.STORED))
    result.priority() == RegistrationFixtures.BATCH_PRIORITY
  }

  def 'should throw exception for findPendingBatch method when batch is in incorrect status'() {
    given:
    def batchId = Fixtures.BATCH_ID

    when:
    underTest.findPendingBatch(batchId)

    then:
    1 * batchRepository.findBatchPriorityById(batchId) >>
        Optional.of(new BatchPriorityWithStatus(RegistrationFixtures.BATCH_PRIORITY, BatchStatus.DELIVERED))

    def exception = thrown(IllegalStateException)
    exception.getMessage().contains("[DELIVERED] status is invalid, one of [STORED, PROCESSING] expected.")
  }

  def 'should find batch with policy projection by analysis name'() {
    given:
    def analysisName = RegistrationFixtures.ANALYSIS_NAME

    when:
    def result = underTest.findBatchIdWithPolicyByAnalysisName(analysisName)

    then:
    1 * batchRepository.findBatchIdWithPolicyByAnalysisName(analysisName) >>
        Optional.of(RegistrationFixtures.BATCH_ID_WITH_POLICY_PROJECTION)
    result == RegistrationFixtures.BATCH_ID_WITH_POLICY_PROJECTION
  }


  def 'should throw NoSuchElementException when can not find a batch by analysis name'() {
    given:
    def analysisName = 'notExistingAnalysisName'

    when:
    underTest.findBatchByAnalysisName(analysisName)

    then:
    1 * batchRepository.findBatchByAnalysisName(analysisName) >> Optional.empty()
    thrown(NoSuchElementException.class)
  }

  def 'should completed batch when batch exists'() {
    given:
    def batch = RegistrationFixtures.BATCH
    def batchCompletionStrategy = Mock(BatchCompletionStrategy)

    when:
    underTest.completeBatch(batch)

    then:
    1 * batchStrategyFactory.getStrategyForCompletion(batch) >> batchCompletionStrategy
    1 * batchCompletionStrategy.completeBatch(batch)
  }

  @Unroll
  def 'should update batch with status #status to DELIVERED'() {
    given:
    def batch = RegistrationFixtures.batch(status)

    and:
    batchRepository.findById(batch.id()) >> Optional.of(batch)

    when:
    underTest.markBatchAsDelivered(batch)

    then:
    1 * batchRepository.updateStatusToDelivered(batch.id())

    where:
    status << BatchService.ALLOWED_BATCH_STATUSES_FOR_MARKING_AS_DELIVERED
  }

  def 'should not update batch with status #status to DELIVERED'() {
    given:
    def batch = RegistrationFixtures.batch(status as BatchStatus)

    and:
    batchRepository.findById(batch.id()) >> Optional.of(batch)

    when:
    underTest.markBatchAsDelivered(batch)

    then:
    0 * batchRepository.updateStatusToDelivered(batch.id())
    thrown(IllegalStateException.class)

    where:
    status << BatchStatus.findAll {
      !BatchService.ALLOWED_BATCH_STATUSES_FOR_MARKING_AS_DELIVERED.contains(it)
    }
  }

  def 'should find batch priority for analysis name'() {
    given:
    def analysisName = RegistrationFixtures.ANALYSIS_NAME

    when:
    def result = underTest.findBatchPriority(analysisName)

    then:
    1 * batchRepository.findBatchPriorityByAnalysisName(analysisName) >> new BatchPriority(RegistrationFixtures.BATCH_PRIORITY)

    result.priority() == RegistrationFixtures.BATCH_PRIORITY
  }
}

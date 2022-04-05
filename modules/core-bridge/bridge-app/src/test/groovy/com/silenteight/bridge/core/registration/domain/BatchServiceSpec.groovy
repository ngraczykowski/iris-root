package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.command.CompleteBatchCommand
import com.silenteight.bridge.core.registration.domain.model.*
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.*

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class BatchServiceSpec extends Specification {

  def eventPublisher = Mock(BatchEventPublisher)
  def analysisService = Mock(AnalysisService)
  def batchRepository = Mock(BatchRepository)
  def modelService = Mock(DefaultModelService)
  def verifyBatchTimeoutPublisher = Mock(VerifyBatchTimeoutPublisher)

  @Subject
  def underTest = new BatchService(
      eventPublisher, analysisService, batchRepository, modelService, verifyBatchTimeoutPublisher)

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
    1 * modelService.getForSolving() >> DefaultModel.builder().build()
    1 * analysisService.create(_ as DefaultModel) >>
        new Analysis(RegistrationFixtures.ANALYSIS_NAME)
    1 * batchRepository.create(_ as Batch) >> RegistrationFixtures.BATCH
    1 * verifyBatchTimeoutPublisher.publish(new VerifyBatchTimeoutEvent(batchId))
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
    0 * modelService.getForSolving()
    0 * analysisService.create(_ as DefaultModel)
    0 * batchRepository.create(_ as Batch)
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

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.notifyBatchError(RegistrationFixtures.NOTIFY_BATCH_ERROR_COMMAND)

    then:
    1 * batchRepository.create(_ as Batch) >> Batch.error(batchId, errorDescription, metadata)
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
    exception.getMessage().contains("DELIVERED status is invalid, one of [STORED, PROCESSING] expected.")
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

  def 'should update batch status as COMPLETED and publish message when batch exists'() {
    given:
    def command = new CompleteBatchCommand(RegistrationFixtures.BATCH)
    def batchCompleted = new BatchCompleted(
        Fixtures.BATCH_ID,
        RegistrationFixtures.ANALYSIS_NAME,
        RegistrationFixtures.METADATA
    )

    when:
    underTest.completeBatch(command)

    then:
    1 * batchRepository.updateStatusToCompleted(command.batch().id())
    1 * eventPublisher.publish(batchCompleted)
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
}

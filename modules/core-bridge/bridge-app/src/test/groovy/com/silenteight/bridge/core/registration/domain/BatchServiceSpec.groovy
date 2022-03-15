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
    def result = underTest.findBatchId(analysisName)

    then:
    1 * batchRepository.findBatchIdByAnalysisName(analysisName) >>
        Optional.of(RegistrationFixtures.BATCH_ID_PROJECTION)
    result == RegistrationFixtures.BATCH_ID_PROJECTION
  }

  def 'should find batch priority by analysis name'() {
    given:
    def batchId = Fixtures.BATCH_ID

    when:
    def result = underTest.findBatchPriority(batchId)

    then:
    1 * batchRepository.findBatchPriorityById(batchId) >>
        Optional.of(new BatchPriority(RegistrationFixtures.BATCH_PRIORITY))
    result.priority() == RegistrationFixtures.BATCH_PRIORITY
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
    underTest.findBatchId(analysisName)

    then:
    1 * batchRepository.findBatchIdByAnalysisName(analysisName) >> Optional.empty()
    thrown(NoSuchElementException.class)
  }

  def 'should update batch status as COMPLETED and publish message when batch exists'() {
    given:
    def alertNames = ['firstAlertName', 'secondAlertName']
    def command = new CompleteBatchCommand(Fixtures.BATCH_ID, alertNames)
    def batchCompleted = new BatchCompleted(
        Fixtures.BATCH_ID,
        RegistrationFixtures.ANALYSIS_NAME,
        RegistrationFixtures.METADATA,
        alertNames
    )

    and:
    batchRepository.findById(Fixtures.BATCH_ID) >> Optional.of(RegistrationFixtures.BATCH)

    when:
    underTest.completeBatch(command)

    then:
    1 * batchRepository.updateStatusToCompleted(command.id())
    1 * eventPublisher.publish(batchCompleted)
  }

  def 'should not update batch status as COMPLETED and publish message when batch does not exists'() {
    given:
    def batchId = 'notExistingBatchId'
    def command = new CompleteBatchCommand(batchId, [])

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.completeBatch(command)

    then:
    0 * batchRepository.updateStatusToCompleted(_ as CompleteBatchCommand)
    0 * eventPublisher.publish(_ as BatchCompleted)
  }

  def 'should update batch status as DELIVERED'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def batch = RegistrationFixtures.batch(BatchStatus.COMPLETED)

    and:
    batchRepository.findById(batchId) >> Optional.of(batch)

    when:
    underTest.markBatchAsDelivered(batchId)

    then:
    1 * batchRepository.updateStatusToDelivered(batchId)
  }

  def 'should not update batch status as DELIVERED when batch status is other than COMPLETED, ERROR or DELIVERED'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def batch = RegistrationFixtures.batch(BatchStatus.STORED)
    and:
    batchRepository.findById(batchId) >> Optional.of(batch)

    when:
    underTest.markBatchAsDelivered(batchId)

    then:
    0 * batchRepository.updateStatusToDelivered(batchId)
    thrown(IllegalStateException.class)
  }

  def 'should not update batch status as DELIVERED when batch not found by batch id'() {
    given:
    def batchId = UUID.randomUUID().toString()
    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.markBatchAsDelivered(batchId)

    then:
    0 * batchRepository.updateStatusToDelivered(batchId)
    thrown(NoSuchElementException.class)
  }
}

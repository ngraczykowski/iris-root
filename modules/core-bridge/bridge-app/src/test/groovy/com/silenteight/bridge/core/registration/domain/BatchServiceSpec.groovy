package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.model.Analysis
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchCompleted
import com.silenteight.bridge.core.registration.domain.model.DefaultModel
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher

import spock.lang.Specification
import spock.lang.Subject

class BatchServiceSpec extends Specification {

  def eventPublisher = Mock(EventPublisher)
  def analysisService = Mock(AnalysisService)
  def batchRepository = Mock(BatchRepository)
  def modelService = Mock(DefaultModelService)
  def batchStatisticsService = Mock(BatchStatisticsService)

  @Subject
  def underTest = new BatchService(
      eventPublisher, analysisService, batchRepository, modelService, batchStatisticsService)

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
  }

  def 'should return batch if already exists'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_BATCH_COMMAND

    and:
    batchRepository.findById(batchId) >> Optional.of(RegistrationFixtures.BATCH)

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    0 * modelService.getForSolving()
    0 * analysisService.create(_ as DefaultModel)
    0 * batchRepository.create(_ as Batch)
  }

  def 'should call updateStatusAndErrorDescription method with status error and error description when batch exists'() {
    given:
    def batchId = Fixtures.BATCH_ID

    and:
    batchRepository.findById(batchId) >> Optional.of(RegistrationFixtures.BATCH)

    when:
    underTest.notifyBatchError(RegistrationFixtures.NOTIFY_BATCH_ERROR_COMMAND)

    then:
    1 * batchStatisticsService.createBatchErrorStatistics() >> RegistrationFixtures.BATCH_STATISTICS
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
    1 * batchStatisticsService.createBatchErrorStatistics() >> RegistrationFixtures.BATCH_STATISTICS
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
        alertNames,
        RegistrationFixtures.BATCH_STATISTICS
    )

    and:
    batchRepository.findById(Fixtures.BATCH_ID) >> Optional.of(RegistrationFixtures.BATCH)

    when:
    underTest.completeBatch(command)

    then:
    1 * batchRepository.updateStatusToCompleted(command.id())
    1 * batchStatisticsService
        .createBatchCompletedStatistics(Fixtures.BATCH_ID, RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH_STATISTICS
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
    0 * batchStatisticsService.createBatchCompletedStatistics(_ as String, _ as String)
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

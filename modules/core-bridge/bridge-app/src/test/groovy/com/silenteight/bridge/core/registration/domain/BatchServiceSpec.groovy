package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.domain.port.outgoing.*

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.BatchedColumnProcessor
import spock.lang.Specification
import spock.lang.Subject

class BatchServiceSpec extends Specification {

  def eventPublisher = Mock(EventPublisher)
  def analysisService = Mock(AnalysisService)
  def batchRepository = Mock(BatchRepository)
  def modelService = Mock(DefaultModelService)

  @Subject
  def underTest = new BatchService(eventPublisher, analysisService, batchRepository, modelService)

  def "Should register batch"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = "analysis_name"
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25, "batchMetadata")

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    1 * modelService.getForSolving() >> DefaultModel.builder().build()
    1 * analysisService.create(_ as DefaultModel) >> new Analysis(analysisName)
    1 * batchRepository.create(_ as Batch) >> Batch.newOne(batchId, analysisName, 123, "batchMetadata")
  }

  def "Should return batch if already exists"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = "analysis_name"
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25, "batchMetadata")

    and:
    batchRepository.findById(batchId) >> Optional.of(Batch.newOne(batchId, analysisName, 123, "batchMetadata"))

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    0 * modelService.getForSolving()
    0 * analysisService.create(_ as DefaultModel)
    0 * batchRepository.create(_ as Batch)
  }

  def "Should call updateStatusAndErrorDescription method with status error and error description when batch exists"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def batch = Batch.newOne(batchId, "SomeAnalysisName", 25L, "batchMetadata")
    def errorDescription = "error occurred"
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(batchId, errorDescription, "batchMetadata")
    def batchError = new BatchError(batchId, errorDescription, "batchMetadata")

    and:
    batchRepository.findById(batchId) >> Optional.of(batch)

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR, errorDescription)
    1 * eventPublisher.publish(batchError)
    0 * batchRepository.create(_ as Batch)
  }

  def "Should create new batch as error when batch does not exist"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def errorDescription = "error occurred"
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(batchId, errorDescription, "batchMetadata")
    def batchError = new BatchError(batchId, errorDescription, "batchMetadata")

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchRepository.create(_ as Batch) >> Batch.error(batchId, errorDescription, "batchMetadata")
    1 * eventPublisher.publish(batchError)
    0 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR)
  }
}

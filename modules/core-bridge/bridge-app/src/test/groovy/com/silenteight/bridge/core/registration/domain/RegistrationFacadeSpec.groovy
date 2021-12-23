package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.*

import spock.lang.Specification
import spock.lang.Subject

class RegistrationFacadeSpec extends Specification {

  def analysisService = Mock(AnalysisService)
  def batchRepository = Mock(BatchRepository)
  def modelService = Mock(DefaultModelService)

  @Subject
  def underTest = new RegistrationFacade(analysisService, batchRepository, modelService)

  def "Should register batch"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = "analysis_name"
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25)

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    1 * modelService.getForSolving() >> DefaultModel.builder().build()
    1 * analysisService.create(_ as DefaultModel) >> new Analysis(analysisName)
    1 * batchRepository.create(_ as Batch) >> Batch.newOne(batchId, analysisName, 123)
  }

  def "Should return batch if already exists"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = "analysis_name"
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25)

    and:
    batchRepository.findById(batchId) >> Optional.of(Batch.newOne(batchId, analysisName, 123))

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
    def batch = Batch.newOne(batchId, "SomeAnalysisName", 25L)
    def errorDescription = "error occurred"

    and:
    batchRepository.findById(batchId) >> Optional.of(batch)

    when:
    underTest.notifyBatchError(new NotifyBatchErrorCommand(batchId, errorDescription))

    then:
    1 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR, errorDescription)
    0 * batchRepository.create(_ as Batch)
  }

  def "Should create new batch as error when batch does not exist"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def errorDescription = "error occurred"

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.notifyBatchError(new NotifyBatchErrorCommand(batchId, errorDescription))

    then:
    1 * batchRepository.create(_ as Batch) >> Batch.error(batchId, errorDescription)
    0 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR)
  }
}

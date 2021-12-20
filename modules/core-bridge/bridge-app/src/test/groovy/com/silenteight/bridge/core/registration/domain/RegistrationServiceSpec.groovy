package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.port.outgoing.*

import spock.lang.Specification
import spock.lang.Subject

class RegistrationServiceSpec extends Specification {

  def modelService = Mock(DefaultModelService)
  def analysisService = Mock(AnalysisService)
  def batchRepository = Mock(BatchRepository)

  @Subject
  def underTest = new RegistrationService(modelService, analysisService, batchRepository)

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
}

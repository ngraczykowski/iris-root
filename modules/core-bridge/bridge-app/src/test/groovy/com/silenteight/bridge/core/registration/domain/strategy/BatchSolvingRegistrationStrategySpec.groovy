package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.RegistrationFixtures
import com.silenteight.bridge.core.registration.domain.model.Analysis
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.DefaultModel
import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService
import com.silenteight.bridge.core.registration.domain.port.outgoing.VerifyBatchTimeoutPublisher

import spock.lang.Specification
import spock.lang.Subject

class BatchSolvingRegistrationStrategySpec extends Specification {

  def analysisService = Mock(AnalysisService)
  def modelService = Mock(DefaultModelService)
  def batchRepository = Mock(BatchRepository)
  def verifyBatchTimeoutPublisher = Mock(VerifyBatchTimeoutPublisher)

  @Subject
  def underTest = new BatchSolvingRegistrationStrategy(
      analysisService, modelService, batchRepository, verifyBatchTimeoutPublisher)

  def 'should register solving batch'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_BATCH_COMMAND

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

}

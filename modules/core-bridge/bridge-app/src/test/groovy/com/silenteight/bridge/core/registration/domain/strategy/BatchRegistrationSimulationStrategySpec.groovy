package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.RegistrationFixtures
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository

import spock.lang.Specification
import spock.lang.Subject

class BatchRegistrationSimulationStrategySpec extends Specification {

  def batchRepository = Mock(BatchRepository)

  @Subject
  def underTest = new BatchRegistrationSimulationStrategy(batchRepository)

  def 'should register simulation batch'() {

    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_SIMULATION_BATCH_COMMAND

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId
    batchIdDto.isSimulation()

    and:
    1 * batchRepository.create(_ as Batch) >> RegistrationFixtures.SIMULATION_BATCH
  }
}

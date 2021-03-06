package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.RegistrationFixtures
import com.silenteight.bridge.core.registration.domain.model.SimulationBatchCompleted
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository

import spock.lang.Specification
import spock.lang.Subject

class BatchCompletionSimulationStrategySpec extends Specification {

  def eventPublisher = Mock(BatchEventPublisher)
  def batchRepository = Mock(BatchRepository)

  @Subject
  def underTest = new BatchCompletionSimulationStrategy(eventPublisher, batchRepository)

  def "should complete batch"() {
    given:
    def batch = RegistrationFixtures.SIMULATION_BATCH
    def batchCompleted = new SimulationBatchCompleted(
        Fixtures.BATCH_ID,
        RegistrationFixtures.ANALYSIS_NAME,
        RegistrationFixtures.METADATA
    )

    when:
    underTest.completeBatch(batch)

    then:
    1 * batchRepository.updateStatusToCompleted(batch.id())
    1 * eventPublisher.publish(batchCompleted)
  }
}

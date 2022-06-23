package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.RegistrationFixtures
import com.silenteight.bridge.core.registration.domain.model.SolvingBatchCompleted
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository

import spock.lang.Specification
import spock.lang.Subject

class BatchCompletionSolvingStrategySpec extends Specification {

  def eventPublisher = Mock(BatchEventPublisher)
  def batchRepository = Mock(BatchRepository)

  @Subject
  def underTest = new BatchCompletionSolvingStrategy(eventPublisher, batchRepository)

  def "should #desc publish BatchCompleted message"() {
    given:
    def batch = RegistrationFixtures.BATCH
    def batchCompleted = new SolvingBatchCompleted(
        Fixtures.BATCH_ID,
        RegistrationFixtures.ANALYSIS_NAME,
        RegistrationFixtures.METADATA,
        RegistrationFixtures.BATCH_PRIORITY
    )

    when:
    underTest.completeBatch(batch)

    then:
    1 * batchRepository.updateStatusToCompleted(batch.id()) >> isBatchStatusUpdated
    if (isBatchStatusUpdated) {
      1 * eventPublisher.publish(batchCompleted)
    }
    0 * _

    where:
    desc  | isBatchStatusUpdated
    ''    | true
    'not' | false
  }
}

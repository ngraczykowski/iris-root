package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.DataRetentionFixtures
import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand
import com.silenteight.bridge.core.registration.domain.command.StartDataRetentionCommand
import com.silenteight.bridge.core.registration.domain.model.DataRetentionType
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.strategy.DataRetentionStrategy
import com.silenteight.bridge.core.registration.domain.strategy.DataRetentionStrategyFactory

import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.time.Instant

class DataRetentionServiceSpec extends Specification {

  def alertRepository = Mock(AlertRepository)
  def strategyFactory = Mock(DataRetentionStrategyFactory)

  @Subject
  def underTest = new DataRetentionService(alertRepository, strategyFactory)

  def 'should get alerts and call strategy'() {
    given:
    def command = StartDataRetentionCommand.builder()
        .type(DataRetentionType.PERSONAL_INFO_EXPIRED)
        .duration(Duration.ofDays(1))
        .chunkSize(10)
        .build()
    1 * alertRepository.findAlertsApplicableForDataRetention(_) >>
        DataRetentionFixtures.ALERTS_TO_RETENTION

    def strategy = Mock(DataRetentionStrategy)
    1 * strategyFactory.getStrategy(command.type()) >> strategy

    when:
    underTest.start(command)

    then:
    1 * strategy.run(
        {DataRetentionStrategyCommand strategyCommand ->
          with(strategyCommand) {
            type() == command.type()
            expirationDate() < Instant.now() - command.duration()
            it.alerts() == DataRetentionFixtures.ALERTS_TO_RETENTION
            chunkSize() == command.chunkSize()
          }
        })
  }
}

package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.registration.DataRetentionFixtures
import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand
import com.silenteight.bridge.core.registration.domain.model.DataRetentionType
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobAlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobRepository

import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class DataRetentionDryRunStrategySpec extends Specification {

  def jobRepository = Mock(DataRetentionJobRepository)
  def jobAlertRepository = Mock(DataRetentionJobAlertRepository)

  @Subject
  def underTest = new DataRetentionDryRunStrategy(jobRepository, jobAlertRepository)

  def 'should save alert names'() {
    given:
    def command = DataRetentionStrategyCommand.builder()
        .type(DataRetentionType.ALERTS_EXPIRED_DRY_RUN)
        .expirationDate(Instant.now())
        .alerts(DataRetentionFixtures.ALERTS_TO_RETENTION)
        .build()
    def alertNames = DataRetentionFixtures.ALERTS_TO_RETENTION.collect {it.name()}
    1 * jobRepository.save(_, _) >> 1l

    when:
    underTest.run(command)

    then:
    1 * jobAlertRepository.saveAll(1l, alertNames)
  }

  def 'should not save alert names when they are empty'() {
    given:
    def command = DataRetentionStrategyCommand.builder()
        .type(DataRetentionType.ALERTS_EXPIRED_DRY_RUN)
        .expirationDate(Instant.now())
        .alerts([])
        .build()
    1 * jobRepository.save(_, _) >> 1l

    when:
    underTest.run(command)

    then:
    0 * jobAlertRepository._
  }
}

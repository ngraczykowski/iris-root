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

  def 'should create job and save alert ids'() {
    given:
    def command = DataRetentionStrategyCommand.builder()
        .type(DataRetentionType.ALERTS_EXPIRED_DRY_RUN)
        .expirationDate(Instant.now())
        .alerts(DataRetentionFixtures.ALERTS_TO_RETENTION)
        .build()
    def alertPrimaryIds = DataRetentionFixtures.ALERTS_TO_RETENTION.collect {it.alertPrimaryId()}
    1 * jobRepository.save(_, _) >> 1l

    when:
    underTest.run(command)

    then:
    1 * jobAlertRepository.saveAll(1l, alertPrimaryIds)
  }

  def 'should create job and do not save alert ids when they are empty'() {
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

package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.registration.DataRetentionFixtures
import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand
import com.silenteight.bridge.core.registration.domain.model.DataRetentionAlertsExpiredEvent
import com.silenteight.bridge.core.registration.domain.model.DataRetentionPersonalInformationExpiredEvent
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobAlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionPublisher

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.Instant

import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.ALERTS_EXPIRED
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.PERSONAL_INFO_EXPIRED

class DataRetentionWetRunStrategySpec extends Specification {

  def jobRepository = Mock(DataRetentionJobRepository)
  def jobAlertRepository = Mock(DataRetentionJobAlertRepository)
  def alertRepository = Mock(AlertRepository)
  def dataRetentionPublisher = Mock(DataRetentionPublisher)

  @Subject
  def underTest = new DataRetentionWetRunStrategy(
      jobRepository, jobAlertRepository, alertRepository, dataRetentionPublisher)

  @Unroll
  def 'should publish messages in chunks and mark alerts as archived with retention type #dataRetentionType'() {
    given:
    def command = DataRetentionStrategyCommand.builder()
        .type(dataRetentionType)
        .expirationDate(Instant.now())
        .chunkSize(2)
        .alerts(DataRetentionFixtures.ALERTS_TO_RETENTION)
        .build()

    def alertNames = DataRetentionFixtures.ALERTS_TO_RETENTION.collect {it.name()}
    def alertsFirstChunk = [
        DataRetentionFixtures.ALERT_TO_RETENTION_1,
        DataRetentionFixtures.ALERT_TO_RETENTION_2
    ]
    def alertsSecondChunk = [DataRetentionFixtures.ALERT_TO_RETENTION_3]

    when:
    underTest.run(command)

    then:
    1 * jobRepository.save(command.expirationDate(), command.type()) >> 1l
    1 * jobAlertRepository.saveAll(1l, alertNames)
    1 * alertRepository.markAsArchived(alertNames)
    if (ALERTS_EXPIRED == command.type()) {
      1 * dataRetentionPublisher.publish(new DataRetentionAlertsExpiredEvent(alertsFirstChunk))
      1 * dataRetentionPublisher.publish(new DataRetentionAlertsExpiredEvent(alertsSecondChunk))
    } else {
      1 * dataRetentionPublisher
          .publish(new DataRetentionPersonalInformationExpiredEvent(alertsFirstChunk))
      1 * dataRetentionPublisher
          .publish(new DataRetentionPersonalInformationExpiredEvent(alertsSecondChunk))
    }

    where:
    dataRetentionType << [PERSONAL_INFO_EXPIRED, ALERTS_EXPIRED]
  }
}

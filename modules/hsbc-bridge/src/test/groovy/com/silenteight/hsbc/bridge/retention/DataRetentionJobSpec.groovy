package com.silenteight.hsbc.bridge.retention

import spock.lang.Specification

import java.time.Duration
import java.time.OffsetDateTime

class DataRetentionJobSpec extends Specification {

  def dataRetentionDuration = Duration.ofDays(1)
  def alertCleaner = Mock(DataCleaner)
  def matchCleaner = Mock(DataCleaner)
  def retentionSender = Mock(AlertRetentionSender)
  def type = DataRetentionType.PERSONAL_INFO_EXPIRED
  def chunkSize = 2
  def underTest = DataRetentionJob.builder()
      .alertDataCleaner(alertCleaner)
      .matchDataCleaner(matchCleaner)
      .dataRetentionDuration(dataRetentionDuration)
      .alertRetentionMessageSender(retentionSender)
      .type(type)
      .chunkSize(chunkSize)
      .build()

  def 'should process data'() {
    given:
    def expireDate = OffsetDateTime.now() - dataRetentionDuration

    when:
    underTest.process()

    then:
    1 * retentionSender.
        send({OffsetDateTime dateTime -> dateTime <=> expireDate == 1}, 2, DataRetentionType.PERSONAL_INFO_EXPIRED)
    1 * matchCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
    1 * alertCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
  }
}

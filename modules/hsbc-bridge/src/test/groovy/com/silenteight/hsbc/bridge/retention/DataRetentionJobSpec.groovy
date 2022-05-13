package com.silenteight.hsbc.bridge.retention

import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.time.OffsetDateTime

class DataRetentionJobSpec extends Specification {

  def dataRetentionDuration = Duration.ofDays(1)
  def alertCleaner = Mock(DataCleaner)
  def matchCleaner = Mock(DataCleaner)
  def retentionSender = Mock(AlertRetentionSender)
  def type = DataRetentionType.PERSONAL_INFO_EXPIRED
  def chunkSize = 2

  @Subject
  def underTest = new DataRetentionJob(alertCleaner, matchCleaner, retentionSender)

  def 'should process data'() {
    given:
    def properties = DataRetentionJobProperties.builder()
        .dataRetentionDuration(dataRetentionDuration)
        .type(type)
        .chunkSize(chunkSize)
        .build()
    def expireDate = OffsetDateTime.now() - dataRetentionDuration

    when:
    underTest.process(properties)

    then:
    1 * retentionSender.
        send(
            {OffsetDateTime dateTime -> dateTime <=> expireDate == 1}, 2,
            DataRetentionType.PERSONAL_INFO_EXPIRED)
    1 * matchCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
    1 * alertCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
  }
}

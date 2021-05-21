package com.silenteight.hsbc.bridge.retention

import spock.lang.Specification

import java.time.Duration
import java.time.OffsetDateTime

import static java.time.OffsetDateTime.now

class DataCleanerJobSpec extends Specification {

  def dataRetentionDuration = Duration.ofDays(1)
  def alertCleaner = Mock(DataCleaner)
  def matchCleaner = Mock(DataCleaner)
  def underTest = DataCleanerJob.builder()
      .alertDataCleaner(alertCleaner)
      .matchDataCleaner(matchCleaner)
      .dataRetentionDuration(dataRetentionDuration)
      .build()


  def 'should clean data'() {
    given:
    def expireDate = now() - dataRetentionDuration

    when:
    underTest.clean()

    then:
    1 * matchCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
    1 * alertCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
  }
}

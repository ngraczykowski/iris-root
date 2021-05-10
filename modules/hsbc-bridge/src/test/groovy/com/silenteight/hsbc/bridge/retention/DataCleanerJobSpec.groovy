package com.silenteight.hsbc.bridge.retention

import spock.lang.Specification

import java.time.Duration
import java.time.OffsetDateTime

import static java.time.OffsetDateTime.now

class DataCleanerJobSpec extends Specification {

  def dataRetentionDuration = Duration.ofDays(1)
  def bulkCleaner = Mock(DataCleaner)
  def matchCleaner = Mock(DataCleaner)
  def underTest = DataCleanerJob.builder()
      .bulkDataCleaner(bulkCleaner)
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
    1 * bulkCleaner.clean({OffsetDateTime dateTime -> dateTime <=> expireDate == 1})
  }
}

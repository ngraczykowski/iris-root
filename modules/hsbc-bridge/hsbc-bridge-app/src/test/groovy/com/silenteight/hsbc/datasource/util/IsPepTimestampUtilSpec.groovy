package com.silenteight.hsbc.datasource.util

import com.silenteight.hsbc.datasource.util.IsPepTimestampUtil.DateTimeParsingException

import spock.lang.Specification

class IsPepTimestampUtilSpec extends Specification {

  def "Should return seconds from date that matches to current format [dd-MMM-yyyy]"() {
    given:
    def lastUpdatedDate = '08-Jul-2021'

    when:
    def timestamp = IsPepTimestampUtil.toUnixTimestamp(lastUpdatedDate)

    then:
    timestamp == 1625702400
  }

  def "Should throw DateParsingException when date not match to current format [dd-MMM-yyyy]"() {
    given:
    def lastUpdatedDate = '08-Jul'

    when:
    IsPepTimestampUtil.toUnixTimestamp(lastUpdatedDate)

    then:
    thrown DateTimeParsingException
  }
}

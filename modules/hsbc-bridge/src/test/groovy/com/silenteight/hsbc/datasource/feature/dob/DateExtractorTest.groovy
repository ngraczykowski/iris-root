package com.silenteight.hsbc.datasource.feature.dob

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DateExtractorTest extends Specification {

  @Shared
  def dateExtractor = new DateExtractor()

  @Unroll
  def "extracts date correctly"() {
    when:
    def actual = dateExtractor.apply(date)

    then:
    actual == expected

    where:
    date                     || expected
    "01-Jul-1964 00:00:00.0" || "01-Jul-1964"
    "11-Apr-1949 0000"       || "11-Apr-1949"
    "11-Apr-1949 00"         || "11-Apr-1949"
  }
}

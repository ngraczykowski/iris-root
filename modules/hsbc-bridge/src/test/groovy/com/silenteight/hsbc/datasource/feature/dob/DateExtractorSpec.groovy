package com.silenteight.hsbc.datasource.feature.dob

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DateExtractorSpec extends Specification {

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
    "2020-01-01 00:00.0"     || "2020-01-01"
    "2020-00"                || "2020"
    "2020-00-00"             || "2020"
    "2020-02-00"             || "2020-02"
    "20200000"               || "2020"
    "2020.0"                 || "2020"
    "1991"                   || "1991"
    "01-Jul-1964 00:00:00.0" || "01-Jul-1964"
    "11-Apr-1949 0000"       || "11-Apr-1949"
    "11-Apr-1949 00"         || "11-Apr-1949"
  }
}

package com.silenteight.hsbc.datasource.feature.date

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DateDeviationsFilterSpec extends Specification {

  @Shared
  def underTest = new DateDeviationsFilter()

  @Unroll
  def "filters bad dates"() {
    when:
    var result = underTest.test(badDate)

    then:
    !result

    where:
    badDate << [
        "0",
        "00:00.0",
        "00:00:00.0",
        "9999-01-01",
        "11111111",
        "11971031",
        "1901-01-01",
        "9999abc",
        "9999-12-31",
    ]
  }

  def "passes good dates"() {
    when:
    var result = underTest.test(goodDate)

    then:
    result

    where:
    goodDate << [
        "2000",
        "1990",
        "2000-01",
        "2000-01-02",
        "2020-01-01 00:00.0",
        "01-10-2023",
        "11-11-11",
        "1994-12-10",
    ]
  }
}

package com.silenteight.hsbc.datasource.feature.dob

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DobDateFilterSpec extends Specification {

  @Shared
  def underTest = new DobDateFilter()

  @Unroll
  def "filters bad dates"() {
    when:
    var result = underTest.test(badDate)

    then:
    !result

    where:
    badDate << [
        "not-a-date",
        "Definetely // not / a /date",
    ]
  }

  @Unroll
  def "passes correct dates"() {
    when:
    var result = underTest.test(goodDate)

    then:
    result

    where:
    goodDate << [
        "1994",
        "1994-12-12",
        "2012/07/07",
        "somehow 123 this is a date"
    ]
  }
}

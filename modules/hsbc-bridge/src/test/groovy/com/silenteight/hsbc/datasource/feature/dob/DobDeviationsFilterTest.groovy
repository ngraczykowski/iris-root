package com.silenteight.hsbc.datasource.feature.dob

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DobDeviationsFilterTest extends Specification {

  @Shared
  def underTest = new DobDeviationsFilter()

  @Unroll
  def "filters bad dates"() {
    when:
    var result = underTest.test(badDate)

    then:
    !result

    where:
    badDate << [
        "0",
        "9999abc",
        "11111111",
        "11971031",
        "9999-12-31",
        "1901-01-01",
    ]
  }

  def "passes good dates"() {
    when:
    var result = underTest.test(goodDate)

    then:
    result

    where:
    goodDate << [
        "01-10-2023",
        "11-11-11",
        "1994-12-10",
    ]
  }
}

package com.silenteight.hsbc.datasource.feature.dob

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual

import spock.lang.Specification

class ApDateExtractorTest extends Specification {

  def "extracts date correctly"() {
    given:
    def customerIndividual = [
        getBirthDate  : {"1992 8 23 00:00:00.0"},
        getDateOfBirth: {"11111111"},
        getYearOfBirth: {"1994"}
    ] as CustomerIndividual

    when:
    def actual = new ApDateExtractor(customerIndividual).extract()

    then:
    actual.collect() == ["1992 8 23"]
  }
}

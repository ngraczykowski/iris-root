package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual

import spock.lang.Specification

class ApDateExtractorSpec extends Specification {

  def "extracts date correctly"() {
    given:
    def given =
        [
            [
                getBirthDate  : {"1992 8 23 00:00:00.0"},
                getDateOfBirth: {"11111111"},
                getYearOfBirth: {"1994"}
            ] as CustomerIndividual,
            [
                getBirthDate  : {"01/01/1900"},
                getDateOfBirth: {"11111111"},
                getYearOfBirth: {null}
            ] as CustomerIndividual,
            [
                getBirthDate  : {"01/01/1905"},
                getDateOfBirth: {"03-Jun-1986"},
                getYearOfBirth: {"1905"}
            ] as CustomerIndividual
        ]

    when:
    def actual = new ApDateExtractor(given).extract()

    then:
    actual.collect() == ["1992 8 23", "1994", "01/01/1900", "03-Jun-1986", "01/01/1905"]
  }
}

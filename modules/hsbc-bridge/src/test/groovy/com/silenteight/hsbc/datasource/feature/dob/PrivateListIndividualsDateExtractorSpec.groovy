package com.silenteight.hsbc.datasource.feature.dob

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual

import spock.lang.Specification

class PrivateListIndividualsDateExtractorSpec extends Specification {

  def "extracts correct dates"() {
    given:
    def given = [
        [
            getDateOfBirth: {"22 12 1990"},
            getYearOfBirth: {"1994"}
        ] as PrivateListIndividual,
        [
            getDateOfBirth: {"10 10 2012"},
            getYearOfBirth: {null}
        ] as PrivateListIndividual
    ]

    when:
    def actual = new PrivateListIndividualsDateExtractor(given).extract()

    then:
    actual.collect().containsAll(["22 12 1990", "10 10 2012"])
  }
}

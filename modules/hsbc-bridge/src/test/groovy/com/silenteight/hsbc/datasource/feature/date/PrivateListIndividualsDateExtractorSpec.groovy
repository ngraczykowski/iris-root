package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual

import spock.lang.Specification

class PrivateListIndividualsDateExtractorSpec extends Specification {

  def "extracts correct dates"() {
    given:
    def given = [
        [
            getDateOfBirth: {"01 01 1900"},
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
    actual.collect() == ["01 01 1900", "10 10 2012", "1994"]
  }
}

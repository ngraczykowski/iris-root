package com.silenteight.hsbc.datasource.feature.dob

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual

import spock.lang.Specification

class WorldCheckDateExtractorTest extends Specification {

  def "extracts correct values"() {
    given:
    def given = [
        [
            getDobs       : {"22 12 1990"},
            getYearOfBirth: {"1994"}
        ] as WorldCheckIndividual,
        [
            getDobs       : {"10 10 2012"},
            getYearOfBirth: {null}
        ] as WorldCheckIndividual
    ]

    when:
    def actual = new WorldCheckDateExtractor(given).extract()

    then:
    actual.collect().containsAll(["22 12 1990", "10 10 2012", "1994"])
  }
}

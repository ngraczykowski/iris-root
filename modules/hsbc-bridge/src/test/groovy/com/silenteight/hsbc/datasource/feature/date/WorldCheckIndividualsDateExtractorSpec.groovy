package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual

import spock.lang.Specification

class WorldCheckIndividualsDateExtractorSpec extends Specification {

  def "extracts correct values"() {
    given:
    def given = [
        [
            getDobs       : {"01 01 1900"},
            getYearOfBirth: {"1994"}
        ] as WorldCheckIndividual,
        [
            getDobs       : {"1961-00-00|1963-00-00|1964-04-07"},
            getYearOfBirth: {null}
        ] as WorldCheckIndividual
    ]

    when:
    def actual = new WorldCheckIndividualsDateExtractor(given).extract()

    then:
    actual.collect() == ["01 01 1900", "1961", "1963", "1964-04-07", "1994"]
  }
}

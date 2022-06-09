/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals

import spock.lang.Specification

class NnsIndividualsDateExtractorSpec extends Specification {

  def "extracts correct values"() {
    given:
    def given = [
        [
            getDobs       : {"01 01 1900"},
            getYearOfBirth: {"1994"}
        ] as NegativeNewsScreeningIndividuals,
        [
            getDobs       : {"1961-00-00|1963-00-00|1964-04-07"},
            getYearOfBirth: {"1964"}
        ] as NegativeNewsScreeningIndividuals
    ]

    when:
    def actual = new NnsIndividualsDateExtractor(given).extract()

    then:
    actual.collect() == ["01 01 1900", "1961", "1963", "1964-04-07", "1994"]
  }
}

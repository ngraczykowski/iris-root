package com.silenteight.hsbc.datasource.feature.country

import spock.lang.Specification

import java.util.stream.Collectors

class SplitterTest extends Specification {

  def "Should split correctly"() {
    when:
    def split = splitter.split(multipleCountries).map({s -> s}).collect(Collectors.toList())

    then:
    expected == split

    where:
    splitter           | multipleCountries || expected
    Splitter.COMMA     | "PL, US, UK"      || ["PL", "US", "UK"]
    Splitter.SEMICOLON | "PL; US; UK"      || ["PL", "US", "UK"]
    Splitter.SPACE     | "PL US UK"        || ["PL", "US", "UK"]
    Splitter.SPACE     | "PL   US   UK"    || ["PL", "US", "UK"]
  }
}

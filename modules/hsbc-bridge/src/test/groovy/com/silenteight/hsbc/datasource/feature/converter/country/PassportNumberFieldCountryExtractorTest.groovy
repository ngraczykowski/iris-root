package com.silenteight.hsbc.datasource.feature.converter.country

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Optional.empty
import static java.util.Optional.of

class PassportNumberFieldCountryExtractorTest extends Specification {

  @Unroll
  def "#inputField -> #expected"() {
    given:
    def underTest = new PassportNumberFieldCountryExtractor(inputField)

    when:
    def actual = underTest.extract()

    then:
    actual == expected

    where:
    inputField             | expected
    "KJ0114578 (VIET NAM)" | of("VIET NAM")
    "KJ4514578 (IRAN)"     | of("IRAN")
    "A501245"              | empty()
  }
}

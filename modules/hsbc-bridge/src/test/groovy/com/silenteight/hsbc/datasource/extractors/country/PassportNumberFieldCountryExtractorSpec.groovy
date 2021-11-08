package com.silenteight.hsbc.datasource.extractors.country

import spock.lang.Specification
import spock.lang.Unroll

class PassportNumberFieldCountryExtractorSpec extends Specification {

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
    "KJ0114578 (VIET NAM)" | Optional.of("VIET NAM")
    "KJ4514578 (IRAN)"     | Optional.of("IRAN")
    "A501245"              | Optional.empty()
  }
}

package com.silenteight.hsbc.datasource.feature.converter.country

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Optional.empty
import static java.util.Optional.of

class NationalIdNumberFieldCountryExtractorTest extends Specification {

  @Unroll
  def "#inputField -> #expected"() {
    given:
    def underTest = new NationalIdNumberFieldCountryExtractor(inputField)

    when:
    def actual = underTest.extract()

    then:
    actual == expected

    where:
    inputField             | expected
    "BC 78845 (UNK-UNKW)"  | of("UNK UNKW")
    "ID78845 (UNK-UNKW)"   | of("UNK UNKW")
    "78845ID (UNK-UNKW)"   | of("UNK UNKW")
    "78845ID (POL-POLAND)" | of("POL POLAND")
    "78845ID"              | empty()
  }
}

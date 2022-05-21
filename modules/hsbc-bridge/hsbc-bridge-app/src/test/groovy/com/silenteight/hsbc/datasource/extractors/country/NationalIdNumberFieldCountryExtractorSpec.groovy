package com.silenteight.hsbc.datasource.extractors.country

import spock.lang.Specification
import spock.lang.Unroll

class NationalIdNumberFieldCountryExtractorSpec extends Specification {

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
    "BC 78845 (UNK-UNKW)"  | Optional.of("UNK UNKW")
    "ID78845 (UNK-UNKW)"   | Optional.of("UNK UNKW")
    "78845ID (UNK-UNKW)"   | Optional.of("UNK UNKW")
    "78845ID (POL-POLAND)" | Optional.of("POL POLAND")
    "78845ID"              | Optional.empty()
  }
}

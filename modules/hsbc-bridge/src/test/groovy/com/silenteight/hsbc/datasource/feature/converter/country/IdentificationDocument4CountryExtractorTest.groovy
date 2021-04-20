package com.silenteight.hsbc.datasource.feature.converter.country

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Optional.empty
import static java.util.Optional.of

class IdentificationDocument4CountryExtractorTest extends Specification {

  @Unroll
  def "#inputField -> #expected"() {
    given:
    def underTest = new IdentificationDocumentLine4CountryExtractor(inputField)

    when:
    def actual = underTest.extract()

    then:
    actual == expected

    where:
    inputField                             | expected
    "\"NID\",\"Y999999\",\"HK\",\"\",\"\"" | of("HK")
    ""                                     | empty()
  }
}

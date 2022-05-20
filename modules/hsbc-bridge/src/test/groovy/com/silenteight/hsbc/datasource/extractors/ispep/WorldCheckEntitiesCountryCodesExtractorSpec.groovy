package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

class WorldCheckEntitiesCountryCodesExtractorSpec extends Specification {

  @Unroll
  def 'should split fields by space and return list of country codes'() {
    given:
    def worldCheckEntities = [
        Stub(WorldCheckEntity) {
          getCountryCodesAll() >> countryCodes1
        },
        Stub(WorldCheckEntity) {
          getCountryCodesAll() >> countryCodes2
        }
    ]

    when:
    def underTest = new WorldCheckEntitiesCountryCodesExtractor(worldCheckEntities)
    def actual = underTest.extract()

    then:
    actual.collect(Collectors.toList()) == countriesExpected

    where:
    countryCodes1      | countryCodes2 | countriesExpected
    "BY DE RU"         | "PL PH"       | ["BY", "DE", "RU", "PL", "PH"]
    ""                 | "PL PH"       | ["PL", "PH"]
    "BY DE RU"         | ""            | ["BY", "DE", "RU"]
    "  BY   DE   RU  " | "  PL   PH  " | ["BY", "DE", "RU", "PL", "PH"]
  }
}

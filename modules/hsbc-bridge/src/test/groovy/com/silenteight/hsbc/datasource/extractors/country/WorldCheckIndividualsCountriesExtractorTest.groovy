package com.silenteight.hsbc.datasource.extractors.country

import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class WorldCheckIndividualsCountriesExtractorTest extends Specification {

  def "returns correct values"() {
    given:
    def worldCheckIndividual = Stub(WorldCheckIndividuals) {
      getCountryOfBirth() >> Fixtures.COUNTRY_OF_BIRTH
      getNationalities() >> Fixtures.NATIONALITIES
      getPassportCountry() >> Fixtures.PASSPORT_COUNTRY
      getNativeAliasLanguageCountry() >> Fixtures.NATIVE_ALIAS_LANGUAGE_COUNTRY
    }

    when:
    def underTest = new WorldCheckIndividualsCountriesExtractor(worldCheckIndividual)
    def actual = underTest.extract()

    then:
    assertThat(actual)
        .containsExactlyInAnyOrder(
            Fixtures.COUNTRY_OF_BIRTH,
            Fixtures.NATIONALITIES,
            Fixtures.PASSPORT_COUNTRY,
            Fixtures.NATIVE_ALIAS_LANGUAGE_COUNTRY,
        )
  }

  class Fixtures {

    static def COUNTRY_OF_BIRTH = "countryOfBirth"
    static def NATIONALITIES = "nationalities"
    static def PASSPORT_COUNTRY = "passportCountry"
    static def NATIVE_ALIAS_LANGUAGE_COUNTRY = "nativeAliasLanguageCountry"
  }
}


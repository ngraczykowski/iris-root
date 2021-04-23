package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.datasource.MatchRawDataFixtures
import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryConfigurer
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryFeature

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class NationalityCountryFeatureTest extends Specification implements MatchRawDataFixtures {

  def underTest = new NationalityCountryFeature(new NationalityCountryQueryConfigurer().create())

  def 'should retrieve nationality country feature values'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_1)

    then:
    with(result) {
      feature == Feature.NATIONALITY_COUNTRY.name

      assertThat(alertedPartyCountries).contains("HK")
      assertThat(watchlistCountries).contains("IRAN", "VIET NAM", "UNK UNKW")
    }
  }
}

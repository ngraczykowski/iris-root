package com.silenteight.hsbc.datasource.feature.country

import com.silenteight.hsbc.datasource.extractors.country.ResidencyCountryFeatureQueryConfigurer
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Shared
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class ResidencyCountryFeatureSpec extends Specification implements FullMatch {

  @Shared
  def queryConfigurer = new ResidencyCountryFeatureQueryConfigurer()

  @Shared
  def underTest = new ResidencyCountryFeature(queryConfigurer.create())

  def "should extract locations"() {
    given:
    def match = FULL_MATCH_1

    when:
    def result = underTest.retrieve(match)

    then:
    with(result) {
      alertedPartyCountries == ['Polska', 'PL', 'Iran', 'IRN', 'UK']
      assertThat(watchlistCountries)
          .containsExactly('PL', 'Polska', 'IRAN, ISLAMIC REPUBLIC OF', 'IR', 'CHABAHAR')
      feature == 'features/residencyCountry'
    }
  }

  def "should extract locations for Negative News Screening"() {
    given:
    def match = NNS_INDIVIDUAL_MATCH

    when:
    def result = underTest.retrieve(match)

    then:
    with(result) {
      alertedPartyCountries == ['Polska', 'PL', 'Iran', 'IRN', 'UK']
      assertThat(watchlistCountries)
          .containsExactly('PL', 'Polska')
      feature == 'features/residencyCountry'
    }
  }
}

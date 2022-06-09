package com.silenteight.hsbc.datasource.feature.country

import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class NationalityCountryFeatureSpec extends Specification implements FullMatch {

  def underTest = new NationalityCountryFeature(new NationalityCountryQueryConfigurer().create())

  def 'should retrieve nationality country feature values'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_1)

    then:
    with(result) {
      feature == Feature.NATIONALITY_COUNTRY.fullName

      assertThat(alertedPartyCountries)
          .containsExactlyInAnyOrder('HK', 'UNITED KINGDOM', 'GB', 'DE', 'GERMANY')
      assertThat(watchlistCountries).containsExactly(
          'UNK UNKW', 'VIET NAM', 'GB', 'IRAN', 'VNM GB IRN', 'UNITED STATES', 'US',
          'IRAN, ISLAMIC REPUBLIC OF', 'IR', 'CHABAHAR')
    }
  }

  def 'should retrieve nationality country feature values for Negative News Screening'() {
    when:
    def result = underTest.retrieve(NNS_INDIVIDUAL_MATCH)

    then:
    with(result) {
      feature == Feature.NATIONALITY_COUNTRY.fullName

      assertThat(alertedPartyCountries)
          .containsExactlyInAnyOrder('HK', 'UNITED KINGDOM', 'GB', 'DE', 'GERMANY')
      assertThat(watchlistCountries).containsExactly(
          'UNK UNKW', 'VIET NAM', 'GB', 'IRAN', 'VNM GB IRN')
    }
  }
}

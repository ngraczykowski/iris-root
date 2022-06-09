package com.silenteight.hsbc.datasource.feature.country

import com.silenteight.hsbc.datasource.extractors.country.OtherCountryQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class OtherCountryFeatureSpec extends Specification implements FullMatch {

  def underTest = new OtherCountryFeature(new OtherCountryQueryConfigurer().create())

  def 'should retrieve other country feature values when customer is individual'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_1)

    then:
    with(result) {
      feature == Feature.OTHER_COUNTRY.fullName
      alertedPartyCountries == ['BY', 'BELARUS;GERMANY', 'BY DE']
      assertThat(watchlistCountries).containsExactly(
          'BY DE RU', 'BELARUS;GERMANY;RUSSIAN FEDERATION', 'GERMANY', 'IN PUNE', 'INDIA;PUNE',
          'IRAN, ISLAMIC REPUBLIC OF', 'IR', 'CHABAHAR')
    }
  }

  def 'should retrieve other country feature values when customer is individual - Negative News Screening'() {
    when:
    def result = underTest.retrieve(NNS_INDIVIDUAL_MATCH)

    then:
    with(result) {
      feature == Feature.OTHER_COUNTRY.fullName
      alertedPartyCountries == ['BY', 'BELARUS;GERMANY', 'BY DE']
      assertThat(watchlistCountries).containsExactly(
          'BY DE RU', 'BELARUS;GERMANY;RUSSIAN FEDERATION', 'GERMANY')
    }
  }

  def 'should retrieve other country feature values when customer is entity'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_3)

    then:
    with(result) {
      feature == Feature.OTHER_COUNTRY.fullName
      alertedPartyCountries == ["RUSSIAN FEDERATION", "RU", "UA"]
      assertThat(watchlistCountries).containsExactly(
          'RU', 'RUSSIAN FEDERATION', 'UNKNOWN', 'UNITED STATES', 'US', 'IRAN, ISLAMIC REPUBLIC OF',
          'IR', 'CHABAHAR')
    }
  }

  def 'should retrieve other country feature values when customer is entity - Negative News Screening'() {
    when:
    def result = underTest.retrieve(NNS_ENTITY_MATCH)

    then:
    with(result) {
      feature == Feature.OTHER_COUNTRY.fullName
      alertedPartyCountries == ["RUSSIAN FEDERATION", "RU", "UA"]
      assertThat(watchlistCountries).containsExactly(
          'RU', 'RUSSIAN FEDERATION')
    }
  }
}

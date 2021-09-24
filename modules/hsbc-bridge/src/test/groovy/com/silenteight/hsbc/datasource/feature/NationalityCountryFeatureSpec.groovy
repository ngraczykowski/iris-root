package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryConfigurer
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryFeature
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

      assertThat(alertedPartyCountries).contains("HK")
      assertThat(watchlistCountries).
          contains(
              "IRAN", "VIET NAM", "UNK UNKW", "UNITED STATES", "US", "IRAN, ISLAMIC REPUBLIC OF",
              "IR", "CHABAHAR", "VNM GB IRN")
    }
  }
}

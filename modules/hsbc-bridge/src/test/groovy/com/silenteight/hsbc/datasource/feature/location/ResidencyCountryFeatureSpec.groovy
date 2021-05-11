package com.silenteight.hsbc.datasource.feature.location

import com.silenteight.hsbc.datasource.extractors.country.ResidencyCountryFeatureQueryConfigurer
import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeature
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Shared
import spock.lang.Specification

class ResidencyCountryFeatureSpec extends Specification implements FullMatch {

  @Shared
  def queryConfigurer = new ResidencyCountryFeatureQueryConfigurer()

  @Shared
  def underTest = new ResidencyCountryFeature(queryConfigurer.getFactory())

  def "should extract locations"() {
    given:
    def match = FULL_MATCH_1

    when:
    def actual = underTest.retrieve(match)

    then:
    actual.with {
      alertedPartyCountries.containsAll(["PL", "Polska", "IRN", "Iran", "UK"])
      watchlistCountries.containsAll(["PL", "Polska"])
    }
  }
}

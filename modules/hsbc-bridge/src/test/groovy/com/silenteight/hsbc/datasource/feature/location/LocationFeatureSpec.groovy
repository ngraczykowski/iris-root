package com.silenteight.hsbc.datasource.feature.location

import com.silenteight.hsbc.datasource.extractors.country.LocationFeatureQueryConfigurer
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Shared
import spock.lang.Specification

class LocationFeatureSpec extends Specification implements FullMatch {

  @Shared
  def queryConfigurer = new LocationFeatureQueryConfigurer()

  @Shared
  def underTest = new LocationFeature(queryConfigurer.getFactory())

  def "extractsCorrectCountries"() {
    given:
    def match = FULL_MATCH_1

    when:
    def actual = underTest.retrieve(match)

    then:
    actual.with {
      alertedPartyLocations.containsAll(["PL", "Polska", "IRN", "Iran", "UK"])
      watchlistLocations.containsAll(["PL", "Polska"])
    }
  }
}

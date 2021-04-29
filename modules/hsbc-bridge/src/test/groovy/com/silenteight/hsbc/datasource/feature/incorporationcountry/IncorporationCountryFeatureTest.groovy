package com.silenteight.hsbc.datasource.feature.incorporationcountry

import com.silenteight.hsbc.bridge.domain.CustomerEntities
import com.silenteight.hsbc.bridge.domain.EntityComposite
import com.silenteight.hsbc.bridge.domain.WorldCheckEntities
import com.silenteight.hsbc.bridge.match.MatchRawData

import spock.lang.Shared
import spock.lang.Specification

class IncorporationCountryFeatureTest extends Specification {

  @Shared
  def underTest = new IncorporationCountryFeature()

  def "extractCorrectly"() {
    given:
    def match = new MatchRawData(
        entityComposite: new EntityComposite(
            customerEntities: new CustomerEntities(
                countriesOfIncorporation: "Poland",
                edqIncorporationCountries: "Polska",
                edqIncorporationCountriesCodes: "PL"
            ),
            worldCheckEntities: [
                new WorldCheckEntities(
                    countriesAll: "United Kingdom",
                    countryCodesAll: "UK"
                ),
                new WorldCheckEntities(
                    countriesAll: "Germany",
                    countryCodesAll: "DE"
                )
            ]
        )
    )

    when:
    def actual = underTest.retrieve(match)

    then:
    actual.with {
      alertedPartyCountries.containsAll(["Poland", "Polska", "PL"])
      watchlistCountries.containsAll(["UK", "DE", "United Kingdom", "Germany"])
    }
  }
}

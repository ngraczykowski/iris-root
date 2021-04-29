package com.silenteight.hsbc.datasource.feature.registrationcountry

import com.silenteight.hsbc.bridge.domain.CustomerEntities
import com.silenteight.hsbc.bridge.domain.EntityComposite
import com.silenteight.hsbc.bridge.domain.WorldCheckEntities
import com.silenteight.hsbc.bridge.match.MatchRawData

import spock.lang.Shared
import spock.lang.Specification

class RegistrationCountryFeatureTest extends Specification {

  @Shared
  def underTest = new RegistrationCountryFeature()

  def "extractCorrectly"() {
    given:
    def match = new MatchRawData(
        entityComposite: new EntityComposite(
            customerEntities: new CustomerEntities(
                registrationCountry: "Poland",
                countriesOfRegistrationOriginal: "Polska",
                edqRegiistrationCountriesCodes: "PL"
            ),
            worldCheckEntities: [
                new WorldCheckEntities(
                    registrationCountry: "UK"
                ),
                new WorldCheckEntities(
                    registrationCountry: "DE"
                )
            ]
        )
    )

    when:
    def actual = underTest.retrieve(match)

    then:
    actual.with {
      alertedPartyCountries.containsAll(["Poland", "Polska", "PL"])
      watchlistCountries.containsAll(["UK", "DE"])
    }
  }
}

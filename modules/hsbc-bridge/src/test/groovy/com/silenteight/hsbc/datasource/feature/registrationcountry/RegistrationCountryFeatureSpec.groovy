package com.silenteight.hsbc.datasource.feature.registrationcountry

import com.silenteight.hsbc.bridge.json.internal.model.CustomerEntity
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity

import spock.lang.Shared
import spock.lang.Specification

class RegistrationCountryFeatureSpec extends Specification {

  @Shared
  def underTest = new RegistrationCountryFeature()

  def "extractCorrectly"() {
    given:
    def match = [
        getCustomerEntity    : {
          [
              registrationCountry            : "Poland",
              countriesOfRegistrationOriginal: "Polska",
              edqRegistrationCountriesCodes  : "PL"
          ] as CustomerEntity
        },
        getWorldCheckEntities: {
          [
              [
                  getRegistrationCountry: {"UK"}
              ] as WorldCheckEntity,
              [
                  getRegistrationCountry: {"DE"}
              ] as WorldCheckEntity
          ]
        }
    ] as MatchData

    when:
    def actual = underTest.retrieve(match)

    then:
    actual.with {
      alertedPartyCountries.containsAll(["Poland", "Polska", "PL"])
      watchlistCountries.containsAll(["UK", "DE"])
    }
  }
}

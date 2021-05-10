package com.silenteight.hsbc.datasource.feature.incorporationcountry

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity

import spock.lang.Shared
import spock.lang.Specification

class IncorporationCountryFeatureSpec extends Specification {

  @Shared
  def underTest = new IncorporationCountryFeature()

  def "extractCorrectly"() {
    given:
    def match = [
        getCustomerEntity    : {
          [
              getCountriesOfIncorporation      : {"Poland"},
              getEdqIncorporationCountries     : {"Polska"},
              getEdqIncorporationCountriesCodes: {"PL"}
          ] as CustomerEntity
        },
        getWorldCheckEntities: {
          [
              [
                  getCountriesAll   : {"United Kingdom"},
                  getCountryCodesAll: {"UK"}
              ] as WorldCheckEntity,
              [
                  getCountriesAll   : {"Germany"},
                  getCountryCodesAll: {"DE"}
              ] as WorldCheckEntity
          ]
        }
    ] as MatchData

    when:
    def actual = underTest.retrieve(match)

    then:
    actual.with {
      alertedPartyCountries.containsAll(["Poland", "Polska", "PL"])
      watchlistCountries.containsAll(["UK", "DE", "United Kingdom", "Germany"])
    }
  }
}

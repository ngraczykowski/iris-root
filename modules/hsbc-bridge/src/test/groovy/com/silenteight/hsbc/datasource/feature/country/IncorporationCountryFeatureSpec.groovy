package com.silenteight.hsbc.datasource.feature.country

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.feature.country.IncorporationCountryFeature

import spock.lang.Specification

class IncorporationCountryFeatureSpec extends Specification {

  def underTest = new IncorporationCountryFeature()

  def "extractCorrectly"() {
    given:
    def customerEntity = Mock(CustomerEntity) {
      getCountriesOfIncorporation() >> 'Poland'
      getEdqIncorporationCountries() >> 'Polska'
      getEdqIncorporationCountriesCodes() >> 'PL'
    }

    def worldCheckEntity = Mock(WorldCheckEntity) {
      getCountriesAll() >> 'United Kingdom'
      getCountryCodesAll() >> 'UK'
    }

    def privateListEntity = Mock(PrivateListEntity) {
      getCountriesAll() >> 'UNITED STATES'
      getCountryCodesAll() >> 'US'
    }

    def ctrpScreeningEntity = Mock(CtrpScreening) {
      getCountryName() >> 'IRAN, ISLAMIC REPUBLIC OF'
      getCountryCode() >> 'IR'
      getCtrpValue() >> 'CHABAHAR'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getCustomerEntities() >> [customerEntity]
      getWorldCheckEntities() >> [worldCheckEntity]
      hasWorldCheckEntities() >> true
      getPrivateListEntities() >> [privateListEntity]
      hasPrivateListEntities() >> true
      getCtrpScreeningEntities() >> [ctrpScreeningEntity]
      hasCtrpScreeningEntities() >> true
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.INCORPORATION_COUNTRY.fullName
      alertedPartyCountries.size() == 3
      alertedPartyCountries == ['Poland', 'Polska', 'PL']
      watchlistCountries.size() == 7
      watchlistCountries ==
          ["UK", "United Kingdom", "US", "UNITED STATES", "IRAN, ISLAMIC REPUBLIC OF", "IR", "CHABAHAR"]
    }
  }
}

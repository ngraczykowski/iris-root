package com.silenteight.hsbc.datasource.feature.country

import com.silenteight.hsbc.bridge.json.internal.model.CustomerEntity
import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.country.RegistrationCountryFeatureQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class RegistrationCountryFeatureSpec extends Specification {

  def registrationCountryQueryConfigurer = new RegistrationCountryFeatureQueryConfigurer().create()

  def underTest = new RegistrationCountryFeature(registrationCountryQueryConfigurer)

  def "extractCorrectly"() {
    given:
    def customerEntity = Mock(CustomerEntity) {
      getRegistrationCountry() >> 'Poland'
      getCountriesOfRegistrationOriginal() >> 'Polska'
      getEdqRegistrationCountriesCodes() >> 'PL'
    }

    def worldCheckEntity = Mock(WorldCheckEntity) {
      getRegistrationCountry() >> 'UK'
    }

    def privateListEntity = Mock(PrivateListEntity) {
      getCountryCodesAll() >> 'US'
      getCountriesAll() >> 'UNITED STATES'
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
      getNnsEntities() >> []
      hasNnsEntities() >> false
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.REGISTRATION_COUNTRY.fullName
      alertedPartyCountries.size() == 3
      alertedPartyCountries == ['Poland', 'Polska', 'PL']
      watchlistCountries.size() == 6
      watchlistCountries ==
          ['UK', 'US', 'UNITED STATES', 'IRAN, ISLAMIC REPUBLIC OF', 'IR', 'CHABAHAR']
    }
  }

  def "extractCorrectly for Negative News Screening entity"() {
    given:
    def customerEntity = Mock(CustomerEntity) {
      getRegistrationCountry() >> 'Poland'
      getCountriesOfRegistrationOriginal() >> 'Polska'
      getEdqRegistrationCountriesCodes() >> 'PL'
    }

    def nnsEntity = Mock(NegativeNewsScreeningEntities) {
      getRegistrationCountry() >> 'UK'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getCustomerEntities() >> [customerEntity]
      getWorldCheckEntities() >> []
      hasWorldCheckEntities() >> false
      getPrivateListEntities() >> []
      hasPrivateListEntities() >> false
      getCtrpScreeningEntities() >> []
      hasCtrpScreeningEntities() >> false
      getNnsEntities() >> [nnsEntity]
      hasNnsEntities() >> true
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.REGISTRATION_COUNTRY.fullName
      alertedPartyCountries.size() == 3
      alertedPartyCountries == ['Poland', 'Polska', 'PL']
      watchlistCountries.size() == 1
      watchlistCountries == ['UK']
    }
  }
}

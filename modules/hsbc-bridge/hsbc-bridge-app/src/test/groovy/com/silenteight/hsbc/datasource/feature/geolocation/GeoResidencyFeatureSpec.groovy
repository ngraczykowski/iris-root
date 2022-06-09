package com.silenteight.hsbc.datasource.feature.geolocation

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.geolocation.GeoResidenciesConfigurer
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient
import com.silenteight.hsbc.datasource.extractors.name.NameQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer

import spock.lang.Specification

class GeoResidencyFeatureSpec extends Specification {

  def geoResidenciesQueryFactory = new GeoResidenciesConfigurer().create()
  def nameQueryFactory = new NameQueryConfigurer().create()

  def underTest = new GeoResidencyFeature(geoResidenciesQueryFactory, nameQueryFactory)

  def 'should retrieve geoResidencies values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getAddress() >> 'John Doe Los Angeles, US, California'
      getProfileFullAddress() >> 'LOS ANGELES US, CALIFORNIA'
      getResidenceCountries() >> 'United States'
      getAddressCountry() >> 'United States'
      getProfileFullName() >> 'John Doe'
      getFamilyNameOriginal() >> 'Doe'
      getMiddleName() >> 'John'
      getGivenName() >> 'John'
      getOriginalScriptName() >> 'John'
      getFullNameDerived() >> 'John Doe'
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getAddress() >> 'Minsk;St Petersburg,Minsk;Leningrad Region,BELARUS;RUSSIAN FEDERATION'
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getAddress() >> 'California'
      getAddressCountry() >> "United States"
    }

    def ctrpScreeningIndividuals = Mock(CtrpScreening) {
      getCountryCode() >> 'IR'
      getCtrpValue() >> 'CHABAHAR'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual, customerIndividual]
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> [privateListIndividual]
      hasPrivateListIndividuals() >> true
      getCtrpScreeningIndividuals() >> [ctrpScreeningIndividuals]
      hasCtrpScreeningIndividuals() >> true
      getNnsIndividuals() >> []
      hasNnsIndividuals() >> false
    }

    def nameServiceInfoClient = Mock(NameInformationServiceClient)

    when:
    def actual = underTest.retrieve(matchData, new CountryDiscoverer(), nameServiceInfoClient)

    then:
    with(actual) {
      feature == Feature.GEO_RESIDENCIES.fullName
      alertedPartyLocation == 'LOS ANGELES, US, CALIFORNIA UNITED STATES LOS ANGELES US, CALIFORNIA UNITED STATES'
      watchlistLocation ==
          'MINSK;ST PETERSBURG,MINSK;LENINGRAD REGION,BELARUS;RUSSIAN FEDERATION CALIFORNIA UNITED STATES CHABAHAR IR'
    }
  }

  def 'should retrieve geoResidencies values when customer is individual - Negative News Screening'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getAddress() >> 'John Doe Los Angeles, US, California'
      getProfileFullAddress() >> 'LOS ANGELES US, CALIFORNIA'
      getResidenceCountries() >> 'United States'
      getAddressCountry() >> 'United States'
      getProfileFullName() >> 'John Doe'
      getFamilyNameOriginal() >> 'Doe'
      getMiddleName() >> 'John'
      getGivenName() >> 'John'
      getOriginalScriptName() >> 'John'
      getFullNameDerived() >> 'John Doe'
    }

    def nnsIndividual = Mock(NegativeNewsScreeningIndividuals) {
      getAddress() >> 'Minsk;St Petersburg,Minsk;Leningrad Region,BELARUS;RUSSIAN FEDERATION'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual, customerIndividual]
      getWorldCheckIndividuals() >> []
      hasWorldCheckIndividuals() >> false
      getPrivateListIndividuals() >> []
      hasPrivateListIndividuals() >> false
      getCtrpScreeningIndividuals() >> []
      hasCtrpScreeningIndividuals() >> false
      getNnsIndividuals() >> [nnsIndividual]
      hasNnsIndividuals() >> true
    }

    def nameServiceInfoClient = Mock(NameInformationServiceClient)

    when:
    def actual = underTest.retrieve(matchData, new CountryDiscoverer(), nameServiceInfoClient)

    then:
    with(actual) {
      feature == Feature.GEO_RESIDENCIES.fullName
      alertedPartyLocation == 'LOS ANGELES, US, CALIFORNIA UNITED STATES LOS ANGELES US, CALIFORNIA UNITED STATES'
      watchlistLocation ==
          'MINSK;ST PETERSBURG,MINSK;LENINGRAD REGION,BELARUS;RUSSIAN FEDERATION'
    }
  }

  def 'should retrieve geoResidencies values when customer is entity'() {
    given:
    def customerEntity = Mock(CustomerEntity) {
      getAddress() >> 'SAINT PETERSBURG LENINGRAD REGION RUSSIAN FEDERATION'
      getProfileFullAddress() >> 'John Company Ltd Saint Petersburg,Leningrad Region,RUSSIAN FEDERATION'
      getEntityNameOriginal() >> 'John Company Ltd'
      getOriginalScriptName() >> 'John Company Ltd'
      getEntityName() >> 'John Company Ltd'
      getAddressCountry() >> 'Russian FEDERATION'
    }

    def ctrpScreeningEntity = Mock(CtrpScreening) {
      getCountryCode() >> 'IR'
      getCtrpValue() >> 'CHABAHAR'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getCustomerEntities() >> [customerEntity]
      getCtrpScreeningEntities() >> [ctrpScreeningEntity]
      hasCtrpScreeningEntities() >> true
    }

    def nameServiceInfoClient = Mock(NameInformationServiceClient)

    when:
    def actual = underTest.retrieve(matchData, new CountryDiscoverer(), nameServiceInfoClient)

    then:
    with(actual) {
      feature == Feature.GEO_RESIDENCIES.fullName
      alertedPartyLocation ==
          'SAINT PETERSBURG LENINGRAD REGION RUSSIAN FEDERATION SAINT PETERSBURG,LENINGRAD REGION,RUSSIAN FEDERATION'
      watchlistLocation == 'CHABAHAR IR'
    }
  }
}

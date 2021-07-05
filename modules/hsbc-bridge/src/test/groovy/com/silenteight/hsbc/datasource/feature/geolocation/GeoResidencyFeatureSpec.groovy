package com.silenteight.hsbc.datasource.feature.geolocation

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.geolocation.GeoResidenciesConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class GeoResidencyFeatureSpec extends Specification {

  def geoResidenciesQueryConfigurer = new GeoResidenciesConfigurer().create()

  def underTest = new GeoResidencyFeature(geoResidenciesQueryConfigurer)

  def 'should retrieve geoResidencies values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getAddress() >> 'Los Angeles, US, California'
      getProfileFullAddress() >> 'LOS ANGELES US, CALIFORNIA'
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getAddress() >> 'Minsk;St Petersburg,Minsk;Leningrad Region,BELARUS;RUSSIAN FEDERATION'
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getAddress() >> 'California'
    }

    def ctrpScreeningIndividuals = Mock(CtrpScreening) {
      getCountryCode() >> 'IR'
      getCtrpValue() >> 'CHABAHAR'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividual() >> customerIndividual
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> [privateListIndividual]
      hasPrivateListIndividuals() >> true
      getCtrpScreeningIndividuals() >> [ctrpScreeningIndividuals]
      hasCtrpScreeningIndividuals() >> true
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.GEO_RESIDENCIES.fullName
      alertedPartyLocation == 'LOS ANGELES, US, CALIFORNIA LOS ANGELES US, CALIFORNIA'
      watchlistLocation ==
          'MINSK;ST PETERSBURG,MINSK;LENINGRAD REGION,BELARUS;RUSSIAN FEDERATION CALIFORNIA CHABAHAR IR'
    }
  }

  def 'should retrieve geoResidencies values when customer is entity'() {
    given:
    def customerEntity = Mock(CustomerEntity) {
      getAddress() >> 'SAINT PETERSBURG LENINGRAD REGION RUSSIAN FEDERATION'
      getProfileFullAddress() >> 'Saint Petersburg,Leningrad Region,RUSSIAN FEDERATION'
    }

    def ctrpScreeningEntity = Mock(CtrpScreening) {
      getCountryCode() >> 'IR'
      getCtrpValue() >> 'CHABAHAR'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getCustomerEntity() >> customerEntity
      getCtrpScreeningEntities() >> [ctrpScreeningEntity]
      hasCtrpScreeningEntities() >> true
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.GEO_RESIDENCIES.fullName
      alertedPartyLocation ==
          'SAINT PETERSBURG LENINGRAD REGION RUSSIAN FEDERATION SAINT PETERSBURG,LENINGRAD REGION,RUSSIAN FEDERATION'
      watchlistLocation == 'CHABAHAR IR'
    }
  }
}

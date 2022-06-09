package com.silenteight.hsbc.datasource.feature.geolocation

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.geolocation.GeoPlaceOfBirthConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class GeoPlaceOfBirthFeatureSpec extends Specification {

  def geoPlaceOfBirthQueryConfigurer = new GeoPlaceOfBirthConfigurer().create()

  def underTest = new GeoPlaceOfBirthFeature(geoPlaceOfBirthQueryConfigurer)

  def 'should retrieve geoPlaceOfBirth values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getPlaceOfBirth() >> 'UNKNOWN'
      getCountryOfBirth() >> 'GERMANY'
      getStateProvinceOrCountyOfBirth() >> 'GERMANY'
      getTownOfBirth() >> 'Berlin'
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getPlaceOfBirthOriginal() >> 'Germany; Germany (GDR); Gernamy (GDR)'
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getPlaceOfBirth() >> 'UNKNOWN'
      getCountryOfBirth() >> 'UNKNOWN'
    }

    def ctrpScreeningIndividuals = Mock(CtrpScreening) {
      getCountryCode() >> 'IR'
      getCtrpValue() >> 'CHABAHAR'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> [privateListIndividual]
      hasPrivateListIndividuals() >> true
      getCtrpScreeningIndividuals() >> [ctrpScreeningIndividuals]
      hasCtrpScreeningIndividuals() >> true
      getNnsIndividuals() >> []
      hasNnsIndividuals()>> false
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.GEO_PLACE_OF_BIRTH.fullName
      alertedPartyLocation == 'GERMANY GERMANY BERLIN UNKNOWN'
      watchlistLocation == 'GERMANY GERMANY (GDR) GERNAMY (GDR) UNKNOWN CHABAHAR IR '
    }
  }

  def 'should retrieve geoPlaceOfBirth values when customer is individual - Negative News Screening'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getPlaceOfBirth() >> 'UNKNOWN'
      getCountryOfBirth() >> 'GERMANY'
      getStateProvinceOrCountyOfBirth() >> 'GERMANY'
      getTownOfBirth() >> 'Berlin'
    }

    def nnsIndividual = Mock(NegativeNewsScreeningIndividuals) {
      getOriginalPlaceOfBirth() >> 'Germany; Germany (GDR); Gernamy (GDR)'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> []
      hasWorldCheckIndividuals() >> false
      getPrivateListIndividuals() >> []
      hasPrivateListIndividuals() >> false
      getCtrpScreeningIndividuals() >> []
      hasCtrpScreeningIndividuals() >> false
      getNnsIndividuals() >> [nnsIndividual]
      hasNnsIndividuals() >> true
    }

    when:
    def actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.GEO_PLACE_OF_BIRTH.fullName
      alertedPartyLocation == 'GERMANY GERMANY BERLIN UNKNOWN'
      watchlistLocation == ' GERMANY GERMANY (GDR) GERNAMY (GDR)'
    }
  }
}

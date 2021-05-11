package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.extractors.document.DocumentQueryConfigurer
import com.silenteight.hsbc.datasource.feature.nationalityid.NationalityIdFeature

import spock.lang.Specification

class NationalityIdFeatureSpec extends Specification {

  def documentQueryConfigurer = new DocumentQueryConfigurer()

  def underTest = new NationalityIdFeature(
      documentQueryConfigurer.alertedPartyDocumentQuery(),
      documentQueryConfigurer.matchPartyDocumentQuery())

  def 'should retrieve empty lists as nationality id values when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.NATIONALITY_ID.name
      alertedPartyDocumentNumbers == []
      watchlistDocumentNumbers == []
    }
  }

  def 'should retrieve nationality id values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getIdentificationDocument1() >> '"ID","987456"'
      getIdentificationDocument2() >> '2'
      getIdentificationDocument3() >> '3'
      getIdentificationDocument4() >> '4'
      getIdentificationDocument5() >> '5'
      getIdentificationDocument6() >> '6'
      getIdentificationDocument7() >> '7'
      getIdentificationDocument8() >> '8'
      getIdentificationDocument9() >> '9'
      getIdentificationDocument10() >> '10'
    }
    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getPassportNumber() >> 'KJ0114578 (VIET NAM)'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividual() >> customerIndividual
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.NATIONALITY_ID.name
      alertedPartyDocumentNumbers == ['987456']
      watchlistDocumentNumbers == ['KJ0114578']
    }
  }

}

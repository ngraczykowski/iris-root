package com.silenteight.hsbc.datasource.feature.passportnumberdocument

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.extractors.document.PassportNumberDocumentQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class PassportNumberFeatureSpec extends Specification {

  def documentQueryConfigurer = new PassportNumberDocumentQueryConfigurer().create()

  def underTest = new PassportNumberFeature(documentQueryConfigurer)

  def 'should retrieve passport number document empty lists when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.PASSPORT_NUMBER_DOCUMENT.name
      alertedPartyDocuments == []
      watchlistDocuments == []
    }
  }

  def 'should retrieve passport number document values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getIdentificationDocument1() >> '"P","123456"'
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

    def privateListIndividual = Mock(PrivateListIndividual) {
      getPassportNumber() >> 'K45R78986'
      getEdqSuffix() >> 'ID42342'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividual() >> customerIndividual
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> [privateListIndividual]
      hasPrivateListIndividuals() >> true
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.PASSPORT_NUMBER_DOCUMENT.name
      alertedPartyDocuments.size() == 1
      alertedPartyDocuments == ['123456']
      watchlistDocuments.size() == 2
      watchlistDocuments == ['KJ0114578', 'K45R78986']
    }
  }
}

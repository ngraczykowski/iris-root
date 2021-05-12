package com.silenteight.hsbc.datasource.feature.document

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.document.DocumentQueryFacade
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class DocumentFeatureSpec extends Specification {

  def underTest = new DocumentFeature(DocumentQueryFacade::new)

  def 'should retrieve document empty lists when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.DOCUMENT.name
      alertedPartyDocuments == []
      watchlistDocuments == []
    }
  }

  def 'should retrieve document values when customer is individual'() {
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
      getIdNumbers() >> 'BC 78845 (UNK-UNKW)'
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getPassportNumber() >> 'K45R78986'
      getNationalId() >> '4568795132'
      getEdqSuffix() >> 'ID42342'
      getEdqTaxNumber() >> 'WOHZ784512R12'
      getEdqDrivingLicence() >> 'test76@hotmail.com'
    }

    def privateListEntity = Mock(PrivateListEntity) {
      getEdqTaxNumber() >> 'GOHA784512R12'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividual() >> customerIndividual
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> [privateListIndividual]
      hasPrivateListIndividuals() >> true
      getPrivateListEntities() >> [privateListEntity]
      hasPrivateListEntities() >> true
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.DOCUMENT.name
      alertedPartyDocuments.size() == 1
      alertedPartyDocuments == ['987456']
      watchlistDocuments.size() == 8
      watchlistDocuments ==
          ['KJ0114578', 'K45R78986', '4568795132', 'BC 78845', 'test76@hotmail.com', 'WOHZ784512R12', 'ID42342', 'GOHA784512R12']
    }
  }
}

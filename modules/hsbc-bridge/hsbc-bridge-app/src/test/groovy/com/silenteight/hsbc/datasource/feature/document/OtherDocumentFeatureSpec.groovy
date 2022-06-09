package com.silenteight.hsbc.datasource.feature.document

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.document.OtherDocumentQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class OtherDocumentFeatureSpec extends Specification {

  def documentQueryConfigurer = new OtherDocumentQueryConfigurer().create()

  def underTest = new OtherDocumentFeature(documentQueryConfigurer)

  def 'should retrieve other document empty lists when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.OTHER_DOCUMENT.fullName
      alertedPartyDocuments == []
      watchlistDocuments == []
    }
  }

  def 'should retrieve other document values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getIdentificationDocument1() >> '"ID","987654 nUmBeR"'
      getIdentificationDocument2() >> '3'
      getIdentificationDocument3() >> '4'
      getIdentificationDocument4() >> '4'
      getIdentificationDocument5() >> '5'
      getIdentificationDocument6() >> '6'
      getIdentificationDocument7() >> '7'
      getIdentificationDocument8() >> '8'
      getIdentificationDocument9() >> '9'
      getIdentificationDocument10() >> '10'
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getIdNumbers() >> 'BC 78845 (UNK-UNKW)'
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getEdqSuffix() >> 'SUFFIX'
      getEdqTaxNumber() >> 'WOHZ784512R12'
      getEdqDrivingLicence() >> 'test76@hotmail.com'
    }

    def privateListEntity = Mock(PrivateListEntity) {
      getEdqTaxNumber() >> 'GOHA784512R12'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
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
      feature == Feature.OTHER_DOCUMENT.fullName
      alertedPartyDocuments.size() == 1
      alertedPartyDocuments == ['987654 nUmBeR']
      watchlistDocuments.size() == 5
      watchlistDocuments ==
          ['BC 78845', 'test76@hotmail.com', 'WOHZ784512R12', 'SUFFIX', 'GOHA784512R12']
    }
  }

  def 'should retrieve other document values when customer is individual - Negative News Screening'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getIdentificationDocument1() >> '"ID","987654 nUmBeR"'
      getIdentificationDocument2() >> '3'
      getIdentificationDocument3() >> '4'
      getIdentificationDocument4() >> '4'
      getIdentificationDocument5() >> '5'
      getIdentificationDocument6() >> '6'
      getIdentificationDocument7() >> '7'
      getIdentificationDocument8() >> '8'
      getIdentificationDocument9() >> '9'
      getIdentificationDocument10() >> '10'
    }

    def nnsIndividual = Mock(NegativeNewsScreeningIndividuals) {
      getIdNumbers() >> 'BC 78845 (UNK-UNKW)'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> []
      hasWorldCheckIndividuals() >> false
      getPrivateListIndividuals() >> []
      hasPrivateListIndividuals() >> false
      getPrivateListEntities() >> []
      hasPrivateListEntities() >> false
      getNnsIndividuals() >> [nnsIndividual]
      hasNnsIndividuals() >> true
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.OTHER_DOCUMENT.fullName
      alertedPartyDocuments.size() == 1
      alertedPartyDocuments == ['987654 nUmBeR']
      watchlistDocuments.size() == 1
      watchlistDocuments == ['BC 78845']
    }
  }
}

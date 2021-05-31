package com.silenteight.hsbc.datasource.feature.nationaliddocument

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.extractors.document.NationalIdDocumentQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class NationalIdFeatureSpec extends Specification {

  def documentQueryConfigurer = new NationalIdDocumentQueryConfigurer().create()

  def underTest = new NationalIdFeature(documentQueryConfigurer)

  def 'should retrieve national id document empty lists when values when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.NATIONAL_ID_DOCUMENT.fullName
      alertedPartyDocumentNumbers == []
      watchlistDocumentNumbers == []
    }
  }

  def 'should retrieve national id document values when customer is individual'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getIdentificationDocument1() >> '"NID","987456"'
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
      getIdNumbers() >> "78845ID (UNK-UNKW)"
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getNationalId() >> '4568795132,5465498756'
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
      feature == Feature.NATIONAL_ID_DOCUMENT.fullName
      alertedPartyDocumentNumbers == ['987456']
      watchlistDocumentNumbers == ['78845ID', '4568795132', '5465498756']
    }
  }
}

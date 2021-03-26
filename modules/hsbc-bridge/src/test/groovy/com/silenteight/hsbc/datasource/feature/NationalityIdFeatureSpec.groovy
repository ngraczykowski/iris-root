package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.bridge.match.MatchRawData

import spock.lang.Specification

class NationalityIdFeatureSpec extends Specification {

  def underTest = new NationalityIdFeature()

  def 'should retrieve nationality id feature values'() {
    given:
    def matchRawData = new MatchRawData()

    when:
    def result = underTest.retrieve(matchRawData)

    then:
    with(result) {
      feature == Feature.NATIONALITY_ID.name
      alertedPartyCountry == 'AP'
      watchlistCountry == 'PL'
      alertedPartyDocumentNumbers == ['123']
      watchlistDocumentNumbers == ['321']
    }
  }
}

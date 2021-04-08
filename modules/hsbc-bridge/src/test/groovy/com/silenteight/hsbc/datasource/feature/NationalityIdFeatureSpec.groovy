package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.bridge.match.MatchRawData

import spock.lang.Specification

class NationalityIdFeatureSpec extends Specification {

  def underTest = new NationalityIdFeature()

  def 'should retrieve nationality id feature values'() {
    given:
    def matchRawData = Mock(MatchRawData)

    when:
    def result = underTest.retrieve(matchRawData)

    then:
    matchRawData.isIndividual() >> false

    with(result) {
      feature == Feature.NATIONALITY_ID.name
      alertedPartyDocumentNumbers == null
      watchlistDocumentNumbers == null
    }
  }
}

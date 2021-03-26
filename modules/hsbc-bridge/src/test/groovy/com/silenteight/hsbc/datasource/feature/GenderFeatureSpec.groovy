package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.bridge.match.MatchRawData

import spock.lang.Specification

class GenderFeatureSpec extends Specification {

  def underTest = new GenderFeature()

  def 'should retrieve gender feature values'() {
    given:
    def matchRawData = new MatchRawData()

    when:
    def result = underTest.retrieve(matchRawData)

    then:
    with(result) {
      feature == Feature.GENDER.name
      alertedPartyGenders == ['M', 'F']
      watchlistGenders == ['F']
    }
  }
}

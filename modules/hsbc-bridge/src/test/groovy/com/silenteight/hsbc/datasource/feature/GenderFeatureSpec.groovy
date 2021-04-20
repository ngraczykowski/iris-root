package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL
import com.silenteight.hsbc.bridge.match.MatchRawData
import com.silenteight.hsbc.datasource.MatchRawDataFixtures

import spock.lang.Specification

class GenderFeatureSpec extends Specification implements MatchRawDataFixtures {

  def underTest = new GenderFeature()

  def 'should retrieve gender feature values'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_1)

    then:
    with(result) {
      feature == Feature.GENDER.name
      alertedPartyGenders == ['M']
      watchlistGenders == ['M']
    }
  }

  def 'should retrieve empty gender feature values'() {
    given:
    def matchRawData = new MatchRawData(
        caseId: 1,
        caseWithAlertURL: new CasesWithAlertURL(id: 1)
    )

    when:
    def result = underTest.retrieve(matchRawData)

    then:
    with(result) {
      feature == Feature.GENDER.name
      alertedPartyGenders == []
      watchlistGenders == []
    }
  }
}

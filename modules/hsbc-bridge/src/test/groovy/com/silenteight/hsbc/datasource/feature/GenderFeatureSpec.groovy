package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.bridge.domain.*
import com.silenteight.hsbc.bridge.match.MatchRawData

import spock.lang.Specification

class GenderFeatureSpec extends Specification {

  def underTest = new GenderFeature()

  def 'should retrieve gender feature values'() {
    given:
    def matchRawData = new MatchRawData(
        caseId: 1,
        caseWithAlertURL: new CasesWithAlertURL(id: 1),
        individualComposite: new IndividualComposite(
            new CustomerIndividuals(gender: "M"),
            [new WorldCheckIndividuals(gender: "M")],
            [new PrivateListIndividuals(gender: "M")],
            []
        )
    )

    when:
    def result = underTest.retrieve(matchRawData)

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

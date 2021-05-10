package com.silenteight.hsbc.datasource.feature.gender


import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Specification

class GenderFeatureSpec extends Specification implements FullMatch {

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
    when:
    def result = underTest.retrieve(FULL_MATCH_2)

    then:
    with(result) {
      feature == Feature.GENDER.name
      alertedPartyGenders == []
      watchlistGenders == []
    }
  }
}

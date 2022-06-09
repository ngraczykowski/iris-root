package com.silenteight.hsbc.datasource.feature.gender

import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.fixtures.FullMatch
import com.silenteight.hsbc.datasource.fixtures.Match

import spock.lang.Specification

class GenderFeatureSpec extends Specification implements FullMatch, Match {

  def underTest = new GenderFeature()

  def 'should retrieve gender feature values'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_1)

    then:
    with(result) {
      feature == Feature.GENDER.fullName
      alertedPartyGenders == ['M']
      watchlistGenders == ['M']
    }
  }

  def 'should retrieve gender feature values for Negative News Screening'() {
    when:
    def result = underTest.retrieve(NNS_INDIVIDUAL_MATCH)

    then:
    with(result) {
      feature == Feature.GENDER.fullName
      alertedPartyGenders == ['M']
      watchlistGenders == ['M']
    }
  }

  def 'should retrieve empty alertedPartyGenders when genderDerivedFlag is not "N"'() {
    when:
    def result = underTest.retrieve(MATCH_WITHOUT_AP_GENDER_DERIVED_FLAG_1)

    then:
    with(result) {
      feature == Feature.GENDER.fullName
      alertedPartyGenders == []
      watchlistGenders == ['M']
    }
  }

  def 'should retrieve empty watchlistGenders when genderDerivedFlag is not "N"'() {
    when:
    def result = underTest.retrieve(MATCH_WITHOUT_WP_GENDER_DERIVED_FLAG_1)

    then:
    with(result) {
      feature == Feature.GENDER.fullName
      alertedPartyGenders == ['M']
      watchlistGenders == []
    }
  }

  def 'should retrieve empty gender feature values'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_2)

    then:
    with(result) {
      feature == Feature.GENDER.fullName
      alertedPartyGenders == []
      watchlistGenders == []
    }
  }
}

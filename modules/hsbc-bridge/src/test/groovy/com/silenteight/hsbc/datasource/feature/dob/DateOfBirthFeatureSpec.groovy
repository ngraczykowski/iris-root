package com.silenteight.hsbc.datasource.feature.dob

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class DateOfBirthFeatureSpec extends Specification {

  def underTest = new DateOfBirthFeature()

  def "extracts correctly without duplicates"() {
    given:
    def matchData = [
        getCustomerEntities      : {null},
        getCustomerIndividuals   :
            {
              [
                  [
                      getBirthDate  : {"1992 8 23 00:00:00.0"},
                      getDateOfBirth: {"11111111"},
                      getYearOfBirth: {"1994"}
                  ] as CustomerIndividual

              ]
            },
        getWorldCheckIndividuals : {
          [
              [
                  getDobs       : {"22 12 1990"},
                  getYearOfBirth: {"1994"}
              ] as WorldCheckIndividual,
              [
                  getDobs       : {"22 12 1990"},
                  getYearOfBirth: {"1994"}
              ] as WorldCheckIndividual,
              [
                  getDobs       : {"10 10 2012"},
                  getYearOfBirth: {null}
              ] as WorldCheckIndividual
          ]
        },
        getPrivateListIndividuals: {
          [
              [
                  getDateOfBirth: {"23/12/1990"},
                  getYearOfBirth: {"1995"}
              ] as PrivateListIndividual,
              [
                  getDateOfBirth: {"23/12/1990"},
                  getYearOfBirth: {"1995"}
              ] as PrivateListIndividual,
              [
                  getDateOfBirth: {"10 10 2012"},
                  getYearOfBirth: {null}
              ] as PrivateListIndividual
          ]
        }

    ] as MatchData

    when:
    var actual = underTest.retrieve(matchData)

    then:
    with(actual) {
      feature == Feature.DATE_OF_BIRTH.fullName
      alertedPartyDates.size() == 2
      alertedPartyDates == ["1992 8 23", "1994"]
      watchlistDates.size() == 5
      watchlistDates == ["22 12 1990", "10 10 2012", "1994", "23/12/1990", "1995"]
    }
  }
}

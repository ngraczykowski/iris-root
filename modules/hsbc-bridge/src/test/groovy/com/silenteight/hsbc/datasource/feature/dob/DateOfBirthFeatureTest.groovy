package com.silenteight.hsbc.datasource.feature.dob

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual

import spock.lang.Specification

class DateOfBirthFeatureTest extends Specification {

  def underTest = new DateOfBirthFeature()

  def "extracts correctly"() {
    given:
    def matchData = [
        getCustomerEntity        : {null},
        getCustomerIndividual    : {
          [
              getBirthDate  : {"1992 8 23 00:00:00.0"},
              getDateOfBirth: {"11111111"},
              getYearOfBirth: {"1994"}
          ] as CustomerIndividual
        },
        getWorldCheckIndividuals : {
          [
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
                  getDateOfBirth: {"10 10 2012"},
                  getYearOfBirth: {null}
              ] as PrivateListIndividual
          ]
        }

    ] as MatchData

    when:
    var actual = underTest.retrieve(matchData)

    then:
    actual.with {
      it.getAlertedPartyDates().containsAll(["1992 8 23", "1994"])
      it.getWatchlistDates().containsAll(["22 12 1990", "1994", "10 10 2012", "23/12/1994", "1995"])
    }
  }
}

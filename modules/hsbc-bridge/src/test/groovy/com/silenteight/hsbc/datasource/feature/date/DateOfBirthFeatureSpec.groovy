package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.dto.date.SeverityMode
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

import static com.silenteight.hsbc.datasource.dto.name.EntityType.INDIVIDUAL
import static com.silenteight.hsbc.datasource.dto.name.EntityType.ORGANIZATION

class DateOfBirthFeatureSpec extends Specification {

  def underTest = new DateOfBirthFeature()

  def "should extract DateFeatureInput from matchData and watchlistTypes for CustomerIndividuals"() {
    given:
    def matchData = [
        getCaseInformation       : {
          [
              getExtendedAttribute5: {"AML"}
          ] as CaseInformation
        },
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
                  getYearOfBirth: {"2012"}
              ] as WorldCheckIndividual
          ]
        },
        getPrivateListIndividuals: {
          [
              [
                  getDateOfBirth: {"23/12/1995"},
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
    var actual = underTest.retrieve(matchData, watchlistTypes)

    then:
    with(actual) {
      feature == Feature.DATE_OF_BIRTH.fullName
      alertedPartyDates.size() == 2
      watchlistDates.size() == 5
      watchlistDates == ["22 12 1990", "10 10 2012", "1994", "23/12/1995", "23/12/1990"]
      mode == SeverityMode.NORMAL
      alertedPartyType == INDIVIDUAL
      alertedPartyDates == ["1992 8 23", "1994"]
    }
  }

  def "should extract DateFeatureInput from matchData and watchlistTypes for CustomerEntities"() {
    given:
    def matchData = [
        getCaseInformation    : {
          [
              getExtendedAttribute5: {"US-HBUS"}
          ] as CaseInformation
        },
        getCustomerEntities   : {
          [
              [getEntityName: "test"] as CustomerEntity
          ]
        },
        getCustomerIndividuals:
            {null},
    ] as MatchData

    when:
    var actual = underTest.retrieve(matchData, watchlistTypes)

    then:
    with(actual) {
      feature == Feature.DATE_OF_BIRTH.fullName
      alertedPartyType == ORGANIZATION
      mode == SeverityMode.STRICT
    }
  }

  static def watchlistTypes =
      [
          'SAN'  : [
              'AE-MEWOLF',
              'MENA-GREY',
              'MEWOLF',
              'MX-DARK-GREY',
              'SAN',
              'US-HBUS',
          ] as List,
          'AML'  : [
              'AML',
              'CTF-P2',
              'INNIA',
              'MX-AML',
              'MX-SHCP',
          ] as List,
          'PEP'  : [
              'PEP'
          ] as List,
          'EXITS': [
              'SCION'
          ] as List,
          'SSC'  : [
              'SSC'
          ]
      ] as Map
}

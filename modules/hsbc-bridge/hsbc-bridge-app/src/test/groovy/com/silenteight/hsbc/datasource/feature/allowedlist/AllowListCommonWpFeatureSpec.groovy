package com.silenteight.hsbc.datasource.feature.allowedlist

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities
import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals
import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual

import spock.lang.Specification

class AllowListCommonWpFeatureSpec extends Specification {

  def underTest = new AllowListCommonWpFeature()

  def 'should return feature values for individual'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCtrpScreeningIndividuals() >> [
          Mock(CtrpScreening) {
            getCountryCode() >> 'countryCode'
          }
      ]
      getPrivateListIndividuals() >> [
          Mock(PrivateListIndividual) {
            getListRecordId() >> 'privateListId'
          }
      ]
      getWorldCheckIndividuals() >> [
          Mock(WorldCheckIndividual) {
            getListRecordId() >> 'worldCheckListId'
          }
      ]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_common_watchlist_party']
      characteristicsValues == ['worldCheckListId', 'privateListId', 'countryCode']
      feature == 'features/commonMp'
    }
  }

  def 'should return feature values for individual - Negative News Screening'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCtrpScreeningIndividuals() >> []
      getPrivateListIndividuals() >> []
      getWorldCheckIndividuals() >> []
      getNnsIndividuals() >> [
          Mock(NegativeNewsScreeningIndividuals) {
            getListRecordId() >> 'worldCheckListId'
          }
      ]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_common_watchlist_party']
      characteristicsValues == ['worldCheckListId']
      feature == 'features/commonMp'
    }
  }

  def 'should return feature values for entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
      getCtrpScreeningEntities() >> [
          Mock(CtrpScreening) {
            getCountryCode() >> 'countryCode'
          }
      ]
      getPrivateListEntities() >> [
          Mock(PrivateListEntity) {
            getListRecordId() >> 'privateListId'
          }
      ]
      getWorldCheckEntities() >> [
          Mock(WorldCheckEntity) {
            getListRecordId() >> 'worldCheckListId'
          }
      ]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_common_watchlist_party']
      characteristicsValues == ['worldCheckListId', 'privateListId', 'countryCode']
      feature == 'features/commonMp'
    }
  }

  def 'should return feature values for entity - Negative News Screening'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
      getCtrpScreeningEntities() >> []
      getPrivateListEntities() >> []
      getWorldCheckEntities() >> []
      getNnsEntities() >> [
          Mock(NegativeNewsScreeningEntities) {
            getListRecordId() >> 'worldCheckListId'
          }
      ]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_common_watchlist_party']
      characteristicsValues == ['worldCheckListId']
      feature == 'features/commonMp'
    }
  }
}

package com.silenteight.hsbc.datasource.feature.allowedlist

import com.silenteight.hsbc.datasource.datamodel.CaseInformation
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification

class AllowListCommonApFeatureSpec extends Specification {

  def underTest = new AllowListCommonApFeature()

  def 'should return feature values'() {
    given:
    def matchData = Mock(MatchData) {
      getCaseInformation() >> Mock(CaseInformation) {
        getParentId() >> 'parentId'
      }
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_common_alerted_party']
      characteristicsValues == ['parentId']
      feature == 'features/f_common_ap'
    }
  }
}

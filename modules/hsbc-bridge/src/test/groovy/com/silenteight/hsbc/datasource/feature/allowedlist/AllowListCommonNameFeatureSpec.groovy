package com.silenteight.hsbc.datasource.feature.allowedlist

import com.silenteight.hsbc.datasource.datamodel.*

import spock.lang.Specification

class AllowListCommonNameFeatureSpec extends Specification {

  def underTest = new AllowListCommonNameFeature()

  def 'should return feature values for individual'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividual() >> Mock(CustomerIndividual) {
        getGivenName() >> 'givenName'
        getFamilyNameOriginal() >> 'familyNameOriginal'
        getFullNameDerived() >> 'fullNameDerived'
        getMiddleName() >> 'middleName'
        getOriginalScriptName() >> 'originalScriptName'
        getProfileFullName() >> 'profileFullName'
      }
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListName == 'hsbc_common_name'
      characteristicsValues == ['givenName', 'familyNameOriginal', 'fullNameDerived',
                                'middleName', 'originalScriptName', 'profileFullName']
      feature == 'features/f_common_names'
    }
  }

  def 'should return feature values for entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
      getCustomerEntity() >> Mock(CustomerEntity) {
        getEntityName() >> 'entityName'
        getEntityNameOriginal() >> 'entityNameOriginal'
        getOriginalScriptName() >> 'originalScriptName'
      }
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListName == 'hsbc_common_name'
      characteristicsValues == ['entityName', 'entityNameOriginal', 'originalScriptName']
      feature == 'features/f_common_names'
    }
  }
}

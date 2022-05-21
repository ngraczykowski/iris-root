package com.silenteight.hsbc.datasource.feature.allowedlist

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity
import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification

class AllowListInvalidAlertFeatureSpec extends Specification {

  def underTest = new AllowListInvalidAlertFeature()

  def 'should return feature values for individual'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >>
          [
              Mock(CustomerIndividual) {
                getGivenName() >> 'givenName'
                getFamilyNameOriginal() >> 'familyNameOriginal'
                getFullNameDerived() >> 'fullNameDerived'
                getMiddleName() >> 'middleName'
                getOriginalScriptName() >> 'originalScriptName'
                getProfileFullName() >> 'profileFullName'
              }
          ]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_invalid_names', 'hsbc_financial_institution']
      characteristicsValues == ['givenName', 'familyNameOriginal', 'fullNameDerived',
                                'middleName', 'originalScriptName', 'profileFullName']
      feature == 'features/invalidAlert'
    }
  }

  def 'should return feature values for entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
      getCustomerEntities() >>
          [
              Mock(CustomerEntity) {
                getEntityName() >> 'entityName'
                getEntityNameOriginal() >> 'entityNameOriginal'
                getOriginalScriptName() >> 'originalScriptName'
              }
          ]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      allowListNames == ['hsbc_invalid_names', 'hsbc_financial_institution']
      characteristicsValues == ['entityName', 'entityNameOriginal', 'originalScriptName']
      feature == 'features/invalidAlert'
    }
  }
}

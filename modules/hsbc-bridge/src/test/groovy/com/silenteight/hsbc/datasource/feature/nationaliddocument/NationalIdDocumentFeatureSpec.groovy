package com.silenteight.hsbc.datasource.feature.nationaliddocument

import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.extractors.document.NationalIdDocumentQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature
import com.silenteight.hsbc.datasource.fixtures.FullMatch

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class NationalIdDocumentFeatureSpec extends Specification implements FullMatch {

  def documentQueryConfigurer = new NationalIdDocumentQueryConfigurer().create()

  def underTest = new NationalIdDocumentFeature(documentQueryConfigurer)

  def 'should retrieve national id document empty lists when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isEntity() >> true
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.NATIONAL_ID_DOCUMENT.fullName
      alertedPartyDocumentNumbers == []
      watchlistDocumentNumbers == []
      alertedPartyCountries == []
      watchlistCountries == []
    }
  }

  def 'should retrieve national id document values when customer is individual'() {
    when:
    def result = underTest.retrieve(FULL_MATCH_1)

    then:
    with(result) {
      feature == Feature.NATIONAL_ID_DOCUMENT.fullName
      alertedPartyDocumentNumbers == ['Y999999']
      watchlistDocumentNumbers == ['78845ID', '4568795132', '5465498756']
      alertedPartyCountries == ['UNITED KINGDOM', 'GB', 'DE', 'GERMANY', 'HK']
      assertThat(watchlistCountries).containsExactly(
          'VNM GB IRN', 'UNITED STATES', 'US', 'IRAN, ISLAMIC REPUBLIC OF', 'IR', 'CHABAHAR',
          'UNK UNKW', 'VIET NAM', 'GB', 'IRAN')
    }
  }
}

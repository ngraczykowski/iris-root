package com.silenteight.hsbc.datasource.feature

import com.silenteight.hsbc.bridge.match.MatchRawData
import com.silenteight.hsbc.datasource.extractors.document.DocumentQueryConfigurer
import com.silenteight.hsbc.datasource.feature.nationalityid.NationalityIdFeature

import spock.lang.Specification

class NationalityIdFeatureSpec extends Specification {

  def documentQueryConfigurer = new DocumentQueryConfigurer()

  def underTest = new NationalityIdFeature(
      documentQueryConfigurer.alertedPartyDocumentQuery(),
      documentQueryConfigurer.matchPartyDocumentQuery())

  def 'should retrieve nationality id feature values'() {
    given:
    def matchRawData = Mock(MatchRawData)

    when:
    def result = underTest.retrieve(matchRawData)

    then:
    matchRawData.isIndividual() >> false

    with(result) {
      feature == Feature.NATIONALITY_ID.name
      alertedPartyDocumentNumbers == null
      watchlistDocumentNumbers == null
    }
  }
}

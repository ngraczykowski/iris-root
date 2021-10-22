package com.silenteight.hsbc.datasource.extractors.ispepV2

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity

import spock.lang.Specification

class EntitiesListRecordIdExtractorSpec extends Specification {

  def 'returns correct value even if list record id that should be the same has different value'() {
    given:
    def firstRecordId = Mock(WorldCheckEntity) {
      getListRecordId() >> '376829'
    }

    def secondRecordId = Mock(WorldCheckEntity) {
      getListRecordId() >> '399999'
    }

    when:
    def underTest = new EntitiesListRecordIdExtractor([firstRecordId, secondRecordId])
    def actual = underTest.extract()

    then:
    actual == '376829'
  }
}

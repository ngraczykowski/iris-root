package com.silenteight.hsbc.datasource.extractors.ispepV2

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual

import spock.lang.Specification

class IndividualsListRecordIdExtractorSpec extends Specification {

  def 'returns correct value even if list record id that should be the same has different value'() {
    given:
    def firstRecordId = Mock(WorldCheckIndividual) {
      getListRecordId() >> '376829'
    }

    def secondRecordId = Mock(WorldCheckIndividual) {
      getListRecordId() >> '399999'
    }

    when:
    def underTest = new IndividualsListRecordIdExtractor([firstRecordId, secondRecordId])
    def actual = underTest.extract()

    then:
    actual == '376829'
  }
}

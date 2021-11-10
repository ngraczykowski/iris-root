package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity

import spock.lang.Specification

class WorldCheckEntitiesFurtherInformationExtractorSpec extends Specification {

  def 'returns correct value even if furtherInformation that should be the same has different value'() {
    given:
    def firstFurtherInformation = Mock(WorldCheckEntity) {
      getFurtherInformation() >> 'Some Info 1'
    }
    def secondFurtherInformation = Mock(WorldCheckEntity) {
      getFurtherInformation() >> 'Some Info 2'
    }

    when:
    def underTest = new WorldCheckEntitiesFurtherInformationExtractor([firstFurtherInformation, secondFurtherInformation])
    def actual = underTest.extract()

    then:
    actual == 'Some Info 1'
  }
}

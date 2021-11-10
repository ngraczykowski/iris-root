package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual

import spock.lang.Specification

class WorldCheckIndividualsFurtherInformationExtractorSpec extends Specification {

  def 'returns correct value even if furtherInformation that should be the same has different value'() {
    given:
    def firstFurtherInformation = Mock(WorldCheckIndividual) {
      getFurtherInformation() >> 'Some Info 1'
    }
    def secondFurtherInformation = Mock(WorldCheckIndividual) {
      getFurtherInformation() >> 'Some Info 2'
    }

    when:
    def underTest = new WorldCheckIndividualsFurtherInformationExtractor([firstFurtherInformation, secondFurtherInformation])
    def actual = underTest.extract()

    then:
    actual == 'Some Info 1'
  }
}

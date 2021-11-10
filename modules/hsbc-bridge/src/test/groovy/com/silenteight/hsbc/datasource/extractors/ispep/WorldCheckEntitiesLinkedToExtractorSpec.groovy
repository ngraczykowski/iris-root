package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class WorldCheckEntitiesLinkedToExtractorSpec extends Specification {

  def 'returns correct values'() {
    given:
    def firstLinkedTo = Mock(WorldCheckEntity) {
      getLinkedTo() >> '28966;376830;376831;376832;448756;80217'
    }

    def secondLinkedTo = Mock(WorldCheckEntity) {
      getLinkedTo() >> '29999;399990;399991;399992;399993;399994'
    }

    when:
    def underTest = new WorldCheckEntitiesLinkedToExtractor([firstLinkedTo, secondLinkedTo])
    def actual = underTest.extract()

    then:
    assertThat(actual)
        .containsExactly('28966;376830;376831;376832;448756;80217', '29999;399990;399991;399992;399993;399994')
  }
}

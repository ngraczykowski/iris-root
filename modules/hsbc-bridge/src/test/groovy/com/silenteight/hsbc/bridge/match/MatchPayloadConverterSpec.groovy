package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL
import com.silenteight.hsbc.bridge.domain.CountryCtrpScreening
import com.silenteight.hsbc.bridge.domain.CustomerEntities
import com.silenteight.hsbc.bridge.domain.EntityComposite
import com.silenteight.hsbc.bridge.domain.WorldCheckEntities

import spock.lang.Specification

class MatchPayloadConverterSpec extends Specification {

  def underTest = new MatchPayloadConverter()

  def 'should convert dto class to payload and vice versa'() {
    given:
    def dto = new MatchRawData(
        caseId: 1,
        caseWithAlertURL: new CasesWithAlertURL(id: 1),
        entityComposite: new EntityComposite(
            new CustomerEntities(),
            [new WorldCheckEntities()],
            [],
            [new CountryCtrpScreening()]
        )
    )

    when:
    def resultInBytes = underTest.convert(dto)
    def convertedResult = underTest.convert(resultInBytes)

    then:
    resultInBytes.length == 5176
    with(convertedResult) {
      caseId == 1
      caseWithAlertURL
      entityComposite
    }
  }
}

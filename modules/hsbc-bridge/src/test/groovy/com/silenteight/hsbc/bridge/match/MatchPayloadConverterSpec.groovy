package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.domain.CountryCtrpScreening
import com.silenteight.hsbc.bridge.domain.CustomerEntities
import com.silenteight.hsbc.bridge.domain.CustomerIndividuals
import com.silenteight.hsbc.bridge.domain.WorldCheckEntities
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals

import spock.lang.Specification

class MatchPayloadConverterSpec extends Specification {

  def underTest = new MatchPayloadConverter()

  def 'should convert dto class to payload and vice versa'() {
    given:
    def dto = new MatchRawData(
        countryCtrpScreeningIndividuals: [new CountryCtrpScreening()],
        countryCtrpScreeningEntities: [new CountryCtrpScreening()],
        customerEntities: [new CustomerEntities()],
        customerIndividuals: [new CustomerIndividuals()],
        worldCheckEntities: [new WorldCheckEntities()],
        worldCheckIndividuals: [new WorldCheckIndividuals()]
    )

    when:
    def resultInBytes = underTest.convert(dto)
    def convertedResult = underTest.convert(resultInBytes)

    then:
    resultInBytes.length == 9285
    with(convertedResult) {
      !countryCtrpScreeningIndividuals.isEmpty()
      !countryCtrpScreeningEntities.isEmpty()
      !customerEntities.isEmpty()
      !customerIndividuals.isEmpty()
      !worldCheckEntities.isEmpty()
      !worldCheckIndividuals.isEmpty()
    }
  }
}

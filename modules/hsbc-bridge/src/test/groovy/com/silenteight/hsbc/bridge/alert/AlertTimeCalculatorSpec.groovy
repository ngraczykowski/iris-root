package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.json.external.model.CaseInformation
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.time.OffsetDateTime.parse
import static java.util.Optional.empty
import static java.util.Optional.of

class AlertTimeCalculatorSpec extends Specification {

  @Shared
  def invalidDate = '12'
  def dateTimeFormatter = new CustomDateTimeFormatter("[yyyy-MMM-dd HH:mm:ss][dd-MMM-yy]")
  def underTest = new AlertTimeCalculator(dateTimeFormatter.getDateTimeFormatter())

  @Unroll
  def 'should get alert time as `#expectedResult`, `#createdDate`, `#modifiedDate`, `#updatedDateTime`'() {
    given:
    def caseInformation = new CaseInformation(
        createdDateTime: createdDate,
        extendedAttribute13DateTime: modifiedDate,
        updatedDateTime: updatedDateTime)

    when:
    def result = underTest.calculateAlertTime(caseInformation)

    then:
    result == expectedResult

    where:
    createdDate            | modifiedDate           | updatedDateTime        | expectedResult
    null                   | null                   | null                   | empty()
    null                   | null                   | ''                     | empty()
    null                   | ''                     | null                   | empty()
    ''                     | null                   | null                   | empty()
    null                   | ''                     | ''                     | empty()
    ''                     | ''                     | null                   | empty()
    ''                     | null                   | ''                     | empty()
    invalidDate            | invalidDate            | invalidDate            | empty()
    null                   | invalidDate            | invalidDate            | empty()
    invalidDate            | null                   | invalidDate            | empty()
    invalidDate            | invalidDate            | null                   | empty()
    null                   | null                   | invalidDate            | empty()
    null                   | invalidDate            | invalidDate            | empty()

    '2021-FEB-15 09:20:06' | invalidDate            | invalidDate            | empty()
    invalidDate            | '2021-FEB-15 09:20:06' | invalidDate            | empty()
    invalidDate            | '2021-FEB-15 09:20:06' | '2021-FEB-15 09:20:06' | of(parse('2021-02-15T09:20:06Z'))
    invalidDate            | invalidDate            | '2021-FEB-15 09:20:06' | of(parse('2021-02-15T09:20:06Z'))
    '2021-FEB-18 09:20:06' | '2021-FEB-15 09:20:06' | invalidDate            | empty()
    '2021-FEB-15 09:20:06' | '2021-FEB-18 09:20:06' | invalidDate            | of(parse('2021-02-15T09:20:06Z'))
    '2021-FEB-15 09:20:06' | invalidDate            | '2021-FEB-18 09:20:06' | of(parse('2021-02-18T09:20:06Z'))
    '2021-FEB-15 09:20:06' | '2021-FEB-18 09:20:06' | '2021-FEB-18 09:20:06' | of(parse('2021-02-15T09:20:06Z'))
    '2021-FEB-18 09:20:06' | '2021-FEB-15 09:20:06' | '2021-FEB-20 09:20:06' | of(parse('2021-02-20T09:20:06Z'))

    '15-FEB-21'            | invalidDate            | invalidDate            | empty()
    invalidDate            | '15-FEB-21'            | invalidDate            | empty()
    invalidDate            | '15-FEB-21'            | '15-FEB-21'            | of(parse('2021-02-15T00:00Z'))
    invalidDate            | invalidDate            | '15-FEB-21'            | of(parse('2021-02-15T00:00Z'))
    '18-FEB-21'            | '15-FEB-21'            | invalidDate            | empty()
    '15-FEB-21'            | '18-FEB-21'            | invalidDate            | of(parse('2021-02-15T00:00Z'))
    '15-FEB-21'            | invalidDate            | '18-FEB-21'            | of(parse('2021-02-18T00:00Z'))
    '15-FEB-21'            | '18-FEB-21'            | '18-FEB-21'            | of(parse('2021-02-15T00:00Z'))
    '18-FEB-21'            | '15-FEB-21'            | '20-FEB-21'            | of(parse('2021-02-20T00:00Z'))

    invalidDate            | '2021-FEB-15 09:20:06' | '15-FEB-21'            | of(parse('2021-02-15T00:00Z'))
    invalidDate            | '15-FEB-21'            | '2021-FEB-15 09:20:06' | of(parse('2021-02-15T09:20:06Z'))
    '2021-FEB-18 09:20:06' | '15-FEB-21'            | invalidDate            | empty()
    '18-FEB-21'            | '2021-FEB-15 09:20:06' | invalidDate            | empty()
    '15-FEB-21'            | '2021-FEB-18 09:20:06' | invalidDate            | of(parse('2021-02-15T00:00Z'))
    '2021-FEB-15 09:20:06' | '18-FEB-21'            | invalidDate            | of(parse('2021-02-15T09:20:06Z'))
    '15-FEB-21'            | invalidDate            | '2021-FEB-18 09:20:06' | of(parse('2021-02-18T09:20:06Z'))
    '2021-FEB-15 09:20:06' | invalidDate            | '18-FEB-21'            | of(parse('2021-02-18T00:00Z'))
    '2021-FEB-15 09:20:06' | '18-FEB-21'            | '18-FEB-21'            | of(parse('2021-02-15T09:20:06Z'))
    '15-FEB-21'            | '18-FEB-21'            | '2021-FEB-18 09:20:06' | of(parse('2021-02-15T00:00Z'))
    '15-FEB-21'            | '2021-FEB-18 09:20:06' | '18-FEB-21'            | of(parse('2021-02-15T00:00Z'))
    '2021-FEB-15 09:20:06' | '18-FEB-21'            | '2021-FEB-18 09:20:06' | of(parse('2021-02-15T09:20:06Z'))
    '15-FEB-21'            | '2021-FEB-18 09:20:06' | '2021-FEB-18 09:20:06' | of(parse('2021-02-15T00:00Z'))
    '2021-FEB-15 09:20:06' | '2021-FEB-18 09:20:06' | '18-FEB-21'            | of(parse('2021-02-15T09:20:06Z'))
    '2021-FEB-18 09:20:06' | '15-FEB-21'            | '20-FEB-21'            | of(parse('2021-02-20T00:00Z'))
    '18-FEB-21'            | '15-FEB-21'            | '2021-FEB-20 09:20:06' | of(parse('2021-02-20T09:20:06Z'))
    '18-FEB-21'            | '2021-FEB-15 09:20:06' | '20-FEB-21'            | of(parse('2021-02-20T00:00Z'))
    '2021-FEB-18 09:20:06' | '15-FEB-21'            | '2021-FEB-20 09:20:06' | of(parse('2021-02-20T09:20:06Z'))
    '18-FEB-21'            | '2021-FEB-15 09:20:06' | '2021-FEB-20 09:20:06' | of(parse('2021-02-20T09:20:06Z'))
    '2021-FEB-18 09:20:06' | '2021-FEB-15 09:20:06' | '20-FEB-21'            | of(parse('2021-02-20T00:00Z'))
  }
}

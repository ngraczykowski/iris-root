package com.silenteight.hsbc.bridge.json.external.model

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.time.OffsetDateTime.parse
import static java.util.Optional.empty
import static java.util.Optional.of

class CaseInformationSpec extends Specification {

  @Shared
  def invalidDate = '12'

  @Unroll
  def 'should get alert time as #expectedResult, `#createdDate`, `#modifiedDate`, `#stateChangeDate`'() {
    given:
    def caseInformation = new CaseInformation(
        createdDateTime: createdDate,
        extendedAttribute13DateTime: modifiedDate,
        stateChangeDateTime: stateChangeDate)

    when:
    def result = caseInformation.getAlertTime()

    then:
    result == expectedResult

    where:
    createdDate | modifiedDate | stateChangeDate | expectedResult
    null        | null         | null            | empty()
    null        | null         | ''              | empty()
    null        | ''           | null            | empty()
    ''          | null         | null            | empty()
    null        | ''           | ''              | empty()
    ''          | ''           | null            | empty()
    ''          | null         | ''              | empty()
    invalidDate | invalidDate  | invalidDate     | empty()
    null        | invalidDate  | invalidDate     | empty()
    invalidDate | null         | invalidDate     | empty()
    invalidDate | invalidDate  | null            | empty()
    null        | null         | invalidDate     | empty()
    null        | invalidDate  | invalidDate     | empty()
    '15-FEB-21' | invalidDate  | invalidDate     | empty()
    invalidDate | '15-FEB-21'  | invalidDate     | empty()
    invalidDate | '15-FEB-21'  | '15-FEB-21'     | of(parse('2021-02-15T00:00Z'))
    invalidDate | invalidDate  | '15-FEB-21'     | of(parse('2021-02-15T00:00Z'))
    '18-FEB-21' | '15-FEB-21'  | invalidDate     | empty()
    '15-FEB-21' | '18-FEB-21'  | invalidDate     | of(parse('2021-02-15T00:00Z'))
    '15-FEB-21' | invalidDate  | '18-FEB-21'     | of(parse('2021-02-18T00:00Z'))
  }
}

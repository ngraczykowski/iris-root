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
  def 'should get alert time as #expectedResult, stateChangeDate = `#stateChangeDate`'() {
    given:
    def caseInformation = new CaseInformation(stateChangeDateTime: stateChangeDate)

    when:
    def result = caseInformation.getAlertTime()

    then:
    result == expectedResult

    where:
    stateChangeDate | expectedResult
    null            | empty()
    ''              | empty()
    invalidDate     | empty()
    '15-FEB-21'     | of(parse('2021-02-15T00:00Z'))
  }
}

package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.bulk.rest.input.CasesWithAlertURL

import spock.lang.Specification

class AlertRawMapperSpec extends Specification {

  def underTest = new AlertRawMapper()

  def 'should map alertRawData to byte[]'() {
    given:
    def caseWithAlertUrl = new CasesWithAlertURL(id: 1)
    def alert = new AlertRawData(
        casesWithAlertURL: [caseWithAlertUrl]
    )

    when:
    def result = underTest.map(alert)

    then:
    result.length == 1301
  }
}

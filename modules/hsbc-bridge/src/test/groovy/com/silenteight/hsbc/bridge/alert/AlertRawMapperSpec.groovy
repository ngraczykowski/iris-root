package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.rest.model.input.Alert
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation
import com.silenteight.hsbc.bridge.rest.model.input.CasesWithAlertURL

import spock.lang.Specification

class AlertRawMapperSpec extends Specification {

  def underTest = new AlertRawMapper()

  def 'should map alert to alertRawData'() {
    given:
    def caseWithAlertUrl = new CasesWithAlertURL(id: 1)
    def alert = createAlert(caseWithAlertUrl)

    when:
    def result = underTest.map(alert)

    then:
    with (result.casesWithAlertURL) {
      id == caseWithAlertUrl.id
    }
  }

  def createAlert(CasesWithAlertURL casesWithAlertURL) {
    new Alert(systemInformation: new AlertSystemInformation(
        casesWithAlertURL: [casesWithAlertURL]
    ))
  }
}

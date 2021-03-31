package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL
import com.silenteight.hsbc.bridge.rest.model.input.Alert
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation

import spock.lang.Specification

class AlertFacadeSpec extends Specification {

  def alertMapper = Mock(AlertMapper)
  def alertRawMapper = Mock(AlertRawMapper)
  def alertRepository = Mock(AlertRepository)

  def underTest = AlertFacade.builder()
      .alertMapper(alertMapper)
      .alertRawMapper(alertRawMapper)
      .alertRepository(alertRepository)
      .build()

  def 'should prepare and save alert'() {
    given:
    def bulkItemId = 1L
    def alertPayload = "".getBytes()
    def someAlert = new Alert(systemInformation: new AlertSystemInformation())
    def someAlertRawData = new AlertRawData(new CasesWithAlertURL())

    when:
    def result = underTest.prepareAndSaveAlert(bulkItemId, alertPayload)

    then:
    1 * alertMapper.map(alertPayload) >> someAlert
    1 * alertRawMapper.map(someAlert) >> someAlertRawData
    1 * alertMapper.map(someAlertRawData) >> alertPayload
    1 * alertRepository.save(_ as AlertEntity) >> {AlertEntity entity -> entity.id = 2}

    with(result) {
      id == 2
      alert == someAlert
      alertSystemInformation == someAlert.systemInformation
    }
  }
}

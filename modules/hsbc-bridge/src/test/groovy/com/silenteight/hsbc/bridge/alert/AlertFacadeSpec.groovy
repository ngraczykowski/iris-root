package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL

import spock.lang.Specification

class AlertFacadeSpec extends Specification {

  def alertRawMapper = Mock(AlertRawMapper)
  def alertRepository = Mock(AlertRepository)

  def underTest = AlertFacade.builder()
      .alertRawMapper(alertRawMapper)
      .alertRepository(alertRepository)
      .build()

  def 'should prepare and save alert'() {
    given:
    def bulkItemId = 1L
    def alertPayload = "".getBytes()
    def someAlertRawData = new AlertRawData(
        casesWithAlertURL: [new CasesWithAlertURL()]
    )

    when:
    def result = underTest.prepareAndSaveAlert(bulkItemId, alertPayload)

    then:
    1 * alertRawMapper.mapBulkPayload(alertPayload) >> someAlertRawData
    1 * alertRepository.save(_ as AlertEntity) >> {AlertEntity entity -> entity.id = 2}

    with(result) {
      id == 2
      alertRawData == someAlertRawData
    }
  }
}

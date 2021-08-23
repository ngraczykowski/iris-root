package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter.InputCommand

import spock.lang.Specification

import javax.persistence.EntityManager

class AlertFacadeSpec extends Specification {

  def alertPayloadConverter = Mock(AlertPayloadConverter)
  def entityManger = Mock(EntityManager)
  def repository = Mock(AlertRepository)

  def underTest = AlertFacade.builder()
      .alertPayloadConverter(alertPayloadConverter)
      .repository(repository)
      .entityManager(entityManger)
      .build()

  def 'should create raw alerts'() {
    given:
    def bulkId = "1L"
    def inputStream = Mock(InputStream)

    when:
    underTest.createRawAlerts(bulkId, inputStream)

    then:
    1 * alertPayloadConverter.
        convertAndConsumeAlertData(
            {InputCommand command -> command.bulkId == bulkId && command.inputStream == inputStream},
            underTest)
  }
}

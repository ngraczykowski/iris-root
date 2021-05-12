package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.ProcessingResult.ProcessedAlert
import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.json.external.model.HsbcMatch
import com.silenteight.hsbc.bridge.match.Match

import spock.lang.Specification

class AlertFacadeSpec extends Specification {

  def alertPayloadConverter = Mock(AlertPayloadConverter)
  def repository = Mock(AlertRepository)
  def relationshipProcessor = Mock(RelationshipProcessor)

  def underTest = AlertFacade.builder()
      .alertPayloadConverter(alertPayloadConverter)
      .relationshipProcessor(relationshipProcessor)
      .repository(repository)
      .build()

  def 'should prepare and save alert'() {
    given:
    def bulkItemId = "1L"
    def somePayload = "".getBytes()
    def alertsData = [createAlertData()]
    var processedAlert = ProcessedAlert.builder()
        .externalId('externalId')
        .matches(createMatches())
        .build()
    def processingResult = new ProcessingResult([processedAlert])

    when:
    def result = underTest.createAndSaveAlerts(bulkItemId, somePayload)

    then:
    1 * alertPayloadConverter.convert(somePayload) >> alertsData
    1 * relationshipProcessor.process(alertsData) >> processingResult
    1 * repository.save(_ as AlertEntity) >> {AlertEntity entity -> entity.id = 2}

   result.size() == 1
  }

  def createMatches() {
    [new Match('match-id', new HsbcMatch())]
  }

  def createAlertData() {
    new AlertData()
  }
}

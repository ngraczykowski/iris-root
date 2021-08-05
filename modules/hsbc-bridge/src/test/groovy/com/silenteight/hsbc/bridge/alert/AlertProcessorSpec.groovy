package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.ProcessingResult.ProcessedAlert
import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation
import com.silenteight.hsbc.bridge.match.MatchFacade

import spock.lang.Specification

import static com.silenteight.hsbc.bridge.alert.AlertStatus.STORED

class AlertProcessorSpec extends Specification {

  def payloadConverter = Mock(AlertPayloadConverter)
  def repository = Mock(AlertRepository)
  def relationshipProcessor = Mock(RelationshipProcessor)
  def matchFacade = Mock(MatchFacade)

  def underTest = new AlertProcessor(
      payloadConverter, repository, relationshipProcessor, matchFacade)

  def fixtures = new Fixtures()

  def 'should preprocess alerts'() {
    given:
    def someBulkId = 'bulk-test'

    when:
    underTest.preProcessAlertsWithinBulk(someBulkId)

    then:
    fixtures.okAlert.status == AlertStatus.PRE_PROCESSED

    with(fixtures.invalidAlert) {
      status == AlertStatus.ERROR
      errorMessage == 'Alert id pre-processing failure'
    }

    with(fixtures.duplicatedAlert) {
      status == AlertStatus.ERROR
      errorMessage == 'Alert ID is duplicated within a batch'
    }

    1 * repository.findByBulkIdAndStatus(someBulkId, STORED) >>
        [fixtures.okAlert, fixtures.invalidAlert, fixtures.duplicatedAlert].stream()
    1 * payloadConverter.convertAlertData(fixtures.invalidAlert.getPayloadAsBytes()) >>
        {throw new RuntimeException()}
    1 * payloadConverter.convertAlertData(fixtures.okAlert.getPayloadAsBytes()) >>
        fixtures.alertData
    1 * payloadConverter.convertAlertData(fixtures.duplicatedAlert.getPayloadAsBytes()) >>
        fixtures.alertData
    2 * relationshipProcessor.process(fixtures.alertData) >> fixtures.processedAlert
    2 * matchFacade.prepareAndSaveMatches(1, fixtures.processedAlert.matches)
    1 * repository.findDuplicateAlertsByBulkId(someBulkId) >> [fixtures.duplicatedAlert]
  }

  class Fixtures {

    def invalidAlert = createAlert('invalid')
    def okAlert = createAlert('ok')
    def duplicatedAlert = createAlert('duplicated')

    def alertData = new AlertData(
        caseInformation: new CaseInformation()
    )
    def processedAlert = new ProcessedAlert([], '')

    def createAlert(name) {
      def entity = new AlertEntity(id: 1, bulkId: 'bulkId')
      entity.setName(name)
      def payload = new AlertDataPayloadEntity(
          payload: name.getBytes()
      )
      entity.setPayload(payload)
      entity
    }
  }
}

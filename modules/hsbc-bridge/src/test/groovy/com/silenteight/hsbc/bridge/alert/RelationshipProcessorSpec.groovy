package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation
import com.silenteight.hsbc.bridge.json.external.model.CustomerIndividual
import com.silenteight.hsbc.bridge.json.external.model.Relationship
import com.silenteight.hsbc.bridge.json.external.model.WorldCheckIndividual

import spock.lang.Specification

class RelationshipProcessorSpec extends Specification {

  def underTest = new RelationshipProcessor()

  def 'should process alerts'() {
    given:
    def alert1 = new AlertData(
        caseInformation: new CaseInformation(
            keyLabel: 'validAlert'
        )
    )
    alert1.customerIndividuals.add(new CustomerIndividual(recordId: '1'))
    alert1.worldCheckIndividuals.add(new WorldCheckIndividual(recordId: '2'))
    alert1.relationships.add(new Relationship(recordId: '1', relatedRecordId: '2'))

    def alert2 = new AlertData(
        caseInformation: new CaseInformation(
            keyLabel: 'invalidAlert'
        ))

    when:
    def result = underTest.process([alert1, alert2])

    then:
    def processedAlerts = result.processedAlerts
    processedAlerts.size() == 2

    with(processedAlerts.first()) {
      externalId == 'validAlert'
      !matches.isEmpty()
    }

    with(processedAlerts[1]) {
      externalId == 'invalidAlert'
      errorMessage.get() == 'No relationships defined!'
    }
  }
}

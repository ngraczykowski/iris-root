package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider

import spock.lang.Specification

class IngestServiceSpec extends Specification {

  def objectUnderTest = IngestService.builder()
      .listeners([])
      .build()

  def "should ingest alert and try to receive recommendation"() {
    given:
    def someDecisionGroup = 'decisionGroup'
    ObjectId objectId = ObjectId.builder().build()
    def alert = Alert.builder()
        .id(objectId)
        .details(
            AlertDetails.builder()
                .systemId("system-id")
                .build())
        .decisionGroup(someDecisionGroup).build()
    def messageProvider = Mock(MessagePropertiesProvider)

    when:
    def result = objectUnderTest.ingestAlertAndTryToReceiveRecommendation(alert, messageProvider)

    then:
    result == Optional.empty()
  }
}

package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.proto.serp.v1.recommendation.Recommendation
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.sep.base.common.messaging.MessageSender
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider

import spock.lang.Specification

import javax.swing.text.html.Option

class IngestServiceSpec extends Specification {

  def messageSender = Mock(MessageSender)
  def objectUnderTest = IngestService.builder()
      .sender(messageSender)
      .listeners([])
      .build()

  def "should ingest alert and try to receive recommendation"() {
    given:
    def someDecisionGroup = 'decisionGroup'
    ObjectId objectId = ObjectId.builder().build()
    def alert = Alert.builder().id(objectId).decisionGroup(someDecisionGroup).build()
    def messageProvider = Mock(MessagePropertiesProvider)

    when:
    def result = objectUnderTest.ingestAlertAndTryToReceiveRecommendation(alert, messageProvider)

    then:
    result == Optional.empty()
  }
}

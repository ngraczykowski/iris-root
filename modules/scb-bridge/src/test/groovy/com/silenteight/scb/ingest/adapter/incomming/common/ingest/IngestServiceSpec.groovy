package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.proto.serp.v1.alert.Alert
import com.silenteight.proto.serp.v1.recommendation.Recommendation
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction
import com.silenteight.sep.base.common.messaging.MessageSender
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider

import spock.lang.Specification

class IngestServiceSpec extends Specification {

  def messageSender = Mock(MessageSender)
  def objectUnderTest = IngestService.builder()
      .sender(messageSender)
      .listeners([])
      .build()
  def someRecommendation = createRecommendation()

  def "should ingest alert and try to receive recommendation"() {
    given:
    def someDecisionGroup = 'decisionGroup'
    def alert = Alert.newBuilder().setDecisionGroup(someDecisionGroup).build()
    def messageProvider = Mock(MessagePropertiesProvider)

    when:
    def result = objectUnderTest.ingestAlertAndTryToReceiveRecommendation(alert, messageProvider)

    then:
    result.get() == someRecommendation
    1 * messageSender.sendAndReceive(
        {it.getFlags() == 13 && it.hasIngestedAt()},
        messageProvider) >> Optional.of(someRecommendation)
  }

  def createRecommendation() {
    Recommendation.newBuilder()
        .setAction(RecommendedAction.ACTION_FALSE_POSITIVE)
        .setComment('Comment')
        .build()
  }
}

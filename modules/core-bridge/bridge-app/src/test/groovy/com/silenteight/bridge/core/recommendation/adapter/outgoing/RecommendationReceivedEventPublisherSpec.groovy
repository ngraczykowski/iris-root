package com.silenteight.bridge.core.recommendation.adapter.outgoing

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationRabbitProperties
import com.silenteight.proto.recommendation.api.v1.RecommendationsReceived

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RecommendationReceivedEventPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new RecommendationRabbitProperties("exchange")

  @Subject
  def underTest = new RecommendationReceivedEventPublisher(rabbitTemplate, properties)

  def "should send message on exchange"() {
    given:
    def analysisName = "analysis"
    def alertNames = ["alert/1"]
    def event = new RecommendationsReceivedEvent(analysisName, alertNames)
    def message = RecommendationsReceived.newBuilder()
        .setAnalysisId(analysisName)
        .addAllAlertIds(alertNames)
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(properties.exchangeName(), "", message)
  }
}

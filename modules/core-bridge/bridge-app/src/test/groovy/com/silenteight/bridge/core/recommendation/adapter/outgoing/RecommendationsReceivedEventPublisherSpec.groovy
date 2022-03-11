package com.silenteight.bridge.core.recommendation.adapter.outgoing

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationOutgoingRecommendationsReceivedConfigurationProperties
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RecommendationsReceivedEventPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new RecommendationOutgoingRecommendationsReceivedConfigurationProperties("exchange")

  @Subject
  def underTest = new RecommendationsReceivedEventPublisher(rabbitTemplate, properties)

  def "should send message on exchange"() {
    given:
    def analysisName = "analysis"
    def alertNames = ["alert/1"]
    def event = new RecommendationsReceivedEvent(analysisName, alertNames)
    def message = RecommendationsStored.newBuilder()
        .setAnalysisName(analysisName)
        .addAllAlertNames(alertNames)
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(properties.exchangeName(), "", message)
  }
}

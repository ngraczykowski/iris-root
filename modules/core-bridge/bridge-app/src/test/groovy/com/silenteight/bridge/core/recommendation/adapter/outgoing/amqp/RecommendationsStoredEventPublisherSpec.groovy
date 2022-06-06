package com.silenteight.bridge.core.recommendation.adapter.outgoing.amqp

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStoredEvent
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationOutgoingRecommendationsStoredConfigurationProperties
import com.silenteight.bridge.core.registration.domain.RegistrationFixtures
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RecommendationsStoredEventPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new RecommendationOutgoingRecommendationsStoredConfigurationProperties("exchange")

  @Subject
  def underTest = new RecommendationsStoredEventPublisher(rabbitTemplate, properties)

  def "should send message on exchange"() {
    given:
    def analysisName = "analysis"
    def alertNames = ["alert/1"]
    def event = new RecommendationsStoredEvent(analysisName, alertNames, false, RegistrationFixtures.BATCH_PRIORITY)
    def message = RecommendationsStored.newBuilder()
        .setAnalysisName(analysisName)
        .addAllAlertNames(alertNames)
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(
        properties.exchangeName(), "", message, _)
  }
}

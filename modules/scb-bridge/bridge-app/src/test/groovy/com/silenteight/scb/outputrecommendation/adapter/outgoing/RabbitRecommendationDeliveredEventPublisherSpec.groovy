package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsDeliveredEvent
import com.silenteight.scb.outputrecommendation.infrastructure.amqp.OutputRecommendationDeliveredProperties

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RabbitRecommendationDeliveredEventPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new OutputRecommendationDeliveredProperties("test_exchange")

  @Subject
  def underTest = new RabbitRecommendationDeliveredEventPublisher(rabbitTemplate, properties)

  def "should send recommendation delivered event"() {
    given:
    def batchId = 'batchId'
    def analysisName = 'analysisName'
    def event = new RecommendationsDeliveredEvent(batchId, analysisName)
    def recommendationsDelivered = RecommendationsDelivered.newBuilder()
        .setBatchId(batchId)
        .setAnalysisName(analysisName)
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(properties.exchangeName(), "", recommendationsDelivered)
  }
}

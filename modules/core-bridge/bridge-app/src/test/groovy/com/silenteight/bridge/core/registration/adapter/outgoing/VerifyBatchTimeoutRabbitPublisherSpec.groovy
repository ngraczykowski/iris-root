package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingVerifyBatchTimeoutProperties
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class VerifyBatchTimeoutRabbitPublisherSpec extends Specification {

  def amqpProperties = new AmqpRegistrationOutgoingVerifyBatchTimeoutProperties('exchange')
  def rabbitTemplate = Mock(RabbitTemplate)

  @Subject
  def underTest = new VerifyBatchTimeoutRabbitPublisher(amqpProperties, rabbitTemplate)

  def "should publish message with delay header"() {
    given:
    def batchId = Fixtures.BATCH_ID
    def event = new VerifyBatchTimeoutEvent(batchId)
    def message = MessageVerifyBatchTimeout.newBuilder()
        .setBatchId(event.batchId())
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(amqpProperties.exchangeName(), '', message)
  }
}

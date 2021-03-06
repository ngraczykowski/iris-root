package com.silenteight.bridge.core.registration.adapter.outgoing.amqp

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingVerifyBatchTimeoutProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.RegistrationVerifyBatchTimeoutProperties
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout

import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class VerifyBatchTimeoutRabbitPublisherSpec extends Specification {

  def amqpProperties = new AmqpRegistrationOutgoingVerifyBatchTimeoutProperties('exchange')
  def registrationProperties = new RegistrationVerifyBatchTimeoutProperties(true, Duration.ofSeconds(10))
  def rabbitTemplate = Mock(RabbitTemplate)

  @Subject
  def underTest = new VerifyBatchTimeoutRabbitPublisher(amqpProperties, registrationProperties, rabbitTemplate)

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
    1 * rabbitTemplate
        .convertAndSend(amqpProperties.exchangeName(), '', message, _ as MessagePostProcessor)
  }
}

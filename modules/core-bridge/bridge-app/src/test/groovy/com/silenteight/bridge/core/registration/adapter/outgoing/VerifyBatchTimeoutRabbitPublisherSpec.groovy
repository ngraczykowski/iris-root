package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingVerifyBatchTimeoutProperties
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout

import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

import static VerifyBatchTimeoutRabbitPublisher.DELAY_HEADER_NAME

class VerifyBatchTimeoutRabbitPublisherSpec extends Specification {

  def amqpProperties = new AmqpRegistrationOutgoingVerifyBatchTimeoutProperties(
      'exchange', Duration.ofMinutes(60))
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
    1 * rabbitTemplate
        .convertAndSend(amqpProperties.exchangeName(), '', message, _ as MessagePostProcessor) >>
        {List args ->
          def messagePostProcessor = args[3] as MessagePostProcessor
          def rabbitMessage = new Message()
          messagePostProcessor.postProcessMessage(rabbitMessage)
          assert rabbitMessage
              .getMessageProperties()
              .getHeader(DELAY_HEADER_NAME) == amqpProperties.delayTime().toMillis()
        }
  }
}

package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties
import com.silenteight.proto.registration.api.v1.MessageBatchError

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RabbitEventPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new AmqpRegistrationOutgoingNotifyBatchErrorProperties('exchange')

  @Subject
  def underTest = new RabbitEventPublisher(rabbitTemplate, properties)

  def 'notify error for batch'() {
    given:
    def batchError = BatchError.builder()
        .id('batchId')
        .errorDescription('Failed to register batch in Core Bridge Registration')
        .batchMetadata("batchMetadata")
        .build()

    def messageBatchError = MessageBatchError.newBuilder()
        .setBatchId('batchId')
        .setErrorDescription('Failed to register batch in Core Bridge Registration')
        .setBatchMetadata("batchMetadata")
        .build()

    when:
    underTest.publish(batchError)

    then:
    1 * rabbitTemplate.convertAndSend(properties.exchangeName(), "", messageBatchError)
  }
}

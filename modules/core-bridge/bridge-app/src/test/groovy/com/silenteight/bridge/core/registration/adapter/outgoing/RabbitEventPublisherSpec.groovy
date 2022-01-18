package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted
import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.proto.registration.api.v1.MessageBatchError

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RabbitEventPublisherSpec extends Specification {

  def mapper = Mock(RabbitEventMapper)

  def rabbitTemplate = Mock(RabbitTemplate)

  def batchErrorProperties =
      new AmqpRegistrationOutgoingNotifyBatchErrorProperties('error-exchange')

  def batchCompletedProperties =
      new AmqpRegistrationOutgoingNotifyBatchCompletedProperties('completed-exchange')

  @Subject
  def underTest = new RabbitEventPublisher(
      mapper, rabbitTemplate, batchErrorProperties, batchCompletedProperties)

  def 'notify error for batch'() {
    given:
    def batchError = BatchError.builder()
        .id('batchId')
        .errorDescription('Failed to register batch in Core Bridge Registration')
        .batchMetadata('batchMetadata')
        .build()

    def messageBatchError = MessageBatchError.newBuilder()
        .setBatchId('batchId')
        .setErrorDescription('Failed to register batch in Core Bridge Registration')
        .setBatchMetadata('batchMetadata')
        .build()

    when:
    underTest.publish(batchError)

    then:
    1 * mapper.toMessageBatchError(batchError) >> messageBatchError
    1 * rabbitTemplate.convertAndSend(batchErrorProperties.exchangeName(), "", messageBatchError)
  }

  def 'notify batch completed'() {
    given:
    def batchCompleted = BatchCompleted.builder()
        .id('batchId')
        .analysisId('analysisName')
        .alertIds(['firstAlertName', 'secondAlertName'])
        .batchMetadata('batchMetadata')
        .build()

    def messageBatchCompleted = MessageBatchCompleted.newBuilder()
        .setBatchId('batchId')
        .setAnalysisId('analysisName')
        .addAllAlertIds(['firstAlertName', 'secondAlertName'])
        .setBatchMetadata('batchMetadata')
        .build()

    when:
    underTest.publish(batchCompleted)

    then:
    1 * mapper.toMessageBatchCompleted(batchCompleted) >> messageBatchCompleted
    1 * rabbitTemplate
        .convertAndSend(batchCompletedProperties.exchangeName(), "", messageBatchCompleted)
  }
}

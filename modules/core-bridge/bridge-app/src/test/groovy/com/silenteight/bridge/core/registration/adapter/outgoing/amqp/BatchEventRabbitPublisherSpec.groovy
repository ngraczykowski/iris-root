package com.silenteight.bridge.core.registration.adapter.outgoing.amqp

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted
import com.silenteight.bridge.core.registration.domain.model.BatchDelivered
import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchDeliveredProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchTimedOutProperties
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.proto.registration.api.v1.MessageBatchDelivered
import com.silenteight.proto.registration.api.v1.MessageBatchError
import com.silenteight.proto.registration.api.v1.MessageNotifyBatchTimedOut

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class BatchEventRabbitPublisherSpec extends Specification {

  def mapper = Mock(RabbitEventMapper)

  def rabbitTemplate = Mock(RabbitTemplate)

  def batchErrorProperties =
      new AmqpRegistrationOutgoingNotifyBatchErrorProperties(
          'error-exchange', 'solving-routing-key', 'sim-routing-key')

  def batchCompletedProperties =
      new AmqpRegistrationOutgoingNotifyBatchCompletedProperties('completed-exchange')

  def batchTimedOutProperties = new AmqpRegistrationOutgoingNotifyBatchTimedOutProperties(
      'timed-out-exchange')

  def batchDeliveredProperties = new AmqpRegistrationOutgoingNotifyBatchDeliveredProperties(
      'delivered-exchange')

  @Subject
  def underTest = new BatchEventRabbitPublisher(
      mapper, rabbitTemplate, batchErrorProperties, batchCompletedProperties,
      batchTimedOutProperties, batchDeliveredProperties)

  def 'should notify error for batch for #scenario'() {
    given:
    def batchError = BatchError.builder()
        .id('batchId')
        .errorDescription('Failed to register batch in Core Bridge Registration')
        .batchMetadata('batchMetadata')
        .isSimulation(isSimulation)
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
    1 * rabbitTemplate.convertAndSend(batchErrorProperties.exchangeName(), routingKey, messageBatchError)

    where:
    scenario     | isSimulation | routingKey
    'simulation' | true         | 'sim-routing-key'
    'solving'    | false        | 'solving-routing-key'
  }

  def 'should notify batch completed'() {
    given:
    def batchCompleted = BatchCompleted.builder()
        .id('batchId')
        .analysisName('analysisName')
        .batchMetadata('batchMetadata')
        .build()

    def messageBatchCompleted = MessageBatchCompleted.newBuilder()
        .setBatchId('batchId')
        .setAnalysisName('analysisName')
        .setBatchMetadata('batchMetadata')
        .build()

    when:
    underTest.publish(batchCompleted)

    then:
    1 * mapper.toMessageBatchCompleted(batchCompleted) >> messageBatchCompleted
    1 * rabbitTemplate
        .convertAndSend(batchCompletedProperties.exchangeName(), "", messageBatchCompleted)
  }

  def 'should notify batch timed out'() {
    given:
    def analysisName = 'analysisName'
    def alertNames = ['firstAlertName', 'secondAlertName']
    def event = new BatchTimedOut(analysisName, alertNames)
    def message = MessageNotifyBatchTimedOut.newBuilder()
        .setAnalysisName(analysisName)
        .addAllAlertNames(alertNames)
        .build()

    when:
    underTest.publish(event)

    then:
    1 * mapper.toMessageNotifyBatchTimedOut(event) >> message
    1 * rabbitTemplate.convertAndSend(batchTimedOutProperties.exchangeName(), "", message)
  }

  def 'should notify batch delivered'() {
    given:
    def batchId = 'batchId'
    def analysisName = 'analysisName'
    def event = new BatchDelivered(batchId, analysisName)
    def message = MessageBatchDelivered.newBuilder()
        .setBatchId(batchId)
        .setAnalysisName(analysisName)
        .build()

    when:
    underTest.publish(event)

    then:
    1 * mapper.toMessageBatchDelivered(event) >> message
    1 * rabbitTemplate.convertAndSend(batchDeliveredProperties.exchangeName(), '', message)
  }
}

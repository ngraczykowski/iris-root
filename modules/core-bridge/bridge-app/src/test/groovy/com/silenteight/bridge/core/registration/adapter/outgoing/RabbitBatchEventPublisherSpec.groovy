package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.model.*
import com.silenteight.bridge.core.registration.domain.port.outgoing.VerifyBatchTimeoutPublisher
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchTimedOutProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.proto.registration.api.v1.MessageBatchError
import com.silenteight.proto.registration.api.v1.MessageNotifyBatchTimedOut

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class RabbitBatchEventPublisherSpec extends Specification {

  def mapper = Mock(RabbitEventMapper)

  def rabbitTemplate = Mock(RabbitTemplate)

  def batchErrorProperties =
      new AmqpRegistrationOutgoingNotifyBatchErrorProperties('error-exchange')

  def batchCompletedProperties =
      new AmqpRegistrationOutgoingNotifyBatchCompletedProperties('completed-exchange')

  def batchTimedOutProperties = new AmqpRegistrationOutgoingNotifyBatchTimedOutProperties(
      'timed-out-exchange')

  def batchProcessingTimeoutPublisher = Mock(VerifyBatchTimeoutPublisher)

  @Subject
  def underTest = new BatchEventRabbitPublisher(
      mapper, rabbitTemplate, batchErrorProperties, batchCompletedProperties,
      batchTimedOutProperties, batchProcessingTimeoutPublisher)

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

  def 'notify batch created'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def batchCreated = new BatchCreated(batchId)

    when:
    underTest.publish(batchCreated)

    then:
    1 * batchProcessingTimeoutPublisher.publish(new VerifyBatchTimeoutEvent(batchId))
  }

  def 'notify batch timed out'() {
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
}

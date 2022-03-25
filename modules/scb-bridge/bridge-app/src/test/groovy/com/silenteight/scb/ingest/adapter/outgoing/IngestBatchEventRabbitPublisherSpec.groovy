package com.silenteight.scb.ingest.adapter.outgoing

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.BatchReadEvent
import com.silenteight.scb.ingest.domain.model.Batch.Priority
import com.silenteight.scb.ingest.domain.model.IngestBatchMessage
import com.silenteight.scb.ingest.infrastructure.amqp.AmqpIngestIncomingBatchProcessingProperties

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.scb.ingest.adapter.outgoing.Fixtures.BATCH_ID
import static com.silenteight.scb.ingest.adapter.outgoing.Fixtures.BATCH_READ_EVENT_JSON
import static com.silenteight.scb.ingest.infrastructure.amqp.IngestRabbitConfiguration.EMPTY_ROUTING_KEY

class IngestBatchEventRabbitPublisherSpec extends Specification {

  def properties = new AmqpIngestIncomingBatchProcessingProperties("queueName", "exchangeName")
  def rabbitTemplate = Mock(RabbitTemplate)
  def objectMapper = Mock(ObjectMapper)

  @Subject
  def underTest = new IngestBatchEventRabbitPublisher(
      rabbitTemplate,
      properties,
      objectMapper)

  def "should_publish_successfully"() {
    given:
    def batchMessage = new IngestBatchMessage(new BatchReadEvent(BATCH_ID), Priority.MEDIUM)

    when:
    underTest.publish(batchMessage);

    then:
    1 * objectMapper.writeValueAsString(_) >> BATCH_READ_EVENT_JSON
    1 * rabbitTemplate.convertAndSend(properties.exchangeName(), EMPTY_ROUTING_KEY, _ as Message)
  }

  def "should_not_publish_and_finish_without_error_after_throw_exception_ny one_of_function"() {
    given:
    def batchMessage = new IngestBatchMessage(new BatchReadEvent(BATCH_ID), Priority.MEDIUM)

    when:
    underTest.publish(batchMessage);

    then:
    1 * objectMapper.writeValueAsString(_) >> {throw new JsonProcessingException("test exception")}
    0 * rabbitTemplate.convertAndSend(properties.exchangeName(), EMPTY_ROUTING_KEY, _ as Message)
  }
}

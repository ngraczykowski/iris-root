package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.domain.exceptons.IngestJsonMessageException;
import com.silenteight.scb.ingest.domain.model.IngestBatchMessage;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestBatchEventPublisher;
import com.silenteight.scb.ingest.infrastructure.amqp.AmqpIngestIncomingBatchProcessingProperties;

import io.vavr.control.Try;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.silenteight.scb.ingest.infrastructure.amqp.IngestRabbitConfiguration.EMPTY_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
class IngestBatchEventRabbitPublisher implements IngestBatchEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final AmqpIngestIncomingBatchProcessingProperties amqpBatchProcessingProperties;
  private final PayloadConverter converter;

  @Override
  public void publish(IngestBatchMessage batchMessage) {
    final var exchangeName = amqpBatchProcessingProperties.exchangeName();
    final var internalBatchId = batchMessage.event().internalBatchId();
    Try.run(() -> sendMessage(batchMessage, exchangeName))
        .onSuccess(unused ->
            log.debug("Batch id {} was sent to {} exchange", internalBatchId, exchangeName))
        .onFailure(e ->
            log.error("Encountered error sending Batch id {} to {} exchange",
                internalBatchId, exchangeName, e));
  }

  private void sendMessage(IngestBatchMessage batchMessage, String exchangeName) {
    var message = createMessage(batchMessage);
    rabbitTemplate.convertAndSend(exchangeName, EMPTY_ROUTING_KEY, message);
  }

  private Message createMessage(IngestBatchMessage batchMessage) {
    var orderJson = converter.serializeFromObjectToJson(batchMessage.event())
        .getOrElseThrow(IngestJsonMessageException::new);
    return MessageBuilder
        .withBody(orderJson.getBytes())
        .setPriority(batchMessage.priority().getValue())
        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
        .build();
  }
}

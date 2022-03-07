package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent;
import com.silenteight.bridge.core.registration.domain.port.outgoing.VerifyBatchTimeoutPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingVerifyBatchTimeoutProperties;
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class VerifyBatchTimeoutRabbitPublisher implements VerifyBatchTimeoutPublisher {

  private static final String DELAY_HEADER_NAME = "x-delay";

  private final AmqpRegistrationOutgoingVerifyBatchTimeoutProperties properties;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(VerifyBatchTimeoutEvent event) {
    log.info(
        "Send verify batch timeout notification. Batch id: {}, delay time: {}",
        event.batchId(), properties.delayTime());

    final var routingKey = "";
    final var message = createMessage(event);

    // Temporarily commented out due to ALL-657. Will be reimplemented in ALL-655
    //rabbitTemplate.convertAndSend(properties.exchangeName(), routingKey, message, setDelayTime());
  }

  private MessageVerifyBatchTimeout createMessage(VerifyBatchTimeoutEvent event) {
    return MessageVerifyBatchTimeout.newBuilder()
        .setBatchId(event.batchId())
        .build();
  }

  private MessagePostProcessor setDelayTime() {
    return message -> {
      message
          .getMessageProperties()
          .setHeader(DELAY_HEADER_NAME, properties.delayTime().toMillis());
      return message;
    };
  }
}

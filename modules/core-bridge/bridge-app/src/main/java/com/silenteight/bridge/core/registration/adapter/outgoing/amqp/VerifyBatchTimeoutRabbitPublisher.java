package com.silenteight.bridge.core.registration.adapter.outgoing.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent;
import com.silenteight.bridge.core.registration.domain.port.outgoing.VerifyBatchTimeoutPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingVerifyBatchTimeoutProperties;
import com.silenteight.bridge.core.registration.infrastructure.amqp.RegistrationVerifyBatchTimeoutProperties;
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class VerifyBatchTimeoutRabbitPublisher implements VerifyBatchTimeoutPublisher {

  private final AmqpRegistrationOutgoingVerifyBatchTimeoutProperties properties;
  private final RegistrationVerifyBatchTimeoutProperties registrationVerifyBatchTimeoutProperties;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(VerifyBatchTimeoutEvent event) {
    if (registrationVerifyBatchTimeoutProperties.enabled()) {
      log.info("Send verify batch timeout notification. Batch id: {}", event.batchId());

      final var routingKey = "";
      final var message = createMessage(event);
      rabbitTemplate.convertAndSend(properties.exchangeName(), routingKey, message, m -> {
        m.getMessageProperties()
            .setExpiration(
                String.valueOf(registrationVerifyBatchTimeoutProperties.delayTime().toMillis()));
        return m;
      });
    }
  }

  private MessageVerifyBatchTimeout createMessage(VerifyBatchTimeoutEvent event) {
    return MessageVerifyBatchTimeout.newBuilder()
        .setBatchId(event.batchId())
        .build();
  }
}

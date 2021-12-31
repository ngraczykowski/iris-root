package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.RegistrationRabbitProperties;
import com.silenteight.proto.registration.api.v1.MessageBatchError;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RabbitEventPublisher implements EventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RegistrationRabbitProperties registrationRabbitProperties;

  @Override
  public void publish(BatchError event) {
    var msg = MessageBatchError.newBuilder()
        .setBatchId(event.id())
        .setErrorDescription(event.errorDescription())
        .build();

    log.info("Send error notification for batch with id: {}", event.id());

    rabbitTemplate.convertAndSend(registrationRabbitProperties.exchangeName(), "", msg);
  }
}

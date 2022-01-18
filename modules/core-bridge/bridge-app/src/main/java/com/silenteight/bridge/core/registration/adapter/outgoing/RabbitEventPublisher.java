package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RabbitEventPublisher implements EventPublisher {

  private final RabbitEventMapper mapper;
  private final RabbitTemplate rabbitTemplate;
  private final AmqpRegistrationOutgoingNotifyBatchErrorProperties batchErrorProperties;
  private final AmqpRegistrationOutgoingNotifyBatchCompletedProperties batchCompletedProperties;

  @Override
  public void publish(BatchError event) {
    var message = mapper.toMessageBatchError(event);

    log.info("Send batch error notification for batch with id: {}", event.id());
    rabbitTemplate.convertAndSend(batchErrorProperties.exchangeName(), "", message);
  }

  @Override
  public void publish(BatchCompleted event) {
    var message = mapper.toMessageBatchCompleted(event);

    log.info("Send batch completed notification for batch with id: {}", event.id());
    rabbitTemplate.convertAndSend(batchCompletedProperties.exchangeName(), "", message);
  }
}

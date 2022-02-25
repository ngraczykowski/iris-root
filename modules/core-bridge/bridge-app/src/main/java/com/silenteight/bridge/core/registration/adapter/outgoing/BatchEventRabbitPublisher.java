package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.VerifyBatchTimeoutPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchTimedOutProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class BatchEventRabbitPublisher implements BatchEventPublisher {

  private final RabbitEventMapper mapper;
  private final RabbitTemplate rabbitTemplate;
  private final AmqpRegistrationOutgoingNotifyBatchErrorProperties batchErrorProperties;
  private final AmqpRegistrationOutgoingNotifyBatchCompletedProperties batchCompletedProperties;
  private final AmqpRegistrationOutgoingNotifyBatchTimedOutProperties batchTimedOutProperties;
  private final VerifyBatchTimeoutPublisher verifyBatchTimeoutPublisher;

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

  @Override
  public void publish(BatchCreated event) {
    verifyBatchTimeoutPublisher.publish(new VerifyBatchTimeoutEvent(event.batchId()));
  }

  @Override
  public void publish(BatchTimedOut event) {
    var message = mapper.toMessageNotifyBatchTimedOut(event);

    log.info(
        "Send batch timed out notification for batch with analysisName: {}, alertNamesCount: {}",
        event.analysisName(), event.alertNames().size());
    rabbitTemplate.convertAndSend(batchTimedOutProperties.exchangeName(), "", message);
  }
}

package com.silenteight.bridge.core.registration.adapter.outgoing.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchDelivered;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchDeliveredProperties;
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
  private final AmqpRegistrationOutgoingNotifyBatchDeliveredProperties batchDeliveredProperties;

  @Override
  public void publish(BatchError event) {
    var message = mapper.toMessageBatchError(event);
    var routingKey = event.isSimulation() ? batchErrorProperties.simulationBatchRoutingKey()
                                          : batchErrorProperties.solvingBatchRoutingKey();

    log.info(
        "Send batch error notification for batch with id: {}, solving: {}", event.id(),
        !event.isSimulation());
    rabbitTemplate.convertAndSend(batchErrorProperties.exchangeName(), routingKey, message);
  }

  @Override
  public void publish(BatchCompleted event) {
    var message = mapper.toMessageBatchCompleted(event);

    log.info("Send batch completed notification for batch with id: {}", event.id());
    rabbitTemplate.convertAndSend(batchCompletedProperties.exchangeName(), "", message);
  }

  @Override
  public void publish(BatchTimedOut event) {
    var message = mapper.toMessageNotifyBatchTimedOut(event);

    log.info(
        "Send batch timed out notification for batch with analysisName: {}, alertNamesCount: {}",
        event.analysisName(), event.alertNames().size());
    rabbitTemplate.convertAndSend(batchTimedOutProperties.exchangeName(), "", message);
  }

  @Override
  public void publish(BatchDelivered event) {
    var message = mapper.toMessageBatchDelivered(event);

    log.info(
        "Send batch delivered for batch with id: {} and analysis name: {}", message.getBatchId(),
        message.getAnalysisName());

    rabbitTemplate.convertAndSend(batchDeliveredProperties.exchangeName(), "", message);
  }
}

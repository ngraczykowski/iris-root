package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.proto.registration.api.v1.MessageBatchError;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class RabbitEventPublisher implements EventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final AmqpRegistrationOutgoingNotifyBatchErrorProperties batchErrorProperties;
  private final AmqpRegistrationOutgoingNotifyBatchCompletedProperties batchCompletedProperties;

  @Override
  public void publish(BatchError event) {
    var msg = MessageBatchError.newBuilder()
        .setBatchId(event.id())
        .setErrorDescription(event.errorDescription())
        .setBatchMetadata(Optional.ofNullable(event.batchMetadata()).orElse(""))
        .build();

    log.info("Send error notification for batch with id: {}", event.id());

    rabbitTemplate.convertAndSend(batchErrorProperties.exchangeName(), "", msg);
  }

  @Override
  public void publish(BatchCompleted event) {
    var message = MessageBatchCompleted.newBuilder()
        .setBatchId(event.id())
        .setAnalysisId(event.analysisId())
        .addAllAlertIds(event.alertIds())
        .setBatchMetadata(event.batchMetadata())
        .build();

    rabbitTemplate.convertAndSend(batchCompletedProperties.exchangeName(), "", message);
  }
}

package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.DataPrepFacade;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import com.google.common.base.Stopwatch;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertMessagesRabbitAmqpListener {

  public static final String QUEUE_NAME_PROPERTY =
      "${amqp.dataprep.incoming.alert-message.queue-name}";

  private final DataPrepFacade dataPrepFacade;

  //TODO add retry

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(AlertMessageStored message) {
    try {
      log.info(
          "Received a message with: batch: {}, alert message: {}", message.getBatchName(),
          message.getMessageName());
      Stopwatch stopwatch = Stopwatch.createStarted();

      dataPrepFacade.processAlert(message);

      long alertMessageHandlingDuration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      log.info(
          "Batch: {}, alert message: {} handled in {} millis", message.getBatchName(),
          message.getMessageName(), alertMessageHandlingDuration);
    } catch (Exception e) {
      log.warn("Unable to handle message", e);
      throw new AmqpRejectAndDontRequeueException(e);
    }
  }
}

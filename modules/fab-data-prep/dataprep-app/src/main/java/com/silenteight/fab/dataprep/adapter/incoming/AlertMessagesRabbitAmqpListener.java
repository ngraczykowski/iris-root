package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.DataPrepFacade;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import com.google.common.base.Stopwatch;
import com.google.protobuf.InvalidProtocolBufferException;
import io.vavr.control.Try;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertMessagesRabbitAmqpListener {

  public static final String QUEUE_NAME_PROPERTY =
      "${amqp.dataprep.incoming.alert-message.queue-name}";
  private static final String BATCH_NAME = "batchName";

  private final DataPrepFacade dataPrepFacade;

  static final String RETRY_PROPERTY = "DeliveryCount";

  @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
  private int retryLimit;

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(Message msg) throws InvalidProtocolBufferException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    boolean isLastTry = isLastTry(msg);
    AlertMessageStored message = AlertMessageStored.parseFrom(msg.getBody());
    MDC.put(BATCH_NAME, message.getBatchName());

    log.info(
        "Received a message with: batch: {}, alert message: {}", message.getBatchName(),
        message.getMessageName());

    Try.run(() -> dataPrepFacade.processAlert(message))
        .onFailure(e -> {
          if (isLastTry) {
            dataPrepFacade.processAlertFailed(message, e);
            log.error("Failed to process message, error occurred", e);
          } else {
            log.error("Failed to process message, error occurred, retrying...", e);
          }
        })
        .andFinally(() -> {
          long alertMessageHandlingDuration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
          log.info(
              "Batch: {}, alert message: {} handled in {} millis", message.getBatchName(),
              message.getMessageName(), alertMessageHandlingDuration);
          MDC.remove(BATCH_NAME);
        })
        .get();
  }

  private boolean isLastTry(Message msg) {
    Integer retriesCount = (Integer) msg.getMessageProperties().getHeaders().get(RETRY_PROPERTY);
    if (retriesCount == null) {
      retriesCount = 0;
    }
    msg.getMessageProperties().setHeader(RETRY_PROPERTY, ++retriesCount);

    return retriesCount == retryLimit;
  }
}

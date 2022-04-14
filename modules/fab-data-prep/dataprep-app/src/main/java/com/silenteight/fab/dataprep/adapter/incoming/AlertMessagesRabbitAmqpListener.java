package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.DataPrepFacade;
import com.silenteight.fab.dataprep.domain.ex.DataPrepException;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import com.google.common.base.Stopwatch;
import com.google.protobuf.InvalidProtocolBufferException;
import io.vavr.control.Try;
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

    log.info(
        "Received a message with: batch: {}, alert message: {}", message.getBatchName(),
        message.getMessageName());

    Try.run(() -> dataPrepFacade.processAlert(message))
        .onFailure(e -> {
          if (isLastTry || message.getState() == State.NEW) {
            dataPrepFacade.processAlertFailed(message);
            log.error("Failed to process message, error occurred", e);
          } else {
            log.error("Failed to process message, error occurred, retrying...", e);
            throw new DataPrepException(e);
          }
        });

    long alertMessageHandlingDuration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
    log.info(
        "Batch: {}, alert message: {} handled in {} millis", message.getBatchName(),
        message.getMessageName(), alertMessageHandlingDuration);
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

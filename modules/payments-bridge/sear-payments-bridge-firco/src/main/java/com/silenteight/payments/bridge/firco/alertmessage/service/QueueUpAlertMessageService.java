package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.AlertStoredEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.callback.model.CallbackException;
import com.silenteight.payments.bridge.firco.callback.port.CreateResponseUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.STORED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@Component
@RequiredArgsConstructor
@Slf4j
class QueueUpAlertMessageService {

  private final AlertMessageStatusService statusService;
  private final AlertMessageStatusRepository repository;
  private final AlertMessageProperties properties;
  private final CreateResponseUseCase createResponseUseCase;

  private final CommonChannels commonChannels;

  void queueUp(FircoAlertMessage alert) {
    if (isQueueOverflowed(alert)) {
      return;
    }

    commonChannels.amqpOutbound().send(
        MessageBuilder.withPayload(buildMessageStore(alert)).build());
    statusService.transitionAlertMessageStatus(alert.getId(), STORED);
    commonChannels.alertStored().send(
        MessageBuilder.withPayload(new AlertStoredEvent(alert.getId())).build()
    );
  }

  private boolean isQueueOverflowed(FircoAlertMessage alert) {
    if (repository.countAllByStatus(STORED) > properties.getStoredQueueLimit()) {
      log.info("AlertMessage [{}] rejected due to queue limit ({})",
          alert.getId(), properties.getStoredQueueLimit());

      try {
        createResponseUseCase.createResponse(alert.getId(), REJECTED_OVERFLOWED);
        statusService.transitionAlertMessageStatus(alert.getId(), REJECTED_OVERFLOWED);
      } catch (CallbackException exception) {
        statusService.transitionAlertMessageStatus(alert.getId(), REJECTED_OVERFLOWED,
            DeliveryStatus.UNDELIVERED);
      }
      return true;
    }
    return false;
  }

  private MessageStored buildMessageStore(FircoAlertMessage message) {
    var id = "alert-messages/" + message.getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }

}

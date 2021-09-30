package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.callback.port.CreateResponseUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

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

  void queueUp(FircoAlertMessage message) {
    if (isQueueOverflowed(message)) {
      return;
    }

    commonChannels.amqpOutbound().send(
        MessageBuilder.withPayload(buildMessageStore(message)).build());
    statusService.transitionAlertMessageStatus(message.getId(), STORED);
  }

  private boolean isQueueOverflowed(FircoAlertMessage message) {
    if (repository.countAllByStatus(STORED) > properties.getStoredQueueLimit()) {
      log.debug("AlertMessage [{}] rejected due to queue limit ({})",
          message.getId(), properties.getStoredQueueLimit());

      createResponseUseCase.createResponse(message.getId(), AlertMessageStatus.REJECTED_OVERFLOWED);
      return true;
    }
    return false;
  }

  private MessageStored buildMessageStore(FircoAlertMessage message) {
    var id = "alert-messages/" + message.getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }

}

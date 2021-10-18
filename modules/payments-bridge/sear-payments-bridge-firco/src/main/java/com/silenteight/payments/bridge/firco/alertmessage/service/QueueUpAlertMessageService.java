package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.AlertStoredEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.STORED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.NA;
import static com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason.QUEUE_OVERFLOWED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@Component
@RequiredArgsConstructor
@Slf4j
class QueueUpAlertMessageService {

  private final AlertMessageStatusService statusService;
  private final AlertMessageStatusRepository repository;
  private final AlertMessageProperties properties;

  private final CommonChannels commonChannels;

  void queueUp(FircoAlertMessage alert) {
    if (isQueueOverflowed()) {
      handleOverflow(alert);
      return;
    }

    commonChannels.messageStoredOutbound().send(
        MessageBuilder.withPayload(buildMessageStore(alert)).build());
    statusService.transitionAlertMessageStatus(alert.getId(), STORED, NA);
    commonChannels.alertStored().send(
        MessageBuilder.withPayload(new AlertStoredEvent(alert.getId())).build()
    );
  }

  private boolean isQueueOverflowed() {
    return repository.countAllByStatus(STORED) > properties.getStoredQueueLimit();
  }

  private void handleOverflow(FircoAlertMessage alert) {
    log.info("AlertMessage [{}] rejected due to queue limit ({})",
        alert.getId(), properties.getStoredQueueLimit());

    commonChannels.recommendationCompleted().send(
        MessageBuilder.withPayload(
            RecommendationCompletedEvent.fromBridge(
                alert.getId(), QUEUE_OVERFLOWED.name())).build()
    );
  }

  private MessageStored buildMessageStore(FircoAlertMessage message) {
    var id = "alert-messages/" + message.getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }
}

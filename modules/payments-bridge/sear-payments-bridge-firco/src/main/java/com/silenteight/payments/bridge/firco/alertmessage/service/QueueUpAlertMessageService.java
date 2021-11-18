package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertStoredEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertStoredPublisherPort;
import com.silenteight.payments.bridge.firco.alertmessage.port.MessageStoredPublisherPort;
import com.silenteight.payments.bridge.firco.alertmessage.port.RecommendationCompletedPublisherPort;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

  private final MessageStoredPublisherPort messageStoredPublisherPort;
  private final AlertStoredPublisherPort alertStoredPublisherPort;
  private final RecommendationCompletedPublisherPort recommendationCompletedPublisherPort;

  void queueUp(FircoAlertMessage alert) {
    if (isQueueOverflowed()) {
      handleOverflow(alert);
      return;
    }

    messageStoredPublisherPort.send(buildMessageStore(alert));
    statusService.transitionAlertMessageStatus(alert.getId(), STORED, NA);
    alertStoredPublisherPort.send(new AlertStoredEvent(alert.getId()));
  }

  private boolean isQueueOverflowed() {
    return repository.countAllByStatus(STORED) > properties.getStoredQueueLimit();
  }

  private void handleOverflow(FircoAlertMessage alert) {
    log.warn("AlertMessage [{}] rejected due to queue limit ({})",
        alert.getId(), properties.getStoredQueueLimit());

    recommendationCompletedPublisherPort.send(RecommendationCompletedEvent.fromBridge(
        alert.getId(), AlertMessageStatus.REJECTED_OVERFLOWED.name(),
        QUEUE_OVERFLOWED.name()));
  }

  private MessageStored buildMessageStore(FircoAlertMessage message) {
    var id = "alert-messages/" + message.getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }
}

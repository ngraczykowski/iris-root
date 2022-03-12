package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.MessageStoredPublisherPort;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
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
  private final CreateRecommendationUseCase createRecommendationUseCase;

  void queueUp(FircoAlertMessage alert) {
    if (isQueueOverflowed()) {
      handleOverflow(alert);
      return;
    }
    statusService.transitionAlertMessageStatus(alert.getId(), STORED, NA);
    log.info("Sending FircoAlertMessage to internal queue for processing:{}", alert.getId());
    messageStoredPublisherPort.send(buildMessageStore(alert));
  }

  private boolean isQueueOverflowed() {
    return repository.countAllByStatus(STORED) > properties.getStoredQueueLimit();
  }

  private void handleOverflow(FircoAlertMessage alert) {
    log.warn("AlertMessage [{}] rejected due to queue limit ({})",
        alert.getId(), properties.getStoredQueueLimit());

    createRecommendationUseCase.create(new BridgeSourcedRecommendation(
        alert.getId(), AlertMessageStatus.REJECTED_OVERFLOWED.name(),
        QUEUE_OVERFLOWED.name()));
  }

  private MessageStored buildMessageStore(FircoAlertMessage message) {
    var id = "alert-messages/" + message.getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }
}

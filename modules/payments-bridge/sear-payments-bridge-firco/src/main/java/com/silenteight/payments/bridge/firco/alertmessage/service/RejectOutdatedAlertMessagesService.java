package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.RejectOutdatedAlertMessagesUseCase;
import com.silenteight.payments.bridge.firco.callback.port.CreateResponseUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import javax.transaction.Transactional;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Component
@Slf4j
public class RejectOutdatedAlertMessagesService implements RejectOutdatedAlertMessagesUseCase {

  private final AlertMessageProperties alertMessageProperties;
  private final AlertMessageStatusRepository repository;
  private final AlertMessageStatusService alertMessageStatusService;
  private final CreateResponseUseCase createResponseUseCase;

  @Setter
  private Clock clock = Clock.systemUTC();

  @Transactional
  @Override
  public boolean process(int chunkSize) {
    var outdatedAlerts = repository.findOutdated(chunkSize, decisionObsoleteSince());
    outdatedAlerts.forEach(this::transitionToOutdated);
    // tradeoff: case when size == chunkSize triggers an additional db hit.
    return outdatedAlerts.size() <= chunkSize;
  }

  private OffsetDateTime decisionObsoleteSince() {
    return OffsetDateTime.now(clock).minus(
      alertMessageProperties.getDecisionRequestedTime().minusSeconds(1));
  }

  private void transitionToOutdated(AlertMessageStatusEntity status) {
    var alertMessageId = status.getAlertMessageId();
    if (isDeliverable(status)) {
      createResponseUseCase.createResponse(alertMessageId, REJECTED_OUTDATED);
      alertMessageStatusService
          .transitionAlertMessageStatus(alertMessageId, REJECTED_OUTDATED);
    } else {
      // The delivery time passed thus we transfer alert to the final state (OUTDATED)
      // and record that it remains undelivered.
      alertMessageStatusService
          .transitionAlertMessageStatus(
              alertMessageId, REJECTED_OUTDATED, DeliveryStatus.UNDELIVERED);
    }
  }

  private boolean isDeliverable(AlertMessageStatusEntity status) {
    return status.getLastModifyAt()
        .plus(alertMessageProperties.getDecisionRequestedTime())
        .isAfter(OffsetDateTime.now(clock));
  }

}

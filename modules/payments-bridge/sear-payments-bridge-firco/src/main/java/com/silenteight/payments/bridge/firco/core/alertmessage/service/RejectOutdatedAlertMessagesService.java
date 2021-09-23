package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.RejectOutdatedAlertMessagesUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import javax.transaction.Transactional;

import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Component
@Slf4j
public class RejectOutdatedAlertMessagesService implements RejectOutdatedAlertMessagesUseCase {

  private final AlertMessageProperties alertMessageProperties;

  private final AlertMessageStatusRepository repository;
  private final ResponseGeneratorService responseGeneratorService;
  private final AlertMessageStatusService alertMessageStatusService;

  @Setter
  private Clock clock = Clock.systemUTC();

  @Transactional
  @Override
  public boolean process(int chunkSize) {
    var outdatedAlerts = repository.findOutdated(chunkSize, decisionObsoleteSince());
    outdatedAlerts.forEach(this::sendResponse);
    // tradeoff: case when size == chunkSize triggers an additional db hit.
    return outdatedAlerts.size() <= chunkSize;
  }

  private OffsetDateTime decisionObsoleteSince() {
    return OffsetDateTime.now(clock).minus(
      alertMessageProperties.getDecisionRequestedTime().minusSeconds(1));
  }

  private void sendResponse(AlertMessageStatusEntity alertMessageStatus) {
    var alertMessageId = alertMessageStatus.getAlertMessageId();
    responseGeneratorService.prepareAndSendResponse(alertMessageId, REJECTED_OUTDATED);
    alertMessageStatusService
        .transitionAlertMessageStatus(alertMessageId, REJECTED_OUTDATED);
  }

}

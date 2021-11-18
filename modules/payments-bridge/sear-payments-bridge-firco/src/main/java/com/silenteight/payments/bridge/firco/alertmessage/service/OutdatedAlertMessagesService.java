package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.OutdatedAlertMessagesUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.RecommendationCompletedPublisherPort;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Component
@Slf4j
class OutdatedAlertMessagesService implements OutdatedAlertMessagesUseCase {

  private final AlertMessageProperties alertMessageProperties;
  private final AlertMessageStatusRepository repository;
  private final RecommendationCompletedPublisherPort recommendationCompletedPublisherPort;

  @Setter
  private Clock clock = Clock.systemUTC();

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
    var alertId = status.getAlertMessageId();
    recommendationCompletedPublisherPort.send(RecommendationCompletedEvent.fromBridge(alertId,
            AlertMessageStatus.REJECTED_OUTDATED.name(),
            RecommendationReason.OUTDATED.name()));
  }

}

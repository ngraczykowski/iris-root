package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.SingleAlertFetcher;
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.SingleAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.State;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.sep.base.common.messaging.properties.MessageIdAndPriorityProvider;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;

@RequiredArgsConstructor
@Slf4j
class RecommendationOrderHandler {

  public static final int ALERT_ORDER_PRIORITY = 10;

  private static final String TIMEOUT_EXCEPTION =
      "Recommendation for {} has not been provided within {} seconds";

  private final SingleAlertFetcher alertFetcher;
  private final SingleAlertIngestService ingestService;
  private final Duration timeout;

  RecommendationDto orderAndReceiveRecommendation(String systemId) {
    Alert alert = fetchAlert(systemId);
    verifyAlert(alert);
    Recommendation recommendation = getRecommendation(alert);

    return RecommendationDto.builder()
        .externalId(systemId)
        .discriminator(alert.id().discriminator())
        .date(toOffsetDateTime(recommendation.getCreatedAtOrBuilder()))
        .decision(recommendation.getAction().toString())
        .comment(recommendation.getComment())
        .build();
  }

  private Recommendation getRecommendation(Alert alert) {
    MessagePropertiesProvider messageProvider = getMessageProvider();
    Optional<Recommendation> recommendation =
        ingestService.ingestAlertAndTryToReceiveRecommendation(alert, messageProvider);

    if (recommendation.isEmpty()) {
      log.error(TIMEOUT_EXCEPTION, alert.id().sourceId(), timeout.getSeconds());
      throw new RecommendationTimeoutException();
    }

    return recommendation.get();
  }

  private MessagePropertiesProvider getMessageProvider() {
    return new MessageIdAndPriorityProvider(
        UUID.randomUUID().toString(), ALERT_ORDER_PRIORITY, timeout);
  }

  private Alert fetchAlert(String systemId) {
    return alertFetcher.fetch(systemId).orElseThrow(AlertNotFoundException::new);
  }

  private static void verifyAlert(Alert alert) {
    String alertId = alert.id().sourceId();

    if (isDamagedAlert(alert)) {
      log.error("Alert: {} is damaged, recommendation will not be provided", alertId);
      throw new AlertDamagedException();
    } else if (isSolvedAlert(alert)) {
      log.error("Alert: {} is already solved, recommendation will not be provided", alertId);
      throw new AlertAlreadySolvedException();
    }
  }

  private static boolean isDamagedAlert(Alert alert) {
    return alert.state() != State.STATE_CORRECT;
  }

  private static boolean isSolvedAlert(Alert alert) {
    return alert.matches().stream()
        .noneMatch(Match::isNew);
  }
}

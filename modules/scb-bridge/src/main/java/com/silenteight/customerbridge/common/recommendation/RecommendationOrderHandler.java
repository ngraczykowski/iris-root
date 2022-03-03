package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.batch.SingleAlertFetcher;
import com.silenteight.customerbridge.common.ingest.SingleAlertIngestService;
import com.silenteight.customerbridge.common.protocol.MatchWrapper;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.State;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.sep.base.common.messaging.properties.MessageIdAndPriorityProvider;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ALERT_ORDER_PRIORITY;
import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;

@RequiredArgsConstructor
@Slf4j
class RecommendationOrderHandler {

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
        .discriminator(alert.getId().getDiscriminator())
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
      log.error(TIMEOUT_EXCEPTION, alert.getId().getSourceId(), timeout.getSeconds());
      throw new RecommendationTimeoutException();
    }

    return recommendation.get();
  }

  private Alert fetchAlert(String systemId) {
    return alertFetcher.fetch(systemId).orElseThrow(AlertNotFoundException::new);
  }

  private MessagePropertiesProvider getMessageProvider() {
    return new MessageIdAndPriorityProvider(
        UUID.randomUUID().toString(), ALERT_ORDER_PRIORITY, timeout);
  }

  private static void verifyAlert(Alert alert) {
    String alertId = alert.getId().getSourceId();

    if (isDamagedAlert(alert)) {
      log.error("Alert: {} is damaged, recommendation will not be provided", alertId);
      throw new AlertDamagedException();
    } else if (isSolvedAlert(alert)) {
      log.error("Alert: {} is already solved, recommendation will not be provided", alertId);
      throw new AlertAlreadySolvedException();
    }
  }

  private static boolean isDamagedAlert(Alert alert) {
    return alert.getState() != State.STATE_CORRECT;
  }

  private static boolean isSolvedAlert(Alert alert) {
    return alert.getMatchesList().stream()
        .map(MatchWrapper::new)
        .noneMatch(MatchWrapper::isNew);
  }
}

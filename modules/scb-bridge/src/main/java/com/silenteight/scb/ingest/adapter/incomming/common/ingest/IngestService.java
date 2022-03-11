package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.common.messaging.MessageSender;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;

@Slf4j
@Builder
class IngestService implements SingleAlertIngestService, BatchAlertIngestService {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();
  private final MessageSender sender;
  private final Collection<IngestServiceListener> listeners;
  private final boolean solvedAlertsProcessingEnabled;
  private final ScbRecommendationService scbRecommendationService;
  @Getter
  private long ingestedLearningAlertsCounter;

  @Override
  public void ingestAlertsForLearn(@NonNull Stream<Alert> alerts) {
    alerts.forEach(a -> {
      var flags = determineLearningFlags(a);
      send(a, flags);
      ingestedLearningAlertsCounter++;
    });
  }

  @Override
  public void ingestAlertsForRecommendation(@NonNull Stream<Alert> alerts) {
    filterAlerts(alerts).forEach(a -> send(a, ALERT_RECOMMENDATION_FLAGS));
  }

  private Stream<Alert> filterAlerts(Stream<Alert> alerts) {
    return solvedAlertsProcessingEnabled ? alerts : alerts.filter(IngestService::hasNotBeenSolved);
  }

  private static boolean hasNotBeenSolved(Alert alert) {
    return alert.matches().stream()
        .anyMatch(Match::isNew);
  }

  @LogContext
  private void send(Alert alert, int flags) {
    logAlert(alert.id().sourceId(), alert.id().discriminator());

    Alert ingestedAlert = updateIngestInfoForAlert(alert, flags);
    listeners.forEach(l -> l.send(ingestedAlert));

    log.info("Sending a batched alert, systemId={}", alert.id().sourceId());
  }

  @NotNull
  private static Alert updateIngestInfoForAlert(Alert alert, int flags) {
    Instant ingestedAt = Instant.now();

    if (log.isTraceEnabled())
      log.trace("Updating alert with ingest data: ingestedAt={}", ingestedAt);

    return alert
        .toBuilder()
        .flags(flags)
        .ingestedAt(ingestedAt)
        .build();
  }

  private int determineLearningFlags(Alert alert) {
    var flags = Flag.LEARN.getValue();

    if (shouldLearningAlertBeProcessed(alert)) {
      flags |= Flag.PROCESS.getValue();
    }

    return flags;
  }

  private boolean shouldLearningAlertBeProcessed(Alert alert) {
    return !scbRecommendationService.alertRecommendationExists(
        alert.id().sourceId(),
        alert.id().discriminator());
  }

  @Override
  public void ingestOrderedAlert(Alert alert, MessagePropertiesProvider propertiesProvider) {
    Alert ingestedAlert = updateAlertAndHandleListeners(alert);

    log.info("Sending an ordered alert: systemId={}", alert.id().sourceId());
  }

  @Override
  public Optional<Recommendation> ingestAlertAndTryToReceiveRecommendation(
      Alert alert, MessagePropertiesProvider propertiesProvider) {
    Alert ingestedAlert = updateAlertAndHandleListeners(alert);

    log.info(
        "Sending an ordered alert: systemId={} and waiting for recommendation",
        alert.id().sourceId());

    return Optional.empty();
  }

  @Nonnull
  private Alert updateAlertAndHandleListeners(Alert alert) {
    Alert ingestedAlert = updateIngestInfoForAlert(alert, ALERT_RECOMMENDATION_FLAGS);

    listeners.forEach(l -> l.send(ingestedAlert));
    return ingestedAlert;
  }
}

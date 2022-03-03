package com.silenteight.customerbridge.common.ingest;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.protocol.AlertWrapper;
import com.silenteight.customerbridge.common.protocol.MatchWrapper;
import com.silenteight.customerbridge.common.recommendation.ScbRecommendationService;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.Flags;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.common.messaging.MessageSender;
import com.silenteight.sep.base.common.messaging.properties.MessagePriorityProvider;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import com.google.protobuf.Timestamp;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ALERT_DENY_PRIORITY;
import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ALERT_NON_DENY_PRIORITY;
import static com.silenteight.proto.serp.v1.alert.Alert.Flags.FLAG_LEARN_VALUE;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;

@Slf4j
@Builder
class IngestService implements SingleAlertIngestService, BatchAlertIngestService {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flags.FLAG_RECOMMEND_VALUE | Flags.FLAG_PROCESS_VALUE | Flags.FLAG_ATTACH_VALUE;
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
    return alert.getMatchesList().stream()
        .map(MatchWrapper::new)
        .anyMatch(MatchWrapper::isNew);
  }

  @LogContext
  private void send(Alert alert, int flags) {
    logAlert(alert.getId().getSourceId(), alert.getId().getDiscriminator());

    Alert ingestedAlert = updateIngestInfoForAlert(alert, flags);
    listeners.forEach(l -> l.send(ingestedAlert));

    log.info("Sending a batched alert, systemId={}", alert.getId().getSourceId());

    sendIngestedAlert(ingestedAlert, flags);
  }

  private void sendIngestedAlert(Alert alert, int flags) {
    if (isLearningFlagEnabled(flags))
      sender.send(alert);
    else if (isDenyAlert(alert))
      sender.send(alert, new MessagePriorityProvider(ALERT_DENY_PRIORITY));
    else
      sender.send(alert, new MessagePriorityProvider(ALERT_NON_DENY_PRIORITY));
  }

  private static boolean isLearningFlagEnabled(int flags) {
    return (flags & FLAG_LEARN_VALUE) != 0;
  }

  private static boolean isDenyAlert(Alert alert) {
    AlertWrapper alertWrapper = new AlertWrapper(alert);
    Optional<ScbAlertDetails> details = alertWrapper.unpackDetails(ScbAlertDetails.class);

    return details
        .map(d -> isDeny(d.getUnit()))
        .orElse(Boolean.FALSE);
  }

  private static boolean isDeny(@NonNull String unit) {
    return unit.contains("DENY");
  }

  @NotNull
  private static Alert updateIngestInfoForAlert(Alert alert, int flags) {
    Timestamp ingestedAt = toTimestamp(Instant.now());

    if (log.isTraceEnabled())
      log.trace("Updating alert with ingest data: ingestedAt={}", ingestedAt);

    return alert
        .toBuilder()
        .setFlags(flags)
        .setIngestedAt(ingestedAt)
        .build();
  }

  private int determineLearningFlags(Alert alert) {
    var flags = Flags.FLAG_LEARN_VALUE;

    if (shouldLearningAlertBeProcessed(alert)) {
      flags |= Flags.FLAG_PROCESS_VALUE;
    }

    return flags;
  }

  private boolean shouldLearningAlertBeProcessed(Alert alert) {
    return !scbRecommendationService.alertRecommendationExists(
        alert.getId().getSourceId(),
        alert.getId().getDiscriminator());
  }

  @Override
  public void ingestOrderedAlert(Alert alert, MessagePropertiesProvider propertiesProvider) {
    Alert ingestedAlert = updateAlertAndHandleListeners(alert);

    log.info("Sending an ordered alert: systemId={}", alert.getId().getSourceId());

    sender.send(ingestedAlert, propertiesProvider);
  }

  @Override
  public Optional<Recommendation> ingestAlertAndTryToReceiveRecommendation(
      Alert alert, MessagePropertiesProvider propertiesProvider) {
    Alert ingestedAlert = updateAlertAndHandleListeners(alert);

    log.info(
        "Sending an ordered alert: systemId={} and waiting for recommendation",
        alert.getId().getSourceId());

    return sender.sendAndReceive(ingestedAlert, propertiesProvider)
        .map(m -> (Recommendation) m);
  }

  @Nonnull
  private Alert updateAlertAndHandleListeners(Alert alert) {
    Alert ingestedAlert = updateIngestInfoForAlert(alert, ALERT_RECOMMENDATION_FLAGS);

    listeners.forEach(l -> l.send(ingestedAlert));
    return ingestedAlert;
  }
}

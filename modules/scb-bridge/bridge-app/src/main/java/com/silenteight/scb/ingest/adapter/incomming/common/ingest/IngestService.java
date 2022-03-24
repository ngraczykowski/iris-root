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
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.common.messaging.MessageSender;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Builder
class IngestService implements SingleAlertIngestService, BatchAlertIngestService {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();
  private final MessageSender sender;
  private final Collection<IngestServiceListener> listeners;
  private final boolean solvedAlertsProcessingEnabled;
  private final ScbRecommendationService scbRecommendationService;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;
  @Getter
  private long ingestedLearningAlertsCounter;

  @Override
  public void ingestAlertsForLearn(@NonNull Stream<Alert> alertStream) {
    alertStream.collect(groupingBy(alert -> alert.details().getBatchId()))
        .forEach((batchId, alerts) -> {
          var registrationResponse = alertRegistrationFacade.registerLearningAlert(batchId, alerts);
          alerts.forEach(alert -> {
            var flags = determineLearningFlags(alert);
            publish(alert, flags, registrationResponse);
            ingestedLearningAlertsCounter++;
          });
        });
  }

  @Override
  public void ingestAlertsForRecommendation(
      @NonNull String internalBatchId, @NonNull List<Alert> alerts) {
    var registrationResponse =
        alertRegistrationFacade.registerSolvingAlert(internalBatchId, alerts, Priority.MEDIUM);
    alerts.forEach(alert -> publish(alert, ALERT_RECOMMENDATION_FLAGS, registrationResponse));
  }

  @LogContext
  private void publish(Alert alert, int flags, RegistrationResponse registrationResponse) {
    logAlert(alert.id().sourceId(), alert.id().discriminator());

    Alert ingestedAlert = updateIngestInfoForAlert(alert, flags);
    updateRegistrationInfoForAlert(ingestedAlert, registrationResponse);

    log.info("Publishing a batched alert, systemId={}", ingestedAlert.id().sourceId());
    ingestEventPublisher.publish(ingestedAlert);
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

  private static void updateRegistrationInfoForAlert(
      Alert alert, RegistrationResponse registrationResponse) {
    registrationResponse.getRegisteredAlertWithMatches().stream()
        .peek(registeredAlertWithMatches -> alert
            .details()
            .setAlertName(registeredAlertWithMatches.getAlertName()))
        .map(RegisteredAlertWithMatches::getRegisteredMatches)
        .flatMap(Collection::stream)
        .forEach(
            registeredMatch -> updateRegistrationInfoForMatch(alert.matches(), registeredMatch));
  }

  private static void updateRegistrationInfoForMatch(
      List<Match> matches, RegistrationResponse.RegisteredMatch registeredMatch) {
    matches.stream()
        .filter(match -> match.id().sourceId().equals(registeredMatch.getMatchId()))
        .findFirst()
        .ifPresent(match -> match.details().setMatchName(registeredMatch.getMatchName()));
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

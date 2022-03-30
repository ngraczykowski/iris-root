package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUpdater;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;

@Slf4j
@Builder
class IngestService implements SingleAlertIngestService, BatchAlertIngestService {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();
  private final Collection<IngestServiceListener> listeners;
  private final ScbRecommendationService scbRecommendationService;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;
  @Getter
  private long ingestedLearningAlertsCounter;

  @Override
  public void ingestAlertsForLearn(@NonNull String internalBatchId, @NonNull List<Alert> alerts) {
    var registrationResponse =
        alertRegistrationFacade.registerLearningAlerts(internalBatchId, alerts);
    alerts.forEach(alert -> {
      var flags = determineLearningFlags(alert);
      publish(alert, flags, registrationResponse);
      ingestedLearningAlertsCounter++;
    });
  }

  @Override
  public void ingestAlertsForRecommendation(
      @NonNull String internalBatchId,
      @NonNull List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {
    var registrationResponse =
        alertRegistrationFacade
            .registerSolvingAlerts(internalBatchId, alerts, registrationBatchContext);
    alerts.forEach(alert -> publish(alert, ALERT_RECOMMENDATION_FLAGS, registrationResponse));
  }

  @LogContext
  private void publish(Alert alert, int flags, RegistrationResponse registrationResponse) {
    logAlert(alert.id().sourceId(), alert.id().discriminator());

    Alert ingestedAlert = updateIngestInfoForAlert(alert, flags);
    AlertUpdater.updatedWithRegistrationInfo(ingestedAlert, registrationResponse);

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

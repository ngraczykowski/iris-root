package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUpdater;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;

@Slf4j
@Builder
class IngestService implements BatchAlertIngestService {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();
  private final ScbRecommendationService scbRecommendationService;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;
  @Getter
  private long ingestedLearningAlertsCounter;

  @Override
  public void ingestAlertsForLearn(@NonNull String internalBatchId, @NonNull List<Alert> alerts) {
    var registrationResponse =
        alertRegistrationFacade.registerLearningAlerts(internalBatchId, alerts);
    log.info("Publishing {} Alerts for Ingested for Learn for internalBatchId: {}", alerts.size(),
        internalBatchId);
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
    log.info("Publishing {} Alerts for Ingested for Recommendation for internalBatchId: {}",
        alerts.size(), internalBatchId);
    alerts.forEach(alert -> publish(alert, ALERT_RECOMMENDATION_FLAGS, registrationResponse));
  }

  @LogContext
  private void publish(Alert alert, int flags, RegistrationResponse registrationResponse) {
    logAlert(alert.id().sourceId(), alert.id().discriminator());

    Alert ingestedAlert = updateIngestInfoForAlert(alert, flags);
    AlertUpdater.updatedWithRegistrationInfo(ingestedAlert, registrationResponse);

    ingestEventPublisher.publish(ingestedAlert);
  }

  @NotNull
  private static Alert updateIngestInfoForAlert(Alert alert, int flags) {
    Instant ingestedAt = Instant.now();

    if (log.isTraceEnabled())
      log.trace("Updating {} with ingest data: ingestedAt: {}", alert.logInfo(), ingestedAt);

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
        alert.details().getSystemId(),
        alert.id().discriminator());
  }

}

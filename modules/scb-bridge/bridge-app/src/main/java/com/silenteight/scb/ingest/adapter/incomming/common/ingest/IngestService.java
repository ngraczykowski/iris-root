package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUpdater;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.scb.reports.domain.model.ReportMapper;
import com.silenteight.scb.reports.domain.port.outgoing.ReportsSenderService;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.LOW;
import static com.silenteight.scb.ingest.domain.model.BatchSource.LEARNING;
import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;

@Slf4j
@Builder
class IngestService implements BatchAlertIngestService {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();

  private static final int ALERT_LEARNING_FLAGS =
      Flag.LEARN.getValue() | Flag.PROCESS.getValue();

  private final ScbRecommendationService scbRecommendationService;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;
  private final ReportsSenderService reportsSenderService;

  @Getter
  private long ingestedLearningAlertsCounter;

  @Override
  public void ingestAlertsForLearn(@NonNull String internalBatchId, @NonNull List<Alert> alerts) {
    log.info("Ingesting {} learning alerts", alerts.size());

    var filteredAlerts = new FilteredAlerts(alerts, this::hasRecommendation);

    registerAndPublishLearningAlerts(
        internalBatchId,
        filteredAlerts.alertsWithoutDecisions(),
        filteredAlerts.alertsWithDecisionAndWithoutRecommendation());

    updateAlertNameFromRecommendation(filteredAlerts.alertsWithDecisionAndWithRecommendation());
    sendReportsToWarehouse(filteredAlerts.alertsWithDecisions());
  }

  private void registerAndPublishLearningAlerts(
      String internalBatchId,
      List<Alert> alertsWithoutDecisions,
      List<Alert> alertsWithDecisionAndWithoutRecommendation) {

    if (alertsWithoutDecisions.isEmpty() && alertsWithDecisionAndWithoutRecommendation.isEmpty()) {
      log.info(
          "No learning alerts to register for internalBatchId: {} - skipping", internalBatchId);
      return;
    }

    log.info("Registering {} learning alerts without decision and "
            + "{} learning alerts with decision and without recommendation",
        alertsWithoutDecisions.size(), alertsWithDecisionAndWithoutRecommendation.size());

    registerAndPublishLearningAlerts(internalBatchId, ListUtils.union(
        alertsWithoutDecisions,
        alertsWithDecisionAndWithoutRecommendation));
  }

  private void registerAndPublishLearningAlerts(String internalBatchId, List<Alert> alerts) {
    var batchContext = new RegistrationBatchContext(LOW, LEARNING);
    registerAndPublish(internalBatchId, alerts, ALERT_LEARNING_FLAGS, batchContext);
    ingestedLearningAlertsCounter += alerts.size();
  }

  private void updateAlertNameFromRecommendation(List<Alert> alerts) {
    // For Alerts which have just been sent to core-bridge, we already have AlertName populated as
    // it is sent in the Registration Response.

    // For Alerts which have not been sent to core-bridge (because they have already been
    // registered) we take AlertName from Recommendation.
    alerts.forEach(alert -> alert.details().setAlertName(recommendation(alert).requireAlertName()));
  }

  private void sendReportsToWarehouse(List<Alert> alerts) {
    reportsSenderService.send(ReportMapper.toReports(alerts));
  }

  @Override
  public void ingestAlertsForRecommendation(
      @NonNull String internalBatchId,
      @NonNull List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {
    registerAndPublish(internalBatchId, alerts, ALERT_RECOMMENDATION_FLAGS,
        registrationBatchContext);
  }

  private void registerAndPublish(
      String internalBatchId,
      List<Alert> alerts,
      int flags,
      RegistrationBatchContext batchContext) {

    log.info("Registering {} alerts for {} for internalBatchId: {}",
        alerts.size(), batchContext, internalBatchId);

    var registrationResponse =
        alertRegistrationFacade.registerAlerts(internalBatchId, alerts, batchContext);

    AlertUpdater.updateWithRegistrationResponse(alerts, registrationResponse);

    log.info("Publishing {} alerts for {} for internalBatchId: {}",
        alerts.size(), batchContext, internalBatchId);
    alerts.forEach(alert -> publish(alert, flags));
  }

  @LogContext
  private void publish(Alert alert, int flags) {
    logAlert(alert.id().sourceId(), alert.id().discriminator());

    Alert ingestedAlert = updateIngestInfoForAlert(alert, flags);

    ingestEventPublisher.publish(ingestedAlert);
  }

  @NotNull
  private static Alert updateIngestInfoForAlert(Alert alert, int flags) {
    Instant ingestedAt = Instant.now();

    if (log.isTraceEnabled())
      log.trace("Updating {} with: ingestedAt: {}, flags: {}", alert.logInfo(), ingestedAt, flags);

    return alert
        .toBuilder()
        .flags(flags)
        .ingestedAt(ingestedAt)
        .build();
  }

  private ScbRecommendation recommendation(Alert alert) {
    return maybeRecommendation(alert)
        .orElseThrow(() -> new IllegalStateException(
            "Recommendation for " + alert.logInfo() + " does not exist"));
  }

  private boolean hasRecommendation(Alert alert) {
    return maybeRecommendation(alert).isPresent();
  }

  private Optional<ScbRecommendation> maybeRecommendation(Alert alert) {
    return scbRecommendationService.alertRecommendation(
        alert.details().getSystemId(),
        alert.id().discriminator());
  }

}

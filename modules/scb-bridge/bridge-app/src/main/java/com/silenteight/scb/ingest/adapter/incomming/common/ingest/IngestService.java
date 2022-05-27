package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUpdater;
import com.silenteight.scb.ingest.domain.IngestFacade;
import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.reports.domain.model.ReportMapper;
import com.silenteight.scb.reports.domain.port.outgoing.ReportsSenderService;

import org.apache.commons.collections4.ListUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Builder
class IngestService implements BatchAlertIngestService {

  private final ScbRecommendationService scbRecommendationService;
  private final IngestFacade ingestFacade;
  private final UdsFeedingPublisher udsFeedingPublisher;
  private final ReportsSenderService reportsSenderService;
  private final TrafficManager trafficManager;
  private final BatchInfoService batchInfoService;

  @Getter
  private long ingestedLearningAlertsCounter;

  @Override
  public void ingestAlertsForLearn(@NonNull String internalBatchId, @NonNull List<Alert> alerts) {
    log.info("Ingesting {} learning alerts", alerts.size());

    var recommendations = scbRecommendationService.getRecommendations(getSystemIds(alerts));

    var filteredAlerts = new FilteredAlerts(alerts, hasRecommendation(recommendations));
    updateAlertNameFromRecommendation(
        filteredAlerts.alertsWithDecisionAndWithRecommendation(), recommendations);

    if (trafficManager.holdPeriodicAlertProcessing()) {
      sendToWarehouse(filteredAlerts.alertsWithDecisionAndWithRecommendation());
    } else {
      registerAndPublishLearningAlerts(internalBatchId, filteredAlerts);
    }
  }

  private List<String> getSystemIds(@NonNull List<Alert> alerts) {
    return alerts.stream()
        .map(alert -> alert.details().getSystemId())
        .toList();
  }

  @Override
  public IngestedAlertsStatus ingestAlertsForRecommendation(
      @NonNull String internalBatchId,
      @NonNull List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {
    return registerAndPublish(internalBatchId, alerts, registrationBatchContext);
  }

  private void registerAndPublishLearningAlerts(
      String internalBatchId, FilteredAlerts filteredAlerts) {
    List<Alert> alertsToRegister = ListUtils.union(
        filteredAlerts.alertsWithoutDecisions(),
        filteredAlerts.alertsWithDecisionAndWithoutRecommendation());

    if (alertsToRegister.isEmpty()) {
      sendToWarehouse(filteredAlerts.alertsWithDecisionAndWithRecommendation());
      log.info(
          "No learning alerts to register for internalBatchId: {} - skipping", internalBatchId);
      return;
    }

    log.info("Registering {} learning alerts without decision "
        + "and with decision and without recommendation", alertsToRegister.size());

    batchInfoService.store(internalBatchId, BatchSource.LEARNING, alertsToRegister.size());
    registerAndPublishLearningAlertsInCore(internalBatchId, alertsToRegister);
    sendToWarehouse(filteredAlerts.alertsWithDecisions());
  }

  private void registerAndPublishLearningAlertsInCore(String internalBatchId, List<Alert> alerts) {
    registerAndPublish(internalBatchId, alerts, RegistrationBatchContext.LEARNING_CONTEXT);
    ingestedLearningAlertsCounter += alerts.size();
  }

  private IngestedAlertsStatus registerAndPublish(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext batchContext) {
    var registrationResponse =
        ingestFacade.registerAlerts(internalBatchId, alerts, batchContext);

    AlertUpdater.updateWithRegistrationResponse(alerts, registrationResponse);

    return udsFeedingPublisher.publishToUds(internalBatchId, alerts, batchContext);
  }

  private void sendToWarehouse(List<Alert> alerts) {
    log.info("Sending to Warehouse {} alerts", alerts.size());
    sendReportsToWarehouse(alerts);
  }

  private void sendReportsToWarehouse(List<Alert> alerts) {
    reportsSenderService.send(ReportMapper.toReports(alerts));
  }

  private void updateAlertNameFromRecommendation(
      List<Alert> alerts, List<ScbRecommendation> recommendations) {
    // For Alerts which have just been sent to core-bridge, we already have AlertName populated as
    // it is sent in the Registration Response.

    // For Alerts which have not been sent to core-bridge (because they have already been
    // registered) we take AlertName from Recommendation.
    alerts.forEach(alert -> alert
        .details()
        .setAlertName(recommendation(alert, recommendations).requireAlertName()));
  }

  private ScbRecommendation recommendation(Alert alert, List<ScbRecommendation> recommendations) {
    return maybeRecommendation(alert, recommendations)
        .orElseThrow(() -> new IllegalStateException(
            "Recommendation for " + alert.logInfo() + " does not exist"));
  }

  private Predicate<Alert> hasRecommendation(List<ScbRecommendation> recommendations) {
    return alert -> maybeRecommendation(alert, recommendations).isPresent();
  }

  private Optional<ScbRecommendation> maybeRecommendation(
      Alert alert, List<ScbRecommendation> recommendations) {
    return scbRecommendationService.getRecommendation(
        alert.details().getSystemId(), alert.id().discriminator(), recommendations);
  }
}

/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.IngestedAlertsStatus;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;

import org.apache.commons.lang3.time.StopWatch;

import java.util.List;

import static com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext.CBS_CONTEXT;

@Slf4j
@Builder
class AlertHandler {

  private final AlertInFlightService alertInFlightService;
  private final CbsAckGateway cbsAckGateway;
  private final AlertMapper alertMapper;
  private final BatchAlertIngestService ingestService;
  private final RawAlertService rawAlertService;

  void handleAlerts(
      String internalBatchId,
      ScbAlertIdContext context,
      AlertCompositeCollection alertCompositeCollection) {
    handleValidAlerts(internalBatchId, context, alertCompositeCollection.getValidAlerts());
    handleInvalidAlerts(context, alertCompositeCollection.getInvalidAlerts());
  }

  private void handleValidAlerts(
      String internalBatchId,
      ScbAlertIdContext context,
      List<ValidAlertComposite> validAlertComposites) {
    var stopWatch = StopWatch.createStarted();
    var alerts = alertMapper.fromValidAlertComposites(validAlertComposites);
    if (alerts.isEmpty()) {
      log.info("No valid alerts to handle");
      return;
    }

    persistAlerts(internalBatchId, alerts);
    log.info("Alerts have been persisted for internalBatchId: {} executed in: {}",
        internalBatchId, stopWatch);

    stopWatch = StopWatch.createStarted();
    var ingested = registerAlerts(internalBatchId, alerts);
    log.info("Alerts have been registered for internalBatchId: {} executed in: {}",
        internalBatchId, stopWatch);

    stopWatch = StopWatch.createStarted();
    acknowledgeAlerts(context, ingested.success());
    // TODO: what we do with ingested.failed() as for those won't have recommendations ?
    log.info("Alerts have been acknowledged for internalBatchId: {} executed in: {}",
        internalBatchId, stopWatch);
  }

  private void persistAlerts(String internalBatchId, List<Alert> alerts) {
    rawAlertService.store(internalBatchId, alerts);
  }

  private IngestedAlertsStatus registerAlerts(String internalBatchId, List<Alert> alerts) {
    return ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, CBS_CONTEXT);
  }

  private void acknowledgeAlerts(ScbAlertIdContext context, List<Alert> alerts) {
    alerts.forEach(alert -> {
      var alertId = new AlertId(
          alert.details().getSystemId(),
          alert.details().getBatchId());

      var cbsOutput = ackAlert(
          alertId.getSystemId(),
          alertId.getBatchId(),
          context.getWatchlistLevel());

      switch (cbsOutput.getState()) {
        case OK -> alertInFlightService.ack(alertId);
        case TEMPORARY_FAILURE -> log.error(
            "Temporary failure on ACK for alert={}, will retry again.", alertId);
        default -> alertInFlightService.error(alertId, "Fatal error on ACK");
      }
    });
  }

  private CbsOutput ackAlert(String systemId, String batchId, boolean watchlistLevel) {
    return cbsAckGateway.ackReadAlert(
        CbsAckAlert.builder()
            .alertExternalId(systemId)
            .batchId(batchId)
            .watchlistLevel(watchlistLevel)
            .build());
  }

  private void handleInvalidAlerts(ScbAlertIdContext contex, List<InvalidAlert> invalidAlerts) {
    invalidAlerts.forEach(alert -> {
      if (alert.hasReasonCausedByFatalError()) {
        log.warn("Fatal error occurred on alert={}, marked with ERROR state.", alert.getAlertId());
        ackAlert(alert.getSystemId(), alert.getBatchId(), contex.getWatchlistLevel());
        alertInFlightService.error(alert.getAlertId(), alert.getReasonMessage());
      } else {
        log.warn("Temporary problem occurred on alert={}, will retry again.", alert.getAlertId());
      }
    });
  }
}

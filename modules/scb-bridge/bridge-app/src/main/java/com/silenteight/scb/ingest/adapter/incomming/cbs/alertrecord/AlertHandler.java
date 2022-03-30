package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;

import java.util.List;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.MEDIUM;
import static com.silenteight.scb.ingest.domain.model.BatchSource.CBS;

@RequiredArgsConstructor
@Slf4j
class AlertHandler {

  private final AlertInFlightService alertInFlightService;
  private final CbsAckGateway cbsAckGateway;
  private final ValidAlertCompositeMapper validAlertCompositeMapper;
  private final InvalidAlertMapper invalidAlertMapper;
  private final AlertMapper alertMapper;
  private final BatchAlertIngestService ingestService;

  void handleAlerts(
      String internalBatchId, List<AlertCompositeCollection> alertCompositeCollections) {
    var validAlertComposites =
        validAlertCompositeMapper.fromAlertCompositeCollections(alertCompositeCollections);
    var invalidAlerts =
        invalidAlertMapper.fromAlertCompositeCollections(alertCompositeCollections);
    handleValidAlerts(internalBatchId, validAlertComposites);
    handleInvalidAlerts(invalidAlerts);
  }

  private void handleValidAlerts(
      String internalBatchId, List<ValidAlertComposite> validAlertComposites) {
    registerAlerts(internalBatchId, validAlertComposites);
    acknowledgeAlerts(validAlertComposites);
  }

  private void registerAlerts(
      String internalBatchId, List<ValidAlertComposite> validAlertComposites) {
    var alerts = alertMapper.fromValidAlertComposites(validAlertComposites);
    var registrationBatchContext = new RegistrationBatchContext(MEDIUM, CBS);
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, registrationBatchContext);
  }

  private void acknowledgeAlerts(List<ValidAlertComposite> validAlertComposites) {
    validAlertComposites.forEach(alertComposite -> {
      var alertId = alertComposite.getAlertId();
      var cbsOutput = ackAlert(
          alertId.getSystemId(),
          alertId.getBatchId(),
          alertComposite.getContext().getWatchlistLevel());

      switch (cbsOutput.getState()) {
        case OK -> alertInFlightService.delete(alertId);
        case TEMPORARY_FAILURE -> log.error(
            "Temporary failure on ACK for alert={}, will retry again.", alertId);
        default -> alertInFlightService.update(alertId, State.ERROR, "Fatal error on ACK");
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

  private void handleInvalidAlerts(List<InvalidAlert> invalidAlerts) {
    invalidAlerts.forEach(alert -> {
      if (alert.hasReasonCausedByFatalError()) {
        log.warn("Fatal error occurred on alert={}, marked with ERROR state.", alert.getAlertId());
        ackAlert(alert.getSystemId(), alert.getBatchId(), alert.getContext().getWatchlistLevel());
        alertInFlightService.update(
            alert.getAlertId(), AlertUnderProcessing.State.ERROR, alert.getReasonMessage());
      } else {
        log.warn("Temporary problem occurred on alert={}, will retry again.", alert.getAlertId());
      }
    });
  }
}
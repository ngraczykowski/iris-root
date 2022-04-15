package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;

import org.apache.commons.lang3.time.StopWatch;

import java.util.List;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.MEDIUM;
import static com.silenteight.scb.ingest.domain.model.BatchSource.CBS;

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
    persistAlerts(internalBatchId, validAlertComposites);
    log.info("Alerts have been persisted for internalBatchId: {} executed in: {}",
        internalBatchId, stopWatch);

    stopWatch = StopWatch.createStarted();
    registerAlerts(internalBatchId, validAlertComposites);
    log.info("Alerts have been registered for internalBatchId: {} executed in: {}",
        internalBatchId, stopWatch);

    stopWatch = StopWatch.createStarted();
    acknowledgeAlerts(context, validAlertComposites);
    log.info("Alerts have been acknowledged for internalBatchId: {} executed in: {}",
        internalBatchId, stopWatch);
  }

  private void registerAlerts(
      String internalBatchId, List<ValidAlertComposite> validAlertComposites) {
    var alerts = alertMapper.fromValidAlertComposites(validAlertComposites);
    var registrationBatchContext = new RegistrationBatchContext(MEDIUM, CBS);
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, registrationBatchContext);
  }

  private void acknowledgeAlerts(
      ScbAlertIdContext context, List<ValidAlertComposite> validAlertComposites) {
    validAlertComposites.forEach(alertComposite -> {
      var alertId = alertComposite.getAlertId();
      var cbsOutput = ackAlert(
          alertId.getSystemId(),
          alertId.getBatchId(),
          context.getWatchlistLevel());

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

  private void persistAlerts(
      String internalBatchId,
      List<ValidAlertComposite> validAlertComposites) {
    validAlertComposites.forEach(alertComposite ->
        rawAlertService.store(internalBatchId, alertComposite.getAlerts()));
  }

  private void handleInvalidAlerts(ScbAlertIdContext contex, List<InvalidAlert> invalidAlerts) {
    invalidAlerts.forEach(alert -> {
      if (alert.hasReasonCausedByFatalError()) {
        log.warn("Fatal error occurred on alert={}, marked with ERROR state.", alert.getAlertId());
        ackAlert(alert.getSystemId(), alert.getBatchId(), contex.getWatchlistLevel());
        alertInFlightService.update(
            alert.getAlertId(), AlertUnderProcessing.State.ERROR, alert.getReasonMessage());
      } else {
        log.warn("Temporary problem occurred on alert={}, will retry again.", alert.getAlertId());
      }
    });
  }
}

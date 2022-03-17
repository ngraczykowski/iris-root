package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class AlertHandler {

  private final AlertInFlightService alertInFlightService;
  private final CbsAckGateway cbsAckGateway;

  void handleAlerts(ScbAlertIdContext context, AlertCompositeCollection alertCompositeCollection) {
    handleValidAlerts(alertCompositeCollection.getValidAlerts(), context);
    handleInvalidAlerts(alertCompositeCollection.getInvalidAlerts(), context.getWatchlistLevel());
  }

  private void handleValidAlerts(List<ValidAlertComposite> validAlerts, ScbAlertIdContext context) {
    validAlerts.forEach(a -> {
      var alertId = a.getAlertId();
      var cbsOutput = ackAlert(
          alertId.getSystemId(), alertId.getBatchId(), context.getWatchlistLevel());

      switch (cbsOutput.getState()) {
        case OK:
          sendAlerts(a.getAlerts(), context.getPriority());
          alertInFlightService.delete(alertId);
          break;
        case TEMPORARY_FAILURE:
          log.error("Temporary failure on ACK for alert={}, will retry again.", alertId);
          break;
        default:
        case ERROR:
          alertInFlightService.update(
              alertId, AlertUnderProcessing.State.ERROR, "Fatal error on ACK");
      }
    });
  }

  private void sendAlerts(List<Alert> alerts, int priority) {
    alerts.forEach(a -> {

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

  private void handleInvalidAlerts(List<InvalidAlert> invalidAlerts, boolean watchlistLevel) {
    invalidAlerts.forEach(alert -> {
      if (alert.hasReasonCausedByFatalError()) {
        log.warn("Fatal error occurred on alert={}, marked with ERROR state.", alert.getAlertId());
        ackAlert(alert.getSystemId(), alert.getBatchId(), watchlistLevel);
        alertInFlightService.update(
            alert.getAlertId(), AlertUnderProcessing.State.ERROR, alert.getReasonMessage());
      } else {
        log.warn("Temporary problem occurred on alert={}, will retry again.", alert.getAlertId());
      }
    });
  }
}

package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.sep.base.common.messaging.MessageSender;
import com.silenteight.sep.base.common.messaging.properties.MessageIdAndPriorityProvider;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import java.util.List;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
@Slf4j
class AlertHandler {

  private final AlertInfoService alertInfoService;
  private final AlertInFlightService alertInFlightService;
  private final CbsAckGateway cbsAckGateway;
  private final MessageSender messageSender;

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
          alertInFlightService.update(alertId, State.ERROR, "Fatal error on ACK");
      }
    });
  }

  private void sendAlerts(List<Alert> alerts, int priority) {
    alerts.forEach(a -> {
      messageSender.send(a, getMessageProvider(priority));
      alertInfoService.sendAlertInfo(a);
    });
  }

  private void handleInvalidAlerts(List<InvalidAlert> invalidAlerts, boolean watchlistLevel) {
    invalidAlerts.forEach(alert -> {
      if (alert.hasReasonCausedByFatalError()) {
        log.warn("Fatal error occurred on alert={}, marked with ERROR state.", alert.getAlertId());
        ackAlert(alert.getSystemId(), alert.getBatchId(), watchlistLevel);
        alertInFlightService.update(alert.getAlertId(), State.ERROR, alert.getReasonMessage());
      } else {
        log.warn("Temporary problem occurred on alert={}, will retry again.", alert.getAlertId());
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

  private static MessagePropertiesProvider getMessageProvider(int priority) {
    return new MessageIdAndPriorityProvider(randomUUID().toString(), priority);
  }
}

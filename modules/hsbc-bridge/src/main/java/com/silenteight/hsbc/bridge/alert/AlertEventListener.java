package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent;
import com.silenteight.hsbc.bridge.alert.event.AlertsPreProcessingCompletedEvent;
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
class AlertEventListener {

  private final AlertProcessor alertProcessor;
  private final AlertUpdater updater;
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void onUpdateAlertEventWithNameEvent(UpdateAlertWithNameEvent event) {
    log.trace("Received updateAlertEventWithNameEvent, {}", event.getAlertIdToName());

    updater.updateNames(event.getAlertIdToName());
  }

  @EventListener
  public void onAlertRecommendationReadyEvent(AlertRecommendationReadyEvent event) {
    log.debug("Received alertRecommendationReadyEvent, alertName={}", event.getAlertName());

    updater.updateWithCompletedStatus(event.getAlertName());
  }

  @EventListener
  public void onBulkStoredEvent(BulkStoredEvent event) {
    log.info("NOMAD, bulk stored event handling started");

    var bulkId = event.getBulkId();

    alertProcessor.preProcessAlertsWithinBulk(bulkId);

    log.info("NOMAD, alerts preprocessing has been finished, bulkId=", bulkId);

    eventPublisher.publishEvent(new AlertsPreProcessingCompletedEvent(bulkId));

    log.info("NOMAD, bulk stored event has been handled");
  }
}

package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent;
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;

import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
class AlertEventListener {

  private final AlertUpdater updater;

  @EventListener
  public void onUpdateAlertEventWithNameEvent(UpdateAlertWithNameEvent event) {
    log.debug("Received updateAlertEventWithNameEvent, {}", event);

    updater.updateNames(event.getAlertIdToName());
  }

  @EventListener
  public void onAlertRecommendationReadyEvent(AlertRecommendationReadyEvent event) {
    log.debug("Received alertRecommendationReadyEvent, alertName={}", event.getAlertName());

    updater.updateWithCompletedStatus(event.getAlertName());
  }
}

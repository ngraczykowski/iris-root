package com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficManager {

  private final TimeSemaphore timeSemaphore;
  private final GnsRtSemaphoreProperties semaphoreProperties;

  public void activateRtSemaphore() {
    if (!semaphoreProperties.enabled()) {
      return;
    }
    timeSemaphore.activate(semaphoreProperties.getTimerSeconds());
    log.debug("Semaphore activated for {}s", semaphoreProperties.getTimerSeconds());
  }

  public boolean holdPeriodicAlertProcessing() {
    // TODO: add load management check - ALFA-207
    return timeSemaphore.isActive();
  }
}

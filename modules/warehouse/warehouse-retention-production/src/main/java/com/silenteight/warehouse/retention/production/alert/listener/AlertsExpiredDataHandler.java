package com.silenteight.warehouse.retention.production.alert.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.warehouse.retention.production.alert.EraseAlertsUseCase;

@Slf4j
@RequiredArgsConstructor
class AlertsExpiredDataHandler {

  @NonNull
  private final EraseAlertsUseCase eraseAlertUseCase;

  public void handle(AlertsExpired alertsExpired) {
    log.info("Received AlertsExpired command with {} alerts.", alertsExpired.getAlertsCount());
    eraseAlertUseCase.activate(alertsExpired.getAlertsList());
    log.debug("Processed AlertsExpired command with {} alerts.", alertsExpired.getAlertsCount());
  }
}

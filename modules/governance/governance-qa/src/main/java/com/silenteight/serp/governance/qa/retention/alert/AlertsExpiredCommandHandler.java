package com.silenteight.serp.governance.qa.retention.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;

@Slf4j
@RequiredArgsConstructor
class AlertsExpiredCommandHandler {

  @NonNull
  private final EraseAlertUseCase eraseAlertUseCase;

  public void handle(AlertsExpired command) {
    log.debug("Received AlertsExpired command with {} alerts.",
        command.getAlertsCount());
    eraseAlertUseCase.activate(command.getAlertsList());
    log.debug("Processed AlertsExpired command with {} alerts.",
        command.getAlertsCount());
  }
}

package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.SendNotificationUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.ReadAlertsUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadAlertsService implements ReadAlertsUseCase {

  private final LearningCsvReader learningCsvReader;
  private final SendNotificationUseCase sendNotificationUseCase;
  private final ProcessAlertUseCase processAlertUseCase;

  public void readAlerts() {
    var alertsRead = learningCsvReader.read(this::processAlert);
    sendNotificationUseCase.sendNotification(alertsRead.toNotificationRequest());
  }

  void processAlert(LearningAlert learningAlert) {
    processAlertUseCase.processAlert(learningAlert);
  }
}

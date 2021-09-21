package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.SendNotificationUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningDataUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class HandleLearningDataService implements HandleLearningDataUseCase {

  private final ReadAlertsUseCase readAlertsUseCase;
  private final SendNotificationUseCase sendNotificationUseCase;
  private final ProcessAlertUseCase processAlertUseCase;

  public void readAlerts(LearningRequest learningRequest) {
    var alertsRead = readAlertsUseCase.read(learningRequest, this::processAlert);
    sendNotificationUseCase.sendNotification(alertsRead.toNotificationRequest());
  }

  void processAlert(LearningAlert learningAlert) {
    processAlertUseCase.processAlert(learningAlert);
  }
}

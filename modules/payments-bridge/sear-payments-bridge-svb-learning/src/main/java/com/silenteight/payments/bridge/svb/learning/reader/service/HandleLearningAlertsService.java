package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.SendNotificationUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class HandleLearningAlertsService implements HandleLearningAlertsUseCase {

  private final ProcessAlertService processAlertService;
  private final SendNotificationUseCase sendNotificationUseCase;
  private final IngestService ingestService;

  public void readAlerts(LearningRequest learningRequest) {
    var alertsRead = processAlertService.read(learningRequest, this::registerAndIngest);
    sendNotificationUseCase.sendNotification(alertsRead.toNotificationRequest());
  }

  void registerAndIngest(LearningAlert learningAlert) {
    ingestService.ingest(learningAlert);
  }
}

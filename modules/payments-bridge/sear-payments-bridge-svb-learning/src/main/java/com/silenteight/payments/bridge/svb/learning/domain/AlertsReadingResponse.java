package com.silenteight.payments.bridge.svb.learning.domain;


import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class AlertsReadingResponse {

  int successfulAlerts;

  int failedAlerts;

  List<ReadAlertError> errors;

  String fileName;

  public LearningCsvNotificationRequest toLearningCsvNotificationRequest() {
    return LearningCsvNotificationRequest.builder()
        .fileName(fileName)
        .numberOfSuccessfulAlerts(successfulAlerts)
        .numberOfFailedAlerts(failedAlerts)
        .readAlertErrors(errors)
        .build();
  }
}

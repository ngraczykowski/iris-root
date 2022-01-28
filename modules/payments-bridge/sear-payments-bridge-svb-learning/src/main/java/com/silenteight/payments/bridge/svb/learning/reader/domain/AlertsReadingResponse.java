package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Builder
public class AlertsReadingResponse {

  int successfulAlerts;

  int failedAlerts;

  List<ReadAlertError> readAlertErrorList;

  @Setter
  private LearningCsv objectData;

  public LearningCsvNotificationRequest toLearningCsvNotificationRequest() {
    return LearningCsvNotificationRequest.builder()
        .fileName(objectData.getFileName())
        .numberOfSuccessfulAlerts(successfulAlerts)
        .numberOfFailedAlerts(failedAlerts)
        .fileLength(objectData.getFileLength())
        .build();
  }
}

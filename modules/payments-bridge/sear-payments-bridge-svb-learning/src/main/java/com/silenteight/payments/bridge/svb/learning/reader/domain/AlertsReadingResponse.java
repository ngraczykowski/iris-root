package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Setter;

import com.silenteight.payments.bridge.svb.learning.notification.model.NotificationRequest;

import java.util.List;

@Builder
public class AlertsReadingResponse {

  int successfulAlerts;

  int failedAlerts;

  List<ReadAlertError> readAlertErrorList;

  @Setter
  private LearningCsv objectData;

  public NotificationRequest toNotificationRequest() {
    return NotificationRequest
        .builder()
        .successfullyRecords(successfulAlerts)
        .failedRecord(failedAlerts)
        .errorLogs(readAlertErrorList)
        .fileName(objectData.getFileName())
        .fileLength(objectData.getFileLength())
        .hash(objectData.getHash())
        .build();
  }
}

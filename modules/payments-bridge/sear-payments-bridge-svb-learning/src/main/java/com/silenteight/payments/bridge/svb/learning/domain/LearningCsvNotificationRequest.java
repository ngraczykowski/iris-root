package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LearningCsvNotificationRequest {

  String fileName;
  int numberOfSuccessfulAlerts;
  int numberOfFailedAlerts;
  long fileLength;
  List<ReadAlertError> readAlertErrors;
}

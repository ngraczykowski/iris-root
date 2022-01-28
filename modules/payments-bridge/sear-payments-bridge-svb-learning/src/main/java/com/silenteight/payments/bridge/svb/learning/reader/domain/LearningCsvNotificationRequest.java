package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningCsvNotificationRequest {

  String fileName;
  int numberOfSuccessfulAlerts;
  int numberOfFailedAlerts;
  long fileLength;
}

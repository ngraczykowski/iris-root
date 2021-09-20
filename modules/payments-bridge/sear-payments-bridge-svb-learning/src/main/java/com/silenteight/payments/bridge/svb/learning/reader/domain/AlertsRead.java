package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.learning.notification.model.NotificationRequest;

@Value
@Builder
public class AlertsRead {

  int successfulAlerts;

  int failedAlerts;

  public NotificationRequest toNotificationRequest() {
    return NotificationRequest
        .builder()
        .successfullyRecords(successfulAlerts)
        .failedRecord(failedAlerts)
        .build();
  }
}

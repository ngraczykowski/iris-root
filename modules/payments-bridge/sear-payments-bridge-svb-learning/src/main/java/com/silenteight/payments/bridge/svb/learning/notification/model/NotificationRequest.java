package com.silenteight.payments.bridge.svb.learning.notification.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificationRequest {

  int successfullyRecords;

  int failedRecord;

  public String toBodyHtml() {
    return String.format("<!DOCTYPE html>\n"
            + "<html>\n"
            + "<body>\n"
            + "\n"
            + "<p>Successful Records count: %d</p>\n"
            + "<p>Failed Records count: %d</p>\n"
            + "\n"
            + "</body>\n"
            + "</html>\n",
        successfullyRecords, failedRecord);
  }

  public String toBodyText() {
    return String.format(
        "Successful Records count: %d \r\n"
        + "Failed Records count: %d",
        successfullyRecords, failedRecord);
  }
}

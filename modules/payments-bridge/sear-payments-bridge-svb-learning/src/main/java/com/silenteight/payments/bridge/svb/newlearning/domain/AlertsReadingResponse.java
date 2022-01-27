package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.notification.model.Notification;

import java.util.List;

@Builder
@Value
public class AlertsReadingResponse {

  int successfulAlerts;

  int failedAlerts;

  List<String> errors;

  String fileName;

  public Notification toNotification() {
    return Notification.builder()
        .type("CSV_PROCESSED")
        .message(getCsvAlertsProcessedMessage())
        .build();
  }

  private String getCsvAlertsProcessedMessage() {
    return String.format(
        "<html>\n"
            + "<body>\n"
            + "\n"
            + "<p>This is to confirm Silent Eight has received a "
            + "CSV file which has been processed by SEAR.</p>\n"
            + "<p>File name: %s</p>\n"
            + "<p>Number of successfully processed records: %d</p>\n"
            + "<p>Number of unprocessed records: %d</p>\n"
            + "<p></p>"
            + "<p>Thank you.</p>"
            + "<p>Silent Eight</p>"
            + "</body>\n"
            + "</html>\n", fileName, successfulAlerts, failedAlerts);
  }
}

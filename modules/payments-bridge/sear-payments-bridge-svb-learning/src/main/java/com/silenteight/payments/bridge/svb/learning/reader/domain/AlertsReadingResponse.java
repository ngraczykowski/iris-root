package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Setter;

import com.silenteight.payments.bridge.notification.model.Notification;

import java.util.List;

@Builder
public class AlertsReadingResponse {

  int successfulAlerts;

  int failedAlerts;

  List<ReadAlertError> readAlertErrorList;

  @Setter
  private LearningCsv objectData;

  public Notification toNotification() {
    return Notification.builder()
        .type("CSV_PROCESSED")
        .subject("Silent Eight - CMAPI - alert's processing errors")
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
            + "<p>File size: %d</p>\n"
            + "<p></p>"
            + "<p>Thank you.</p>"
            + "<p>Silent Eight</p>"
            + "</body>\n"
            + "</html>\n", objectData.getFileName(), successfulAlerts, failedAlerts,
        objectData.getFileLength());
  }
}

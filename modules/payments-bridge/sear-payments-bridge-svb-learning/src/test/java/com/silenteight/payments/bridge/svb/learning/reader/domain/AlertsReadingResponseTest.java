package com.silenteight.payments.bridge.svb.learning.reader.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertsReadingResponseTest {

  @Test
  void toNotification_checkIfMessageIsProper() {

    AlertsReadingResponse alertsReadingResponse = AlertsReadingResponse.builder()
        .successfulAlerts(1500)
        .failedAlerts(25)
        .objectData(LearningCsv.builder().fileName("06-06-2021.csv").fileLength(104345L).build())
        .build();

    String expectedMessage = "<html>\n"
        + "<body>\n"
        + "\n"
        + "<p>This is to confirm Silent Eight has received a "
        + "CSV file which has been processed by SEAR.</p>\n"
        + "<p>File name: 06-06-2021.csv</p>\n"
        + "<p>Number of successfully processed records: 1500</p>\n"
        + "<p>Number of unprocessed records: 25</p>\n"
        + "<p>File size: 104345</p>\n"
        + "<p></p>"
        + "<p>Thank you.</p>"
        + "<p>Silent Eight</p>"
        + "</body>\n"
        + "</html>\n";

    assertEquals(expectedMessage, alertsReadingResponse.toNotification().getMessage());
    assertEquals(
        "Silent Eight - CMAPI - alert's processing errors",
        alertsReadingResponse.toNotification().getSubject());
  }
}

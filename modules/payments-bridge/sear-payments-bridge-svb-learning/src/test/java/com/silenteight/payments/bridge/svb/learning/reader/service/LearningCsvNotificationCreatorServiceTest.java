package com.silenteight.payments.bridge.svb.learning.reader.service;


import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvNotificationRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.LearningCsvNotificationCreatorUseCase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LearningCsvNotificationCreatorService.class)
class LearningCsvNotificationCreatorServiceTest {

  @Autowired
  private LearningCsvNotificationCreatorUseCase learningCsvNotificationCreatorUseCase;

  @Test
  void toNotification_checkIfMessageIsProper() {

    var learningCsvNotificationRequest = LearningCsvNotificationRequest.builder()
        .fileName("06-06-2021.csv")
        .numberOfSuccessfulAlerts(1500)
        .numberOfFailedAlerts(25)
        .fileLength(104345L)
        .build();

    var notification = learningCsvNotificationCreatorUseCase.createLearningCsvNotification(
        learningCsvNotificationRequest);

    String expectedMessage = "<html>\n"
        + "<body>\n"
        + "\n"
        + "<p>This is to confirm Silent Eight has received a "
        + "CSV file.</p>\n"
        + "<p>File name: 06-06-2021.csv</p>\n"
        + "<p>Number of successfully processed records: 1500</p>\n"
        + "<p>Number of unprocessed records: 25</p>\n"
        + "<p>File size: 104345</p>\n"
        + "<p></p>"
        + "<p>Thank you.</p>"
        + "<p>Silent Eight</p>"
        + "</body>\n"
        + "</html>\n";

    assertEquals(expectedMessage, notification.getMessage());
    assertEquals(
        "Silent Eight - Learning data FAILURE",
        notification.getSubject());
  }
}

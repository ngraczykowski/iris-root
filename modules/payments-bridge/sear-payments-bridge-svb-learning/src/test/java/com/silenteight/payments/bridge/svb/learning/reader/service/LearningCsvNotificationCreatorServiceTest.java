package com.silenteight.payments.bridge.svb.learning.reader.service;


import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvNotificationRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.learning.reader.port.LearningCsvNotificationCreatorUseCase;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LearningCsvNotificationCreatorService.class)
class LearningCsvNotificationCreatorServiceTest {

  @Autowired
  private LearningCsvNotificationCreatorUseCase learningCsvNotificationCreatorUseCase;

  @Test
  void createLearningCsvNotification_checkIfNotificationIsProper() {

    List<ReadAlertError> readAlertErrors = List.of(
        ReadAlertError.builder()
            .alertId("DIN20211222235853-07583-31686")
            .exception(new UnsupportedMessageException("Tag not supported 47A"))
            .build(),
        ReadAlertError.builder()
            .alertId("DIN20211225455643-03499-11223")
            .exception(new UnsupportedMessageException("Tag '108' not found"))
            .build()
    );

    var learningCsvNotificationRequest = LearningCsvNotificationRequest.builder()
        .fileName("06-06-2021.csv")
        .numberOfSuccessfulAlerts(1500)
        .numberOfFailedAlerts(25)
        .fileLength(104345L)
        .readAlertErrors(readAlertErrors)
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
    assertEquals("LEARNING_CSV_ERRORS.zip", notification.getAttachmentName());
    assertNotNull(notification.getAttachment());
  }
}

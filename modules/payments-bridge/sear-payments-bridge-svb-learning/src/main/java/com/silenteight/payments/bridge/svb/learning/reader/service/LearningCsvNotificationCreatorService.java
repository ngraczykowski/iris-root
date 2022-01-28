package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvNotificationRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.LearningCsvNotificationCreatorUseCase;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
class LearningCsvNotificationCreatorService implements LearningCsvNotificationCreatorUseCase {

  private static final String CSV_PROCESSED_SUBJECT = "Silent Eight - Learning data";
  private static final String CSV_PROCESSED = "CSV_PROCESSED";

  @Override
  public Notification createLearningCsvNotification(
      LearningCsvNotificationRequest learningCsvNotificationRequest) {

    String subject = getSubject(learningCsvNotificationRequest.getNumberOfFailedAlerts());
    String message = getCsvAlertsProcessedMessage(learningCsvNotificationRequest);

    return Notification.builder()
        .type(CSV_PROCESSED)
        .subject(subject)
        .message(message)
        .build();

  }

  private static String getCsvAlertsProcessedMessage(
      LearningCsvNotificationRequest learningCsvNotificationRequest) {
    return String.format(
        "<html>\n"
            + "<body>\n"
            + "\n"
            + "<p>This is to confirm Silent Eight has received a "
            + "CSV file.</p>\n"
            + "<p>File name: %s</p>\n"
            + "<p>Number of successfully processed records: %d</p>\n"
            + "<p>Number of unprocessed records: %d</p>\n"
            + "<p>File size: %d</p>\n"
            + "<p></p>"
            + "<p>Thank you.</p>"
            + "<p>Silent Eight</p>"
            + "</body>\n"
            + "</html>\n",
        learningCsvNotificationRequest.getFileName(),
        learningCsvNotificationRequest.getNumberOfSuccessfulAlerts(),
        learningCsvNotificationRequest.getNumberOfFailedAlerts(),
        learningCsvNotificationRequest.getFileLength());
  }

  private static String getSubject(int numberOfFailedAlerts) {
    String suffix = numberOfFailedAlerts == 0 ? "SUCCESS" : "FAILURE";
    return String.format("%s %s", CSV_PROCESSED_SUBJECT, suffix);
  }
}

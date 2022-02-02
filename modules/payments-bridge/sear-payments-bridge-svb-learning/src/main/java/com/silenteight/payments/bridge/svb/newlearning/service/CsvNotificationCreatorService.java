package com.silenteight.payments.bridge.svb.newlearning.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningCsvNotificationRequest;
import com.silenteight.payments.bridge.svb.newlearning.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningCsvNotificationCreatorUseCase;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.silenteight.payments.bridge.common.app.FileProcessor.createTempFile;
import static com.silenteight.payments.bridge.common.app.FileProcessor.createZipFile;
import static com.silenteight.payments.bridge.common.app.FileProcessor.deleteFile;
import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
@RequiredArgsConstructor
@Service
class CsvNotificationCreatorService implements LearningCsvNotificationCreatorUseCase {

  private static final String[] HEADERS =
      { "System Id", "Message" };
  private static final String CSV_PROCESSED_SUBJECT = "Silent Eight - Learning data";
  private static final String CSV_PROCESSED = "CSV_PROCESSED";
  private static final String CSV_PROCESSED_ERRORS_FILE_NAME = "LEARNING_CSV_ERRORS";
  private static final String CSV_EXTENSION = ".csv";
  private static final String ZIP_EXTENSION = ".zip";

  @Override
  public Notification createLearningCsvNotification(
      LearningCsvNotificationRequest learningCsvNotificationRequest) {

    String subject = getSubject(learningCsvNotificationRequest.getNumberOfFailedAlerts());
    String message = getCsvAlertsProcessedMessage(learningCsvNotificationRequest);
    byte[] attachment = createAttachment(learningCsvNotificationRequest);

    return Notification.builder()
        .type(CSV_PROCESSED)
        .subject(subject)
        .message(message)
        .attachmentName(CSV_PROCESSED_ERRORS_FILE_NAME + ZIP_EXTENSION)
        .attachment(attachment)
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

  private static byte[] createAttachment(
      LearningCsvNotificationRequest learningCsvNotificationRequest) {
    File csvFile = createCsvFile(learningCsvNotificationRequest.getReadAlertErrors());
    File zippedAttachment = null;
    try {
      zippedAttachment = createZipFile(csvFile);
      return Files.readAllBytes(zippedAttachment.toPath());
    } catch (IOException exception) {
      log.error(
          "Error during creating attachment. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    } finally {
      deleteFile(csvFile);
      deleteFile(zippedAttachment);
    }
  }

  private static File createCsvFile(List<ReadAlertError> readAlertErrors) {
    File file = createTempFile(CSV_PROCESSED_ERRORS_FILE_NAME, CSV_EXTENSION);

    try (
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile)) {
      List<String[]> data = new ArrayList<>();
      data.add(HEADERS);
      readAlertErrors.forEach(error -> data.add(new String[] {
          error.getAlertId(), error.getExceptionMessage() }));
      writer.writeAll(data);
      return file;
    } catch (IOException exception) {
      log.error(
          "Error during creating csv file. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }
  }

}

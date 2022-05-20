package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.datasource.model.CmapiNotificationRequest;
import com.silenteight.payments.bridge.firco.datasource.port.CmapiNotificationCreatorUseCase;
import com.silenteight.payments.bridge.notification.model.Notification;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.silenteight.payments.bridge.common.app.FileProcessor.createTempFile;
import static com.silenteight.payments.bridge.common.app.FileProcessor.deleteFile;
import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
@RequiredArgsConstructor
@Service
class CmapiNotificationCreatorService implements CmapiNotificationCreatorUseCase {

  private static final String[] HEADERS =
      { "Alert Id", "Alert Name", "Message Id", "System Id", "Message" };

  private static final String CMAPI_PROCESSING_ERROR = "CMAPI_PROCESSING_ERROR";
  private static final String CMAPI_PROCESSING_ERROR_SUBJECT =
      "Silent Eight - CMAPI - alert's processing errors";
  private static final String TEMP_FILE_NAME = "CMAPI_ERRORS";
  private static final String CSV_EXTENSION = ".csv";

  @Override
  public Notification createCmapiNotification(CmapiNotificationRequest cmapiNotificationRequest) {

    return Notification.builder()
        .type(CMAPI_PROCESSING_ERROR)
        .subject(CMAPI_PROCESSING_ERROR_SUBJECT)
        .message(createMessage())
        .attachment(createAttachment(cmapiNotificationRequest))
        .attachmentName(TEMP_FILE_NAME + CSV_EXTENSION)
        .build();
  }


  private static String createMessage() {
    return "<html>\n"
        + "<body>\n"
        + "<p>This is to let you know there was an error with alert’s processing.</p>\n"
        + "<p>Please see the details in the file attached.</p>\n"
        + "<br>\n"
        + "<p>Thank you.</p>\n"
        + "<p>Silent Eight</p>\n"
        + "</body>\n"
        + "</html>\n";
  }

  private static byte[] createAttachment(CmapiNotificationRequest cmapiNotificationRequest) {

    File csvAttachment = createCsvFile(cmapiNotificationRequest);
    try {
      return Files.readAllBytes(csvAttachment.toPath());
    } catch (IOException exception) {
      log.error(
          "Error during attachment creation. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    } finally {
      deleteFile(csvAttachment);
    }
  }

  private static File createCsvFile(CmapiNotificationRequest cnr) {
    File file = createTempFile(cnr.getAlertId(), CSV_EXTENSION);

    try (
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile)) {
      List<String[]> data = new ArrayList<>();
      data.add(HEADERS);
      data.add(new String[] {
          cnr.getAlertId(), cnr.getAlertName(), cnr.getMessageId(), cnr.getSystemId(),
          cnr.getMessage() });
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
